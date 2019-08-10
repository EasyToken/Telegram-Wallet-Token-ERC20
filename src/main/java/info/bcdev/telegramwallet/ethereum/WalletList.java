package info.bcdev.telegramwallet.ethereum;

import com.vdurmont.emoji.EmojiParser;
import info.bcdev.telegramwallet.bot.BotInstance;
import info.bcdev.telegramwallet.bot.KeyBoards;
import info.bcdev.telegramwallet.bot.LoadWalletList;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.web3j.crypto.WalletUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static info.bcdev.telegramwallet.bot.session.Session.*;

public class WalletList implements KeyBoards, BotInstance {

    private SendMessage sendMessage = new SendMessage();
    private Long chatID;

    public void setChatID(Long chatID) {
        this.chatID = chatID;
    }

    public void loadWalletList() {

        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatID);

        WALLET_INSTANCE_LIST = new LoadWalletList().getWalletList();

        String msg;
        if (WALLET_INSTANCE_LIST.isEmpty()) {
            msg ="Your wallet list, is Empty";
        } else {
            msg ="Your wallet list, is Loaded";
        }

        List<String> list = getWalletAdresses(WALLET_INSTANCE_LIST);
        String em;
        em = EmojiParser.parseToUnicode("\uD83D\uDCCB");
        list.add(em+ " Create Wallet");
        em = EmojiParser.parseToUnicode("\uD83D\uDD17");
        list.add(em + " RecoveryWallet");
        em = EmojiParser.parseToUnicode("⚙");
        list.add(em+ " PageSettings");
        em = EmojiParser.parseToUnicode("\uD83D\uDC48");
        list.add(em+" Back");

        ReplyKeyboardMarkup replyKeyboardMarkup = getReply(1,list);

        sendMessage.setText(msg);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);

        try {
            tbot.execute(sendMessage);
        } catch (TelegramApiException ex) {}

    }

    private List<String> getWalletAdresses(List<WalletsInstance> list){
        List<String> addresslist = new ArrayList<>();
        if (list != null) {
            String em;
            for (WalletsInstance walletsInstance : list) {
                em = EmojiParser.parseToUnicode("\uD83D\uDCB0");
                addresslist.add(em+" "+walletsInstance.getWalletaddress());
            }
        }
        return addresslist;
    }

    public Boolean createWallet(){
        File KeyDir = new File(SETTINGS.getWalletDir());
        if (!KeyDir.exists()) {
            KeyDir.mkdirs();
        } else {
                try {
                    WalletUtils.generateNewWalletFile(SETTINGS.getWalletPassword(), KeyDir, false);
                } catch (Exception ex) {
                    System.out.println(ex);
                }
                return true;
        }
        return false;
    }

}
