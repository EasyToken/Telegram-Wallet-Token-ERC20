package info.bcdev.telegramwallet.ethereum.pages;

import com.vdurmont.emoji.EmojiParser;
import info.bcdev.telegramwallet.bot.BotInstance;
import info.bcdev.telegramwallet.bot.KeyBoards;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.ArrayList;
import java.util.List;

public class PageAbout implements KeyBoards, BotInstance {

    private SendMessage sendMessage = new SendMessage();

    public void openAbout(Message message){

        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());

        List<String> list = new ArrayList<>();
        String em;
        em = EmojiParser.parseToUnicode("\uD83D\uDCB8");
        list.add(em+" Thanks");
        em = EmojiParser.parseToUnicode("\uD83D\uDC48");
        list.add(em+" Back");

        ReplyKeyboardMarkup replyKeyboardMarkup = getReply(1,list);

        String msg = "Telegram Wallet is your personal wallet.\n" +
                "The safety of the keys depends on you.\n" +
                "The wallet is distributed under the Apache license as is.\n" +
                "All liability for loss of data or money from your wallet is at your own risk\n" +
                "\nFor any questions, please contact [Help](https://t.me/joinchat/D62dXAwO6kkm8hjlJTR9VA)\n" +
                "\nSupport the development of the project /thanks";

        sendMessage.setText(msg);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        try{
            tbot.execute(sendMessage);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
