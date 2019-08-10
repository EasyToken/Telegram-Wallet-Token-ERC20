package info.bcdev.telegramwallet.etherscan;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ESTransactions {

    public JsonArray getTransactions(String address, String apiToken) throws IOException {
        String transactionaddress = "https://api.etherscan.io/api?module=account&action=txlist&address="+address+"&startblock=0&endblock=99999999&sort=desc&apikey="+apiToken;

            URL urlapietherscan = new URL(transactionaddress);
            HttpURLConnection apietherscan = (HttpURLConnection) urlapietherscan.openConnection();
            apietherscan.setDoOutput(true);
            apietherscan.setDoInput(true);
            apietherscan.setRequestMethod("GET");
            apietherscan.setRequestProperty("Content-Type", "application/json");
            InputStreamReader inputStreamReader = new InputStreamReader(apietherscan.getInputStream());
            JsonParser jsonParser = new JsonParser();
            Object objapietherscan = jsonParser.parse(inputStreamReader);
            JsonObject jsonObject = (JsonObject) objapietherscan;
            if (jsonObject.get("status").getAsInt() != 0) {
                JsonArray jsonArray = (JsonArray) jsonObject.get("result");

                inputStreamReader.close();

                return jsonArray;
            }
            return null;
    }

}
