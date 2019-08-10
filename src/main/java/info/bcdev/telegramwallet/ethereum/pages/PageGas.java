package info.bcdev.telegramwallet.ethereum.pages;

import com.vdurmont.emoji.EmojiParser;
import info.bcdev.telegramwallet.Config;
import info.bcdev.telegramwallet.bot.Keyboard;
import info.bcdev.telegramwallet.bot.Tbot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.ArrayList;
import java.util.List;

public class Gas extends Keyboard {

    private SendMessage sendMessage = new SendMessage();
    private Tbot tbot;

    public void gasEdit(Message message){

        tbot = Tbot.INSTANCE;
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());

        List<String> list = new ArrayList<>();
        String em;
        list.add("GasPrice Value");
        list.add("GasLimit Value");
        em = EmojiParser.parseToUnicode("\uD83D\uDC48");
        list.add(em+" Back");

        ReplyKeyboardMarkup replyKeyboardMarkup = getReply(2,list);

        String msg = "GasPrice = "+ Config.GAS_PRICE_VALUE + " GWEI" +
                "\nGasLimit = " + Config.GAS_LIMIT_VALUE + " WEI";

        sendMessage.setText(msg);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        try{
            tbot.execute(sendMessage);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void gasPriceEdit(Message message){
        tbot = Tbot.INSTANCE;
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());

        List<String> list = new ArrayList<>();
        String em;
        em = EmojiParser.parseToUnicode("\uD83D\uDC48");
        list.add(em+" Back");

        ReplyKeyboardMarkup replyKeyboardMarkup = getReply(2,list);

        String msg = "Write GasPrice value";

        sendMessage.setText(msg);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        try{
            tbot.execute(sendMessage);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void gasLimitEdit(Message message){
        tbot = Tbot.INSTANCE;
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());

        List<String> list = new ArrayList<>();
        String em;
        em = EmojiParser.parseToUnicode("\uD83D\uDC48");
        list.add(em+" Back");

        ReplyKeyboardMarkup replyKeyboardMarkup = getReply(2,list);

        String msg = "Write GasLimit Value";

        sendMessage.setText(msg);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        try{
            tbot.execute(sendMessage);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setGasPrice(String g){
        Config.GAS_PRICE_VALUE = g;
    }

    public void setGasLimit(String g){
        Config.GAS_LIMIT_VALUE = g;
    }
}
