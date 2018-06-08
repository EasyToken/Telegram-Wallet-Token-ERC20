package ru.easytoken.telegramwallet;


import java.util.ArrayList;
import java.util.List;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class Start {

    SendMessage sendMessage = new SendMessage();
    TelegramWallet tw = new TelegramWallet();

    public void CmdStart (Message message)  {
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());

        System.out.println(message.getChat());

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> RowInline1 = new ArrayList<>();
        List<InlineKeyboardButton> RowInline2 = new ArrayList<>();

        RowInline1.add(new InlineKeyboardButton().setText("Wallets").setCallbackData("/Wallets"));

        keyboard.add(RowInline1);

        markup.setKeyboard(keyboard);

        sendMessage.setReplyMarkup(markup);
        sendMessage.setText("Wallet for Ethereum Tokens");
        try {
            tw.sendMessage(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void CmdStartMenu (Update update)  {
        EditMessageText editMessage = new EditMessageText();
        editMessage.setChatId(String.valueOf(update.getCallbackQuery().getMessage().getChatId()));
        editMessage.setMessageId(update.getCallbackQuery().getMessage().getMessageId());

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> RowInline1 = new ArrayList<>();
        List<InlineKeyboardButton> RowInline2 = new ArrayList<>();

        RowInline1.add(new InlineKeyboardButton().setText("Wallets").setCallbackData("/Wallets"));

        keyboard.add(RowInline1);
        keyboard.add(RowInline2);

        String msg ="Wallet for Ethereum Tokens";

        markup.setKeyboard(keyboard);

        editMessage.setText(msg);
        editMessage.setReplyMarkup(markup);

        try {
            tw.editMessageText(editMessage);
        } catch (TelegramApiException ex) {}
    }

}
