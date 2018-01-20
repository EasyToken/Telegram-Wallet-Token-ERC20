package ru.fastsrv.wallettoken;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import org.telegram.telegrambots.api.methods.updatingmessages.EditMessageText;
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

public class Wallet {
 Config config = new Config();
    public void Wallet (Update update, String data) throws IOException {
        WalletToken wt = new WalletToken();
        int idaddress = Integer.valueOf(data.replaceAll("/Wallet:", ""));
        
Web3j web3 = Web3j.build(new HttpService(config.getUrl()));
            Parity parity = Parity.build(new HttpService(config.getUrl()));
    
            EthAccounts ea = web3.ethAccounts().send();
            System.out.println(ea);

String account = web3.ethAccounts().send().getAccounts().get(idaddress);
Credentials credentials = Credentials.create(account);

        TokenERC20 token = TokenERC20.load(config.getTokenAddress(), web3, credentials, BigInteger.valueOf(3000000), BigInteger.valueOf(2100000));
System.out.println(token);
        try {
        String name = token.name().send();
        System.out.println("Token Name: " +name);
        
        String symbol = token.symbol().send();
        System.out.println("Token Symbol: "+symbol);
        
        String address = token.getContractAddress();
        System.out.println("Token Adress: "+address);
        
        BigInteger totalSupply = token.totalSupply().send();
        System.out.println("Token Total Sypply: "+ totalSupply.toString());
        
        BigInteger balance = token.balanceOf(ea.getAccounts().get(idaddress)).send();
        System.out.println("Token Balance: " + account + " : " + balance.toString());
        
        ////////////////////////////////////////////
        EditMessageText editMessage = new EditMessageText();
        editMessage.setChatId(String.valueOf(update.getCallbackQuery().getMessage().getChatId()));
        editMessage.setMessageId(update.getCallbackQuery().getMessage().getMessageId()); 
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(); 
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        
        
        
        List<InlineKeyboardButton> RI1 = new ArrayList<>();
        RI1.add(new InlineKeyboardButton().setText("Back").setCallbackData("/Wallets"));
        RI1.add(new InlineKeyboardButton().setText("Send Token").setCallbackData("/SendToken"));
        keyboard.add(RI1);
        
                String msg ="Address: "+account +": "
                        + "\n Name Token: "+name+""
                        + "\n Balance: "+balance+" "+symbol;
        
        markup.setKeyboard(keyboard);
        
        editMessage.setText(msg);
        editMessage.setReplyMarkup(markup);
        
            wt.editMessageText(editMessage);
    
        } catch (Exception ex) {System.out.println("Exception: "+ex);}

    }
    
}
