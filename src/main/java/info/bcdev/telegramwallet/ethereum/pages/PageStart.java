package info.bcdev.telegramwallet.ethereum.pages;

import com.vdurmont.emoji.EmojiParser;
import info.bcdev.telegramwallet.bot.BotInstance;
import info.bcdev.telegramwallet.bot.KeyBoards;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public class PageStart implements KeyBoards, BotInstance {

    private SendMessage sendMessage = new SendMessage();

    private Long chatID;

    public void setChatID(Long chatID) {
        this.chatID = chatID;
    }

    public void CmdStart ()  {
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatID);

        List<String> keyboard = new ArrayList<>();

        String em = EmojiParser.parseToUnicode("\uD83D\uDCBC");
        keyboard.add(em + " Wallets");
        em = EmojiParser.parseToUnicode("\uD83D\uDECE");
        keyboard.add(em + " About");
        em = EmojiParser.parseToUnicode("\uD83D\uDCB8");
        keyboard.add(em + " Thanks");

        ReplyKeyboardMarkup replyKeyboardMarkup = getReply(1, keyboard);
        String msg = "Hello ";
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
