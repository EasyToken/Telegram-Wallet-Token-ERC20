package info.bcdev.telegramwallet;

import com.google.gson.JsonSyntaxException;
import info.bcdev.telegramwallet.bot.BotInstance;
import info.bcdev.telegramwallet.bot.Tbot;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;

import static info.bcdev.telegramwallet.bot.session.Session.BOT_INSTANCE;
import static info.bcdev.telegramwallet.bot.session.Session.SETTINGS;

public class Main implements BotInstance {

    private static Settings settings = new Settings();

    public static void main(String[] args) {

        try {
            if (settings.configRead("config.json")) {
                SETTINGS = settings;
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

        if (settings.getProxyAuthActive()) {
            // Create the Authenticator that will return auth's parameters for proxy authentication
            Authenticator.setDefault(new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(settings.getProxyUser(), settings.getProxyPass().toCharArray());
                }
            });
        }
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

        BOT_INSTANCE = new Tbot(botOptions);

        try {

            telegramBotsApi.registerBot(tbot);
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }

}
