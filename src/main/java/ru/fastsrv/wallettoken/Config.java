package ru.fastsrv.wallettoken;

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
        } catch (IOException | ParseException e) {System.out.println(e);
        }

    }
    
    public String getBotUsername() {
        ReadConfig();
        String BotUsername = (String) jsonObject.get("BotUsername");
        return BotUsername;   
    }
    
        public String getBotToken() {
        ReadConfig();
        String BotToken = (String) jsonObject.get("BotToken");
        return BotToken;   
    }
        
        public String getUrl() {
        ReadConfig();
        String Url = (String) jsonObject.get("Url");
        return Url;   
    }  
        
        public Long getGasPrice() {
        ReadConfig();
        Integer GasPriceInt = (Integer) jsonObject.get("GasPrice");
        Long GasPrice = Long.valueOf(GasPriceInt);
        return GasPrice;   
    }
        
        public Long getGasLimit() {
        ReadConfig();
        Integer GasLimitInt = (Integer) jsonObject.get("GasLimit");
        Long GasLimit = Long.valueOf(GasLimitInt);
        return GasLimit;   
    }
        
        public String getTokenAddress() {
        ReadConfig();
        String TokenAddress = (String) jsonObject.get("TokenAddress");
        return TokenAddress;   
    }
}
    
