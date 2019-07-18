package info.bcdev.telegramwallet;

import com.google.gson.JsonSyntaxException;
import info.bcdev.telegramwallet.bot.Tbot;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    public static Settings settings = new Settings();

    public static void main(String[] args) {

        try {
            if (settings.configRead("config.json")) {

                botInstance();
            }
        } catch (JsonSyntaxException e){
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void botInstance() {

        ApiContextInitializer.init();

        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();

        // Set up Http proxy
        DefaultBotOptions botOptions = ApiContext.getInstance(DefaultBotOptions.class);

        if (settings.getProxyActive()) {
            botOptions.setProxyHost(settings.getProxyAddress());
            botOptions.setProxyPort(settings.getProxyPort());
            if (settings.getProxyType().equals("socks5")) {
                botOptions.setProxyType(DefaultBotOptions.ProxyType.SOCKS5);
            } else if(settings.getProxyType().equals("http")){
                botOptions.setProxyType(DefaultBotOptions.ProxyType.HTTP);
            }
        }

        Tbot tbot = new Tbot(botOptions);

        try {

            telegramBotsApi.registerBot(tbot);
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }

/*    private void validDirWallet() throws IOException {
        Path dirPath = Paths.get(walletDir);
        if (!Files.isReadable(dirPath)){
            Files.createDirectories(dirPath);
        }
    }*/

}
