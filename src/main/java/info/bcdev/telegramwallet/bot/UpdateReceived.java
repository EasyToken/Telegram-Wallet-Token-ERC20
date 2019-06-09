package info.bcdev.telegramwallet.bot;

import com.google.gson.JsonObject;
import info.bcdev.telegramwallet.Main;
import info.bcdev.telegramwallet.Settings;
import info.bcdev.telegramwallet.ethereum.CreateEW;
import info.bcdev.telegramwallet.ethereum.ListEW;
import info.bcdev.telegramwallet.ethereum.Wallet;
import info.bcdev.telegramwallet.ethereum.WalletList;
import info.bcdev.telegramwallet.ethereum.pages.PageCreate;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

public class UpdateReceived {

    private SendMessage sendMessage = new SendMessage();

    private Wallet wallet = new Wallet();
    private WalletList walletlist = new WalletList();

    private Settings settings = Main.settings;

    public void Update(Update update) {
        start start = new start();
        Message message = update.getMessage();
        List<KeyboardRow> keyboard = null;
        CallbackQuery query = update.getCallbackQuery();

        if (message != null && message.hasText()) {
            System.out.println(message.getText());
            if (message.getText().equals("/start")) {
                start.CmdStart(message);
            } else if (message.getText().startsWith("/Send")) {
                try {
                    wallet.SendToken(message, message.getText());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } else if (update.hasCallbackQuery()) {

            if (query.getData() != null) {
                if (query.getData().equals("/Wallets")) {
                    try {
                        walletlist.GetAccounts(settings.getNodeUrl(), update);
                    } catch (Exception ex) {
                        System.out.println(ex);
                    }
                } else if (query.getData().equals("/start")) {
                    start.CmdStartMenu(update);
                } else if (query.getData().startsWith("/Wallet")) {
                    try {
                        wallet.Wallet(update);
                    } catch (Exception ex) {
                        System.out.println(ex);
                    }
                }

            }
        }
    }
}
