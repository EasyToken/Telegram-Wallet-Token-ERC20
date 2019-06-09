package info.bcdev.telegramwallet.ethereum;

import info.bcdev.telegramwallet.Main;
import info.bcdev.telegramwallet.Settings;
import info.bcdev.telegramwallet.bot.Keyboard;
import info.bcdev.telegramwallet.bot.Tbot;
import info.bcdev.telegramwallet.erc20.TokenERC20;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.web3j.tx.Transfer.GAS_LIMIT;
import static org.web3j.tx.gas.DefaultGasProvider.GAS_PRICE;


public class Wallet extends Keyboard {
    SendMessage sendMessage = new SendMessage();
    Settings settings = Main.settings;

    File[] listfiles;
    Credentials credentials;

    Tbot tbot = Tbot.INSTANCE;

    private void loadFileWallet() throws IOException, CipherException {
        File KeyDir = new File(settings.getWalletDir());
        if (!KeyDir.exists()) {
            KeyDir.mkdirs();
        } else {
            // Проверяем есть ли кошельки
            listfiles = KeyDir.listFiles();
            System.out.println(listfiles.length);
            if (listfiles.length != 0) {
                credentials = WalletUtils.loadCredentials(settings.getWalletPassword(), settings.getWalletDir() + "" + listfiles[0].getName());
            } else {
                System.out.println("File Wallet not found");
            }

        }
    }

    public void Wallet (Update update) throws IOException, CipherException {
        Tbot tbot = Tbot.INSTANCE;

        EditMessageText editMessage = new EditMessageText();
        editMessage.setChatId(String.valueOf(update.getCallbackQuery().getMessage().getChatId()));
        editMessage.setMessageId(update.getCallbackQuery().getMessage().getMessageId());

        Web3j web3 = Web3j.build(new HttpService(settings.getNodeUrl()));

        loadFileWallet();

        TokenERC20 token = TokenERC20.load(settings.getTokenAddress(), web3, credentials, GAS_PRICE, GAS_LIMIT);

        try {
            String walletaddress = credentials.getAddress();
            String name = token.name().send();
            System.out.println("Token Name: " +name);

            String symbol = token.symbol().send();
            System.out.println("Token Symbol: "+symbol);

            String address = token.getContractAddress();
            System.out.println("Token Adress: "+address);

            BigInteger totalSupply = token.totalSupply().send();
            System.out.println("Token Total Sypply: "+ totalSupply.toString());

            BigInteger balance = token.balanceOf(walletaddress).send();
            System.out.println("Token Balance: " + walletaddress + " : " + balance.toString());

            ///////////////////////////////////////////

            Map<String, String> keyboard = new HashMap<>();
            keyboard.put("Back","/Wallets");

            InlineKeyboardMarkup inlineKB = getInline(1,keyboard);


            //File file = QRCode.from(walletaddress).to(ImageType.PNG).withSize(250, 250).file();


            String msg =""
                    + "\n Address: "+walletaddress+": "
                    + "\n Name Token: "+name+""
                    + "\n Balance: "+balance+" "+symbol;

            editMessage.setText(msg);
            editMessage.setReplyMarkup(inlineKB);

            tbot.execute(editMessage);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void SendToken (Message message, String data) throws IOException, CipherException {

        loadFileWallet();

        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());

        String[] send_value = data.split(" ");

        Web3j web3 = Web3j.build(new HttpService(settings.getNodeUrl()));
        System.out.println(settings.getWalletDir());
        System.out.println(listfiles.length);
        Credentials credentials = WalletUtils.loadCredentials(settings.getWalletPassword(), settings.getWalletDir()+""+listfiles[0].getName());

        BigInteger gasPrice = BigInteger.valueOf(440000);
        BigInteger gasLimit = BigInteger.valueOf(55000);

        TokenERC20 token = TokenERC20.load(settings.getTokenAddress(), web3, credentials, gasPrice, gasLimit);

        int value = Integer.valueOf(send_value[2]);

        try {
            System.out.println(send_value[1]);
            System.out.println(BigInteger.valueOf(value));
            String status = token.transfer(send_value[1], BigInteger.valueOf(value)).send().getStatus();

            InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

            List<InlineKeyboardButton> RI1 = new ArrayList<>();
            RI1.add(new InlineKeyboardButton().setText("Back").setCallbackData("/Wallet"));
            keyboard.add(RI1);

            markup.setKeyboard(keyboard);

            sendMessage.setReplyMarkup(markup);

            if (status.equals("0x1")) {status = "successfully";} else {status = "not successful";}
            System.out.println(status);
            sendMessage.setText("Send Token status: " + status);


            tbot.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
