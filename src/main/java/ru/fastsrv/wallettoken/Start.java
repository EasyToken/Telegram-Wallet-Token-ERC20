package ru.fastsrv.wallettoken;

import java.util.ArrayList;
import java.util.List;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class Start {
    
        SendMessage sendMessage = new SendMessage();
            WalletToken wt = new WalletToken();
        
        public void CmdStart (Message message)  {
            sendMessage.enableMarkdown(true);
            sendMessage.setChatId(message.getChatId().toString());
        System.out.println(message.getChat());    

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
                List<InlineKeyboardButton> RowInline1 = new ArrayList<>();
                    List<InlineKeyboardButton> RowInline2 = new ArrayList<>();
                    
                RowInline1.add(new InlineKeyboardButton().setText("Wallets").setCallbackData("/Wallets"));
                    RowInline2.add(new InlineKeyboardButton().setText("Add Token").setCallbackData("/AddToken"));
        
            keyboard.add(RowInline1);
                keyboard.add(RowInline2);
                
            markup.setKeyboard(keyboard);

        sendMessage.setReplyMarkup(markup);
        sendMessage.setText("Wallet for Ethereum Tokens");
        try {
			wt.sendMessage(sendMessage);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
    }

}