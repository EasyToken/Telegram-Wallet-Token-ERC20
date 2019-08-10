package info.bcdev.telegramwallet.ethereum.pages;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.vdurmont.emoji.EmojiParser;
import info.bcdev.telegramwallet.Settings;
import info.bcdev.telegramwallet.bot.BotInstance;
import info.bcdev.telegramwallet.bot.KeyBoards;
import info.bcdev.telegramwallet.bot.session.Session;
import info.bcdev.telegramwallet.etherscan.ESTransactions;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static info.bcdev.telegramwallet.bot.session.Session.SETTINGS;

public class PageTransactionList implements KeyBoards, BotInstance {

    private SendMessage sendMessage = new SendMessage();
    private ESTransactions esTransactions = new ESTransactions();


    public void getTransactionList(Long chatID){
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatID);

        List<String> list = new ArrayList<>();
        String em;

        em = EmojiParser.parseToUnicode("\uD83D\uDC48");
        list.add(em+" Back");
        ReplyKeyboardMarkup replyKeyboardMarkup = getReply(2,list);

        try {
            JsonArray jsonArray = esTransactions.getTransactions(Session.ACTIVE_WALLET, SETTINGS.getApiTokenEtherScan());

            if (jsonArray == null){
                sendMessage.setText("Transactions not found");
            } else {
                Iterator<JsonElement> iterator = jsonArray.iterator();
                int i = 1;
                while (iterator.hasNext()) {
                    if (i <= 10) {
                        String result = "";
                        JsonObject jsonObject = iterator.next().getAsJsonObject();

                        String from = jsonObject.get("from").getAsString();

                        if (from.equals(Session.ACTIVE_WALLET.toLowerCase())) {
                            result += "\n OUT \uD83D\uDD3A";
                        } else {
                            result += "\n IN \uD83D\uDD39";
                        }

                        result += "\n [Click to see more details](https://etherscan.io/tx/"+jsonObject.get("hash").getAsString()+")";
                        if (from.equals(Session.ACTIVE_WALLET.toLowerCase())) {
                            result += "\n To: ***"+jsonObject.get("to").getAsString()+"***";
                        } else {
                            result += "\n From: ***"+from+"***";
                        }
                        result += "\n Ammount: ***"+converWeiToEth(jsonObject.get("value").getAsString()) + "*** eth";

                        sendMessage.setText(result);
                        tbot.execute(sendMessage);
                        i++;
                    }
                }

                sendMessage.setText("[See more Transactions](https://etherscan.io/address/"+Session.ACTIVE_WALLET+")");

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        sendMessage.setReplyMarkup(replyKeyboardMarkup);

        try {
            tbot.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private String converWeiToEth(String value){
        return Convert.fromWei(value, Convert.Unit.ETHER).toString();
    }

}
