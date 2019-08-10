package info.bcdev.telegramwallet.ethereum.pages;

import info.bcdev.telegramwallet.bot.BotInstance;
import info.bcdev.telegramwallet.bot.session.Session;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class PageCreate implements BotInstance {

    EditMessageText editMessage = new EditMessageText();

    public void page(Update update){
        editMessage.setChatId(String.valueOf(update.getCallbackQuery().getMessage().getChatId()));
        editMessage.setMessageId(update.getCallbackQuery().getMessage().getMessageId());

        editMessage.setText("!!!!");

        try {
            tbot.execute(editMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}
