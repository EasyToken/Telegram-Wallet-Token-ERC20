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

public class start implements KeyBoards {

    SendMessage sendMessage = new SendMessage();

    Tbot tbot = Tbot.INSTANCE;

    public void CmdStart (Message message)  {
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());

        System.out.println(message);

        List<String> keyboard = new ArrayList<>();

        String em = EmojiParser.parseToUnicode("\uD83D\uDCBC");
        keyboard.add(em+" Wallets");
        em = EmojiParser.parseToUnicode("\uD83D\uDECE");
        keyboard.add(em+" About");
        em = EmojiParser.parseToUnicode("\uD83D\uDCB8");
        keyboard.add(em+" Thanks");

        ReplyKeyboardMarkup replyKeyboardMarkup = getReply(1, keyboard);
        String msg ="This Wallet";
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

        String msg ="loadWallet for Ethereum Tokens";

        editMessage.setText(msg);
        editMessage.setReplyMarkup(inlineKB);

        try {
            tbot.execute(sendMessage);
        } catch (TelegramApiException ex) {}
    }

*/


}
