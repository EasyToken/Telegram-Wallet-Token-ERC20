package info.bcdev.telegramwallet.ethereum;

import com.vdurmont.emoji.EmojiParser;
import info.bcdev.telegramwallet.Main;
import info.bcdev.telegramwallet.Settings;
import info.bcdev.telegramwallet.bot.Keyboard;
import info.bcdev.telegramwallet.bot.Tbot;
import info.bcdev.telegramwallet.qr.QRCode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WalletList extends Keyboard {

    SendMessage sendMessage = new SendMessage();
    Settings settings = Main.settings;
    File[] listfiles;

    public void loadWalletList(Message message) throws IOException, CipherException {
        Tbot tbot = Tbot.INSTANCE;

        QRCode qrCode = new QRCode();

        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());

        List<WalletsInstance> walletsInstanceList = new ArrayList<>();

        //////////////////////
        File KeyDir = new File(settings.getWalletDir());
        if (!KeyDir.exists()) {
            KeyDir.mkdirs();
        } else {

            listfiles = loadFiles(KeyDir);

            for (int i = 0; i < listfiles.length; i++){

                //////////////////////
                if (listfiles[i].getName().matches("UTC--.+\\.json")) {
                    Credentials credentials = WalletUtils.loadCredentials(settings.getWalletPassword(), settings.getWalletDir() + "" + listfiles[i].getName());
                    ///////////////////////////////////
                    String walletaddress = credentials.getAddress();
                    File fileqrcode = qrCode.qrGen(walletaddress,settings.getQRCodeDir()).toFile();
                    walletsInstanceList.add(new WalletsInstance(walletaddress, credentials, listfiles[i], fileqrcode));
                }
            }
        }
        Settings.WALLET_INSTANCE_LIST = walletsInstanceList;

        String em;

        List<String> list = getWalletAdresses(walletsInstanceList);

        em = EmojiParser.parseToUnicode("\uD83D\uDCCB");
        list.add(em+ " Create Wallet");

        list.add("Edit Gas");

        ReplyKeyboardMarkup replyKeyboardMarkup = getReply(1,list);

        String msg ="Your wallet list";

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

    private File[] loadFiles(File KeyDir){
        return KeyDir.listFiles();
    }

    public Boolean createWallet(){
        File KeyDir = new File(settings.getWalletDir());
        if (!KeyDir.exists()) {
            KeyDir.mkdirs();
        } else {
                try {
                    WalletUtils.generateNewWalletFile(settings.getWalletPassword(), KeyDir, false);
                } catch (Exception ex) {
                    System.out.println(ex);
                }
                return true;
        }
        return false;
    }

}
