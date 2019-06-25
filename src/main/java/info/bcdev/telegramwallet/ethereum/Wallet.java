package info.bcdev.telegramwallet.ethereum;

import com.vdurmont.emoji.EmojiParser;
import info.bcdev.telegramwallet.Main;
import info.bcdev.telegramwallet.Settings;
import info.bcdev.telegramwallet.bot.Keyboard;
import info.bcdev.telegramwallet.bot.Tbot;
import info.bcdev.telegramwallet.erc20.TokenERC20;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Convert;

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
    private SendMessage sendMessage = new SendMessage();
    private Settings settings = Main.settings;

    /*private File[] listfiles;*/
    private Credentials credentials;

    private Tbot tbot;
    public static Map<String,String> TRANSACTION_VALUE = new HashMap<>();

    /*
    @Deprecated
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
    */

    public void Wallet (Update update) throws IOException, CipherException {
        tbot = Tbot.INSTANCE;
        Message message = update.getMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());

        String walletaddress = message.getText().split(" ")[1];
        setActiveWallet(walletaddress);
        credentials = getCredentials(walletaddress);

        Web3j web3 = Web3j.build(new HttpService(settings.getNodeUrl()));

        /*
        @Deprecated
        TokenERC20 token = TokenERC20.load(settings.getTokenAddress(), web3, credentials, GAS_PRICE, GAS_LIMIT);
        */

        ContractGasProvider contractGasProvider = new DefaultGasProvider();
        TokenERC20 token = TokenERC20.load(settings.getTokenAddress(), web3, credentials, contractGasProvider);

        try {
           // String walletaddress = credentials.getAddress();
            String name = token.name().send();

            String symbol = token.symbol().send();

            String address = token.getContractAddress();

            BigInteger totalSupply = token.totalSupply().send();

            BigInteger balance = token.balanceOf(walletaddress).send();

            ///////////////////////////////////////////

            /*Map<String, String> keyboard = new HashMap<>();
            keyboard.put("Back","/Wallets");

            InlineKeyboardMarkup inlineKB = getInline(1,keyboard);*/


            //File file = QRCode.from(walletaddress).to(ImageType.PNG).withSize(250, 250).file();

            List<String> list = new ArrayList<>();
            String em;
            em = EmojiParser.parseToUnicode("\uD83D\uDCB3");
            list.add(em + " SendToken");

            em = EmojiParser.parseToUnicode("\uD83D\uDC48");
            list.add(em+" Back");
            ReplyKeyboardMarkup replyKeyboardMarkup = getReply(2,list);

            String msg =""
                    + "\n Address: "+walletaddress+": "
                    + "\n Name Token: "+name+""
                    + "\n Balance: "+balance+" "+symbol;

            /*editMessage.setText(msg);
            editMessage.setReplyMarkup(inlineKB);*/

            sendMessage.setText(msg);
            sendMessage.setReplyMarkup(replyKeyboardMarkup);

            tbot.execute(sendMessage);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private Credentials getCredentials(String walletaddress){
        for (WalletsInstance walletsInstance : Settings.WALLET_INSTANCE_LIST){
            if(walletsInstance.checkWallet(walletaddress)){
                return walletsInstance.getCredentials();
            }
        }
        return null;
    }

    public void EnterAddress(Message message) {

        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());

        sendMessage.setText("Enter wallet address");
        setSendStep("enterammount");
        try {
            tbot.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void EnterAmmount(Message message) {
        Tbot tbot = Tbot.INSTANCE;

        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());

        sendMessage.setText("Enter wallet ammount");
        setSendStep("sending");
        try {
            tbot.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void SendToken(Message message) {

        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());

        /*sendMessage.setText("Sending...");
        try {
            tbot.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        Send(message);
    }

    public void Send(Message message) {

        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());

        Web3j web3 = Web3j.build(new HttpService(settings.getNodeUrl()));

        Credentials credentials = getCredentials(getActiveWallet());

        /*
        @Deprecated
        TokenERC20 token = TokenERC20.load(settings.getTokenAddress(), web3, credentials, getGasPrice("50"), getGasLimit("60000"));
        */

        ContractGasProvider contractGasProvider = new DefaultGasProvider();
        TokenERC20 token = TokenERC20.load(settings.getTokenAddress(), web3, credentials, contractGasProvider);

        String addressto = TRANSACTION_VALUE.get("addressto");
        int ammount = Integer.valueOf(TRANSACTION_VALUE.get("ammount"));


        try {
            String status = token.transfer(addressto, BigInteger.valueOf(ammount)).send().getStatus();

            if (status.equals("0x1")) {status = "successfully";} else {status = "not successful";}
            sendMessage.setText("Send Token status: " + status);

            tbot.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


/*    private BigInteger getGasPrice(String mValueGasPrice){
        return Convert.toWei(mValueGasPrice, Convert.Unit.GWEI).toBigInteger();
    }

    private BigInteger getGasLimit(String mValueGasLimit){
        return BigInteger.valueOf(Long.valueOf(mValueGasLimit));
    }*/

    private void setActiveWallet(String w){
        Settings.ACTIVE_WALLET = w;
    }

    private String getActiveWallet(){
        return Settings.ACTIVE_WALLET;
    }

    private void setSendStep(String s){
        Settings.SEND_STEP = s;
    }
}
