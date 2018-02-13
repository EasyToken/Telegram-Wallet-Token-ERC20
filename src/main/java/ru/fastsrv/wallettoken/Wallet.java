package ru.fastsrv.wallettoken;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;
import static org.telegram.telegrambots.api.methods.ParseMode.HTML;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthAccounts;
import org.web3j.protocol.http.HttpService;
import org.web3j.protocol.parity.Parity;

import static org.web3j.tx.Contract.GAS_LIMIT;
import static org.web3j.tx.ManagedTransaction.GAS_PRICE;


public class Wallet {
            SendMessage sendMessage = new SendMessage();
 Config config = new Config();
 
 File[] listfiles;
 Credentials credentials;
 
    public void Wallet (Update update) throws IOException, CipherException {
        WalletToken wt = new WalletToken();
        
Web3j web3 = Web3j.build(new HttpService(config.getUrl()));

//////////////////////
        File KeyDir = new File(config.getDirWalletKey());
        if (!KeyDir.exists()) {
            KeyDir.mkdirs();   
        } else {
                            // Проверяем есть ли кошельки
        listfiles = KeyDir.listFiles();
        if (listfiles.length != 0 ) {
        credentials = WalletUtils.loadCredentials(config.getPassword(), config.getDirWalletKey()+""+listfiles[0].getName());
        } else {
            System.out.println("Файл Кошелька не обнаружен");
        }
            
        }
        //////////////////////

        TokenERC20 token = TokenERC20.load(config.getTokenAddress(), web3, credentials, GAS_PRICE, GAS_LIMIT);

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
        
        ////////////////////////////////////////////
        EditMessageText editMessage = new EditMessageText();
        editMessage.setChatId(String.valueOf(update.getCallbackQuery().getMessage().getChatId()));
        editMessage.setMessageId(update.getCallbackQuery().getMessage().getMessageId()); 
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(); 
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        
            List<InlineKeyboardButton> RI = new ArrayList<>();
            RI.add(new InlineKeyboardButton().setText("Update").setCallbackData("/Wallet"));
            keyboard.add(RI);
            
            List<InlineKeyboardButton> RI2 = new ArrayList<>();
            RI2.add(new InlineKeyboardButton().setText("Back").setCallbackData("/Wallets"));
            keyboard.add(RI2);
  
  
        //File file = QRCode.from(walletaddress).to(ImageType.PNG).withSize(250, 250).file();

        
                String msg =""
                        + "\n Address: "+walletaddress+": "
                        + "\n Name Token: "+name+""
                        + "\n Balance: "+balance+" "+symbol;
        
        markup.setKeyboard(keyboard);
        
        editMessage.setText(msg);
        editMessage.setReplyMarkup(markup);
        
            wt.editMessageText(editMessage);
                            //wt.sendPhoto(sendPhotoRequest);
        } catch (Exception ex) {System.out.println("Exception: "+ex);}

    }
    
    public void SendToken (Message message, String data) throws IOException, CipherException {
        WalletToken wt = new WalletToken();
        
        sendMessage.enableMarkdown(true);
            sendMessage.setChatId(message.getChatId().toString());   
            
        String[] send_value = data.split(" ");

Web3j web3 = Web3j.build(new HttpService(config.getUrl()));

        Credentials credentials = WalletUtils.loadCredentials(config.getPassword(), config.getDirWalletKey()+""+listfiles[0].getName());

        TokenERC20 token = TokenERC20.load(config.getTokenAddress(), web3, credentials, GAS_PRICE, GAS_LIMIT);
        
        int value = Integer.valueOf(send_value[2]);
        try {
        String status = token.transfer(send_value[1], BigInteger.valueOf(value)).send().getStatus();
        ////////////////////////////////////////////
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(); 
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        
        List<InlineKeyboardButton> RI1 = new ArrayList<>();
        RI1.add(new InlineKeyboardButton().setText("Back").setCallbackData("/Wallet"));
        keyboard.add(RI1);
        
        markup.setKeyboard(keyboard);
        
        sendMessage.setReplyMarkup(markup);
        
        if (status.equals("0x1")) {status = "successfully";} else {status = "not successful";}
        sendMessage.setText("Send Token status: " + status);

			wt.sendMessage(sendMessage);

        } catch (Exception ex) {System.out.println("Exception: "+ex);}
    }
    
}
