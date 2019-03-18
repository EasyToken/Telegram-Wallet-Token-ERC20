package info.bcdev.telegramwallet.bot;

import info.bcdev.telegramwallet.Main;
import info.bcdev.telegramwallet.Settings;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class Tbot extends TelegramLongPollingBot {

    UpdateReceived updateReceived = new UpdateReceived();

    Settings settings = Main.settings;

    public static Tbot INSTANCE;

    public Tbot(DefaultBotOptions botOptions) {
        super(botOptions);
    }

    @Override
    public String getBotUsername() {
        return settings.getBotUserName();
    }

    @Override
    public String getBotToken() {
        return settings.getBotToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        INSTANCE = this;
        updateReceived.Update(update);
    }

}
