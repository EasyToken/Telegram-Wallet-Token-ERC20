package info.bcdev.telegramwallet.ethereum.pages;

import com.vdurmont.emoji.EmojiParser;
import info.bcdev.telegramwallet.Main;
import info.bcdev.telegramwallet.Settings;
import info.bcdev.telegramwallet.bot.BotInstance;
import info.bcdev.telegramwallet.bot.KeyBoards;
import info.bcdev.telegramwallet.bot.session.Session;
import info.bcdev.telegramwallet.ethereum.CreateEW;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import javax.activation.MailcapCommandMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static info.bcdev.telegramwallet.bot.session.Session.SETTINGS;

public class PageRecovery implements KeyBoards, BotInstance {

    private SendMessage sendMessage = new SendMessage();

    public void enterSeed(Message message){

        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());

        List<String> list = new ArrayList<>();
        String em;
        em = EmojiParser.parseToUnicode("\uD83D\uDC48");
        list.add(em+" Back");

        ReplyKeyboardMarkup replyKeyboardMarkup = getReply(1,list);

        String msg = "Enter your SEED PHRASE for restore wallet:";

        sendMessage.setText(msg);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        try{
            tbot.execute(sendMessage);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void recoveryWallet(Long chatID, String seedCode){

        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatID);

        List<String> list = new ArrayList<>();
        String em;
        em = EmojiParser.parseToUnicode("\uD83D\uDC48");
        list.add(em+" Back");

        ReplyKeyboardMarkup replyKeyboardMarkup = getReply(1,list);

        String msg = "";
        System.out.println(seedCode);
        if (seedCode.split(" ").length == 12) {
            Map<String, String> result = new CreateEW(SETTINGS).CreateEW(seedCode);
            if (result != null){
                msg = "Wallet with addrees "+ result.get("address") + " recovery Success. \n" +
                        "filenme wallet: " + result.get("filewallet");
            }
        } else {
            msg = "Recovery Wallet Fail";
        }

        sendMessage.setText(msg);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        try{
            tbot.execute(sendMessage);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}
