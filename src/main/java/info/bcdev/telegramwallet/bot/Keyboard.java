package info.bcdev.telegramwallet.bot;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Keyboard {

    public InlineKeyboardMarkup getInline(Integer rows, Map<String, String> m) {

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> RowInline = new ArrayList<>();
        int count = 0;
        for (Map.Entry<String, String> entry : m.entrySet()) {
            if (count < rows) {
                RowInline.add(new InlineKeyboardButton().setText(entry.getKey()).setCallbackData(entry.getValue()));
                count++;
            } else {
                keyboard.add(RowInline);
                RowInline = new ArrayList<>();
                RowInline.add(new InlineKeyboardButton().setText(entry.getKey()).setCallbackData(entry.getValue()));
                count = 1;
            }
        }
        keyboard.add(RowInline);
        markup.setKeyboard(keyboard);
        return markup;
    }

    public ReplyKeyboardMarkup getReply(Integer rows, List<String> l) {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        markup.setResizeKeyboard(Boolean.TRUE);

        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        int count = 0;
        for(int i=0;i< l.size();i++){
            if (count < rows){
                row.add(l.get(i));
                count++;
            }else{
                keyboardRows.add(row);
                row = new KeyboardRow();
                row.add(l.get(i));
                count = 1;
            }
        }
        keyboardRows.add(row);
        markup.setKeyboard(keyboardRows);
        return markup;
    }

}
