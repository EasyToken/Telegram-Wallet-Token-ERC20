package info.bcdev.telegramwallet;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import info.bcdev.telegramwallet.ethereum.WalletsInstance;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Config {

    private String botUserName;
    private String botToken;

    private Boolean proxyActive;
    private String proxyAddress;
    private int proxyPort;
    private String proxyType;

    private String walletDir;
    private String walletPassword;

    private String nodeUrl;
    private String tokenAddress;

    private String qrcodeDir;

    public static String SESSION_PAGE;
    public static String ACTIVE_WALLET;
    public static String SEND_STEP;
    public static String DELETE_STEP;

    public static String GAS_PRICE_VALUE = "3";
    public static String GAS_LIMIT_VALUE = "45000";

    public static List<WalletsInstance> WALLET_INSTANCE_LIST = new ArrayList<>();

    public Boolean configRead(String configName) throws IOException {

        File configFile = new File(configName);

        if (configFile.isFile() && configFile.canRead()){

            FileReader fileReader = new FileReader(configFile);

            JsonParser jsonParser = new JsonParser();
            JsonObject config = jsonParser.parse(fileReader).getAsJsonObject();
            JsonObject settings = config.get("configuration").getAsJsonObject();

            setParamsSettings(settings);

            fileReader.close();
            return true;
        }
            return false;
    }

    private void setParamsSettings(JsonObject settings) {

        if (!settings.isJsonNull() && settings.isJsonObject()){
            JsonObject bot = settings.getAsJsonObject("bot");
            JsonObject proxy = settings.getAsJsonObject("proxy");
            JsonObject wallet = settings.getAsJsonObject("wallet");
            JsonObject node = settings.getAsJsonObject("node");

            botUserName = bot.get("username").getAsString();
            botToken = bot.get("token").getAsString();

            proxyActive = proxy.get("active").getAsBoolean();
            proxyAddress = proxy.get("address").getAsString();
            proxyPort = proxy.get("port").getAsInt();
            proxyType = proxy.get("type").getAsString();

            walletDir = wallet.get("dir").getAsString();

            qrcodeDir = settings.get("qrcodedir").getAsString();

            walletPassword = wallet.get("password").getAsString();

            nodeUrl = node.get("url").getAsString();
            tokenAddress = node.get("tokenaddress").getAsString();
        }

    }

    public Boolean getProxyActive() {
        return proxyActive;
    }

    public String getProxyAddress() {
        return proxyAddress;
    }

    public int getProxyPort() {
        return proxyPort;
    }

    public String getProxyType() {
        return proxyType;
    }

    public String getBotUserName() {
        return botUserName;
    }

    public String getBotToken() {
        return botToken;
    }

    public String getWalletDir() {
        return walletDir;
    }

    public String getQRCodeDir() {
        return qrcodeDir;
    }

    public String getWalletPassword() {
        return walletPassword;
    }

    public String getNodeUrl() {
        return nodeUrl;
    }

    public String getTokenAddress(){
        return tokenAddress;
    }
}
