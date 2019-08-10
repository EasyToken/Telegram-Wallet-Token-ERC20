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

public class PageTnx implements KeyBoards, BotInstance{

        private SendMessage sendMessage = new SendMessage();

        public void openTnx(Message message){

        String chatID = message.getChatId().toString();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatID);

        List<String> list = new ArrayList<>();
        String em;
        em = EmojiParser.parseToUnicode("\uD83D\uDC48");
        list.add(em+" Back");

        ReplyKeyboardMarkup replyKeyboardMarkup = getReply(2,list);

        String msg = "You can support the development of this project and others. \n" +
                     "Your support will go to the development of existing and implementation of new projects.\n" +
                "\n" +
                "Ether: 0x7267Bf407529720B08A65992c07758F341dbF7F5";

        sendMessage.setText(msg);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        try{
            tbot.execute(sendMessage);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
