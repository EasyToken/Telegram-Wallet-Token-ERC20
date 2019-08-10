package info.bcdev.telegramwallet.bot;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;

public class BotAccess {

    private List<Long> usersIDList = new ArrayList<>();

    public BotAccess(JsonArray usersID) {
        for (JsonElement jsonElement: usersID){
            if (jsonElement != null) {
                usersIDList.add(jsonElement.getAsLong());
            }
        }
    }

    public Boolean checkAccess(Long chatID){
        for(Long userID: usersIDList){
            if (chatID.longValue() == userID.longValue()){
                return true;
            }
        }
        return false;
    }

}
