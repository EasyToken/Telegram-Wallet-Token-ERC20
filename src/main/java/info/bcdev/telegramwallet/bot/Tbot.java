package info.bcdev.telegramwallet.bot;

import info.bcdev.telegramwallet.bot.session.Session;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import static info.bcdev.telegramwallet.bot.session.Session.SETTINGS;

public class Tbot extends TelegramLongPollingBot {

    private UpdateReceived updateReceived = new UpdateReceived();

    public Tbot(DefaultBotOptions botOptions) {
        super(botOptions);
    }

    @Override
    public String getBotUsername() {
        return SETTINGS.getBotUserName();
    }

    @Override
    public String getBotToken() {
        return SETTINGS.getBotToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        Session.BOT_INSTANCE = this;
        updateReceived.Update(update);
    }



}
