package info.bcdev.telegramwallet;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import info.bcdev.telegramwallet.bot.BotAccess;
import info.bcdev.telegramwallet.bot.session.Session;
import info.bcdev.telegramwallet.ethereum.WalletsInstance;
import org.glassfish.jersey.jaxb.internal.SecureSaxParserFactory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Settings {

    private String botUserName;
    private String botToken;

    private Boolean proxyActive;
    private String proxyAddress;
    private int proxyPort;
    private String proxyType;
    private String proxyUser;
    private String proxyPass;
    private Boolean proxyAuthActive;

    private String walletDir;
    private String walletPassword;

    private String nodeUrl;

    private Boolean tokenactive;
    private String tokenAddress;

    private String qrCodeDir;

    private String apiTokenEtherScan;

    public static String GAS_PRICE_VALUE = "4";
    public static String GAS_LIMIT_VALUE = "85000";

    public Boolean configRead(String configName) throws IOException {

        File configFile = new File(configName);

        if (configFile.isFile() && configFile.canRead()){

            FileReader fileReader = new FileReader(configFile);

            JsonParser jsonParser = new JsonParser();
            JsonObject config = jsonParser.parse(fileReader).getAsJsonObject();
            JsonObject settings = config.get("settings").getAsJsonObject();

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
            JsonObject etherscan = settings.getAsJsonObject("etherscan");
            JsonArray usersID = bot.getAsJsonArray("usersid");

            botUserName = bot.get("username").getAsString();
            botToken = bot.get("token").getAsString();

            proxyActive = proxy.get("active").getAsBoolean();
            proxyAddress = proxy.get("address").getAsString();
            proxyPort = proxy.get("port").getAsInt();
            proxyType = proxy.get("type").getAsString();
            JsonObject proxyAuth = proxy.getAsJsonObject("proxyauth");
            proxyAuthActive = proxyAuth.get("active").getAsBoolean();
            proxyUser = proxyAuth.get("proxyuser").getAsString();
            proxyPass = proxyAuth.get("proxypas").getAsString();

            walletDir = wallet.get("dir").getAsString();

            qrCodeDir = settings.get("qrcodedir").getAsString();

            walletPassword = wallet.get("password").getAsString();

            nodeUrl = node.get("url").getAsString();

            JsonObject smartcontracterc20 = node.getAsJsonObject("smartcontracterc20");
            tokenactive = smartcontracterc20.get("active").getAsBoolean();
            tokenAddress = smartcontracterc20.get("tokenaddress").getAsString();

            apiTokenEtherScan = etherscan.get("apikeytoken").getAsString();

            Session.BOT_ACCESS = new BotAccess(usersID);
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

    public String getProxyUser() {
        return proxyUser;
    }

    public String getProxyPass() {
        return proxyPass;
    }

    public Boolean getProxyAuthActive() {
        return proxyAuthActive;
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
        return qrCodeDir;
    }

    public String getWalletPassword() {
        return walletPassword;
    }

    public String getNodeUrl() {
        return nodeUrl;
    }

    public Boolean getTokenActive() {
        return tokenactive;
    }

    public String getTokenAddress(){
        return tokenAddress;
    }

    public String getApiTokenEtherScan() {
        return apiTokenEtherScan;
    }
}
