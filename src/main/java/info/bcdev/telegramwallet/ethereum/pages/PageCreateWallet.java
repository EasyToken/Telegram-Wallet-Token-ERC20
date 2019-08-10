package info.bcdev.telegramwallet.ethereum.pages;

import info.bcdev.telegramwallet.bot.BotInstance;
import info.bcdev.telegramwallet.bot.KeyBoards;
import info.bcdev.telegramwallet.bot.Keyboard;
import info.bcdev.telegramwallet.bot.Tbot;
import info.bcdev.telegramwallet.ethereum.CreateEW;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class PageCreate implements KeyBoards, BotInstance {

    EditMessageText editMessage = new EditMessageText();

    CreateEW createEW = new CreateEW();

    //SQLActions sqlActions = new SQLActions();

    public void page(Update update){
        editMessage.setChatId(String.valueOf(update.getCallbackQuery().getMessage().getChatId()));
        editMessage.setMessageId(update.getCallbackQuery().getMessage().getMessageId());

        createEW.CreateEW();

        editMessage.setText("Введите пароль для кошелька!!!");

        try {
            tbot.execute(editMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

}
