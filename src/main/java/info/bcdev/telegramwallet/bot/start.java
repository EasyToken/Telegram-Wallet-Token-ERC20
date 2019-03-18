package info.bcdev.telegramwallet.bot;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class start extends Keyboard {

    SendMessage sendMessage = new SendMessage();

    @Override
    public InlineKeyboardMarkup getInline(Integer rows, Map<String, String> m) {
        return super.getInline(rows, m);
    }

    @Override
    public ReplyKeyboardMarkup getReply(Integer rows, List<String> l) {
        return super.getReply(rows, l);
    }

/*    public void startPage(Message message){
            sendMessage.enableMarkdown(true);
            sendMessage.setChatId(message.getChatId().toString());

        List<String> list = new ArrayList<>();
        list.add("@Ethereum");

        sendMessage.setReplyMarkup(getReply(2,list));
            sendMessage.setText("Wallet for Crypto curency");

            try {
                Tbot.INSTANCE.execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }

*//*        sqlActions.createTableSession();

        sqlActions.addSession(message.getChatId().toString(),"start");*//*
    }*/

    Tbot tbot = Tbot.INSTANCE;

    public void CmdStart (Message message)  {
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());

        System.out.println(message.getChat());

        Map<String, String> keyboard = new HashMap<>();
        keyboard.put("Wallets","/Wallets");

        InlineKeyboardMarkup inlineKB = getInline(keyboard.size()+1,keyboard);

        sendMessage.setReplyMarkup(inlineKB);
        sendMessage.setText("Wallet for Ethereum Tokens");
        try {
            tbot.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void CmdStartMenu (Update update)  {
        EditMessageText editMessage = new EditMessageText();
        editMessage.setChatId(String.valueOf(update.getCallbackQuery().getMessage().getChatId()));
        editMessage.setMessageId(update.getCallbackQuery().getMessage().getMessageId());

        Map<String, String> keyboard = new HashMap<>();
        keyboard.put("Wallets","/Wallets");

        InlineKeyboardMarkup inlineKB = getInline(1,keyboard);

        String msg ="Wallet for Ethereum Tokens";

        editMessage.setText(msg);
        editMessage.setReplyMarkup(inlineKB);

        try {
            tbot.execute(editMessage);
        } catch (TelegramApiException ex) {}
    }



}
