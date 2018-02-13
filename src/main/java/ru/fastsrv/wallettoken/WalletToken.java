package ru.fastsrv.wallettoken;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.CallbackQuery;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

public class WalletToken  extends TelegramLongPollingBot {

        Config config = new Config();
        Wallets wallets = new Wallets();
        Wallet wallet = new Wallet();
            SendMessage sendMessage = new SendMessage();
                                
        public static void main(String[] args) throws IOException, InterruptedException, ExecutionException, TelegramApiRequestException {
        ApiContextInitializer.init();
		TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
		try {
			telegramBotsApi.registerBot(new WalletToken());
		} catch (TelegramApiException e) {System.out.println(e);}  
    }
 
    @Override
	public String getBotUsername() {
		return config.getBotUsername();
	}
 
    @Override
	public String getBotToken() {
		return config.getBotToken();
	}
 
    @Override
	public void onUpdateReceived(Update update) {
                            Start start = new Start();
            Message message = update.getMessage();
            List<KeyboardRow> keyboard = null;
            CallbackQuery query = update.getCallbackQuery();
            
		if (message != null && message.hasText()) 
                {System.out.println(message.getText());
                	if (message.getText().equals("/start")) {start.CmdStart(message);}
                        else if (message.getText().startsWith("/Send")) {try {wallet.SendToken(message, message.getText());} catch (Exception ex) {System.out.println(ex);}}
		}
                else if (update.hasCallbackQuery()) {
		    
                if (query.getData() != null)
                {
                    if (query.getData().equals("/Wallets")) {try {wallets.GetAccounts(config.getUrl(), update);} catch (Exception ex) {System.out.println(ex);}}
                    else if (query.getData().equals("/start")) {start.CmdStartMenu(update);}
                    else if (query.getData().startsWith("/Wallet")) {try {wallet.Wallet(update);} catch (Exception ex) {System.out.println(ex);}}
                    
                } 
            }               
    }
        
}
