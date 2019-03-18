package info.bcdev.telegramwallet.ethereum;

import info.bcdev.telegramwallet.bot.Keyboard;
import info.bcdev.telegramwallet.bot.Tbot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;
import java.util.Map;

public class ListEW extends Keyboard {

    SendMessage sendMessage = new SendMessage();
    WalletList walletList = new WalletList();

    public void list(Message message){
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());

        Map<String,String> map = new HashMap<>();
        map.put("add new","/addnew_ew");

        sendMessage.setReplyMarkup(getInline(1,map));
        sendMessage.setText("Ethereum wallets:");

        try {
            Tbot.INSTANCE.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }


}
