package ru.fastsrv.wallettoken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.telegram.telegrambots.api.methods.send.SendMessage;
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

public class Wallets {
    
        SendMessage sendMessage = new SendMessage();
             Config config = new Config();
             
        public void GetAccounts(String url, Update update) throws IOException, ParseException, CipherException {
        WalletToken wt = new WalletToken();
        Web3j web3 = Web3j.build(new HttpService(url));

            Credentials credentials = WalletUtils.loadCredentials("", config.getPathWalletKey()+""+config.getFileNameKey());
                
                ///////////////////////////////////
        EditMessageText editMessage = new EditMessageText();
        editMessage.setChatId(String.valueOf(update.getCallbackQuery().getMessage().getChatId()));
        editMessage.setMessageId(update.getCallbackQuery().getMessage().getMessageId()); 
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(); 
            List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
            
            String walletaddress = credentials.getAddress();
           List<InlineKeyboardButton> RI = new ArrayList<>();
            RI.add(new InlineKeyboardButton().setText(walletaddress).setCallbackData("/Wallet"));
            keyboard.add(RI);
            
            List<InlineKeyboardButton> RI1 = new ArrayList<>();
            RI1.add(new InlineKeyboardButton().setText("Back").setCallbackData("/start"));
            keyboard.add(RI1);
            
        String msg ="Wallets:";
        
        markup.setKeyboard(keyboard);
        
        editMessage.setText(msg);
        editMessage.setReplyMarkup(markup);
        
        try {
            wt.editMessageText(editMessage);
                } catch (TelegramApiException ex) {}
                
    }
    
}
