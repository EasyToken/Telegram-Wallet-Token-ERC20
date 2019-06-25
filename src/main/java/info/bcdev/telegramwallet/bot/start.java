package info.bcdev.telegramwallet.bot;

import com.vdurmont.emoji.EmojiParser;
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

    Tbot tbot = Tbot.INSTANCE;

    @Override
    public InlineKeyboardMarkup getInline(Integer rows, Map<String, String> m) {
        return super.getInline(rows, m);
    }

    @Override
    public ReplyKeyboardMarkup getReply(Integer rows, List<String> l) {
        return super.getReply(rows, l);
    }

    public void CmdStart (Message message)  {
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());

        System.out.println(message.getChat());

        List<String> keyboard = new ArrayList<>();

        String em = EmojiParser.parseToUnicode("\uD83D\uDCBC");
        keyboard.add(em+" Wallets");

        ReplyKeyboardMarkup replyKeyboardMarkup = getReply(1, keyboard);
        String msg ="Wallet for Ethereum Tokens";
        sendMessage.setText(msg);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);

        try {
            tbot.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

/*

    @Deprecated
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
            tbot.execute(sendMessage);
        } catch (TelegramApiException ex) {}
    }

*/


}
