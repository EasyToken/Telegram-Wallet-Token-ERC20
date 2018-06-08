package ru.easytoken.telegramwallet;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Config {


    private JSONObject jsonObject;

    private void ReadConfig() {

        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader("config.json"));
            jsonObject = (JSONObject) obj;
        } catch (FileNotFoundException e) {System.out.println(e);
        } catch (ParseException e) {System.out.println(e);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getBotUsername() {
        ReadConfig();
        return jsonObject.get("BotUsername").toString();
    }

    public String getBotToken() {
        ReadConfig();
        return jsonObject.get("BotToken").toString();
    }

    public String getUrl() {
        ReadConfig();
        return jsonObject.get("Url").toString();
    }

    public String getTokenAddress() {
        ReadConfig();
        return jsonObject.get("TokenAddress").toString();
    }

    public String getDirWalletKey() {
        ReadConfig();
        return jsonObject.get("DirWalletKey").toString();
    }

    public String getPassword(){
        ReadConfig();
        return jsonObject.get("PasswordWallet").toString();
    }
}
