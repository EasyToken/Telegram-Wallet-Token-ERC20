package info.bcdev.telegramwallet.ethereum;

import info.bcdev.telegramwallet.Main;
import info.bcdev.telegramwallet.Settings;
import info.bcdev.telegramwallet.bot.Keyboard;
import info.bcdev.telegramwallet.bot.Tbot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WalletList extends Keyboard {

    SendMessage sendMessage = new SendMessage();
    Settings settings = Main.settings;
    File[] listfiles;

    public void GetAccounts(String url, Update update) throws IOException, CipherException {
        Tbot tbot = Tbot.INSTANCE;
        Web3j web3 = Web3j.build(new HttpService(url));

        //////////////////////
        File KeyDir = new File(settings.getWalletDir());
        if (!KeyDir.exists()) {
            KeyDir.mkdirs();
        } else {

            // Проверяем есть ли кошельки
            listfiles = KeyDir.listFiles();
            if (listfiles.length == 0 ) {
                // Если в директории нет файла кошелька, добавляем кошелек
                try {
                    String fileName = WalletUtils.generateNewWalletFile(settings.getWalletPassword(), KeyDir, false);
                    System.out.println("FileName: " + KeyDir.getPath() + fileName);
                } catch (Exception ex) {
                    System.out.println(ex);
                }
            } else {
                // Если кошелек создан
                System.out.println("File Wallet: "+ listfiles[0].getName());
            }

        }
        //////////////////////
        listfiles = KeyDir.listFiles();
        Credentials credentials = WalletUtils.loadCredentials(settings.getWalletPassword(), settings.getWalletDir()+""+listfiles[0].getName());

        ///////////////////////////////////
        EditMessageText editMessage = new EditMessageText();
        editMessage.setChatId(String.valueOf(update.getCallbackQuery().getMessage().getChatId()));
        editMessage.setMessageId(update.getCallbackQuery().getMessage().getMessageId());

        String walletaddress = credentials.getAddress();

        Map<String, String> keyboard = new HashMap<>();
        keyboard.put(walletaddress,"/Wallet");
        keyboard.put("Back","/start");

        InlineKeyboardMarkup inlineKB = getInline(1,keyboard);

        String msg ="Wallets:";

        editMessage.setText(msg);
        editMessage.setReplyMarkup(inlineKB);

        try {
            tbot.execute(editMessage);
        } catch (TelegramApiException ex) {}

    }

}
