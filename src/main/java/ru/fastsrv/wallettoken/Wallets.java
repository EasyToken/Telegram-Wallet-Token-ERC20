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
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthAccounts;
import org.web3j.protocol.http.HttpService;
import org.web3j.protocol.parity.Parity;

public class Wallets {
    
        SendMessage sendMessage = new SendMessage();
            
        public void GetAccounts(String url, Update update) throws IOException, ParseException {
        WalletToken wt = new WalletToken();
        Web3j web3 = Web3j.build(new HttpService(url));
            Parity parity = Parity.build(new HttpService(url));
    
            EthAccounts ea = web3.ethAccounts().send();
                
                ///////////////////////////////////
        EditMessageText editMessage = new EditMessageText();
        editMessage.setChatId(String.valueOf(update.getCallbackQuery().getMessage().getChatId()));
        editMessage.setMessageId(update.getCallbackQuery().getMessage().getMessageId()); 
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(); 
            List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
            
        for (int x = 0; x < ea.getAccounts().size(); x++) {
                List<InlineKeyboardButton> RI = new ArrayList<>();
                RI.add(new InlineKeyboardButton().setText(ea.getAccounts().get(x)).setCallbackData("/Wallet:"+x));
                keyboard.add(RI);          
        }
           
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
