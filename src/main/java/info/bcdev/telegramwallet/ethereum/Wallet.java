package info.bcdev.telegramwallet.ethereum;

import com.vdurmont.emoji.EmojiParser;
import info.bcdev.lib.web3j.Balance;
import info.bcdev.telegramwallet.Main;
import info.bcdev.telegramwallet.Settings;
import info.bcdev.telegramwallet.bot.Keyboard;
import info.bcdev.telegramwallet.bot.Tbot;
import info.bcdev.telegramwallet.erc20.TokenERC20;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Wallet extends Keyboard {
    private SendMessage sendMessage = new SendMessage();
    private Settings settings = Main.settings;
    private Tbot tbot;

    public static Map<String,String> TRANSACTION_VALUE = new HashMap<>();

    public void loadWallet(Message message) throws IOException {
        tbot = Tbot.INSTANCE;
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());

        String walletaddress = Settings.ACTIVE_WALLET;

        Credentials credentials = getCredentials(walletaddress);

        Web3j web3 = Web3j.build(new HttpService(settings.getNodeUrl()));

        /*
        @Deprecated
        * */
        TokenERC20 token = TokenERC20.load(settings.getTokenAddress(), web3, credentials, getGasPrice(), getGasLimit());

        /*ContractGasProvider contractGasProvider = new DefaultGasProvider();
        TokenERC20 token = TokenERC20.load(settings.getTokenAddress(), web3, credentials, contractGasProvider);*/

        BigDecimal balance_eth = new Balance(web3).getEtherBalance(walletaddress);

        try {
           // String walletaddress = credentials.getAddress();
            String name = token.name().send();

            String symbol = token.symbol().send();

            String address = token.getContractAddress();

            BigInteger totalSupply = token.totalSupply().send();

            BigInteger balance = token.balanceOf(walletaddress).send();

            ///////////////////////////////////////////

            /*Map<String, String> keyboard = new HashMap<>();
            keyboard.put("Back","/Wallets");

            InlineKeyboardMarkup inlineKB = getInline(1,keyboard);*/


            //File file = QRCode.from(walletaddress).to(ImageType.PNG).withSize(250, 250).file();

            List<String> list = new ArrayList<>();
            String em;
            em = EmojiParser.parseToUnicode("\uD83D\uDCB3");
            list.add(em + " SendToken");

            em = EmojiParser.parseToUnicode("❌");
            list.add(em + " DeleteWallet");

            em = EmojiParser.parseToUnicode("\uD83D\uDC48");
            list.add(em+" Back");
            ReplyKeyboardMarkup replyKeyboardMarkup = getReply(2,list);

            String msg =""
                    + "\n Address: "+walletaddress+" "
                    + "\n Balance: "+balance_eth+" ETH"
                    + "\n Name Token: "+name+" "
                    + "\n Token Balance: "+balance+" "+symbol;

            /*editMessage.setText(msg);
            editMessage.setReplyMarkup(inlineKB);*/

            sendMessage.setText(msg);
            sendMessage.setReplyMarkup(replyKeyboardMarkup);

            tbot.execute(sendMessage);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private Credentials getCredentials(String walletaddress){
        for (WalletsInstance walletsInstance : Settings.WALLET_INSTANCE_LIST){
            if(walletsInstance.checkWallet(walletaddress)){
                return walletsInstance.getCredentials();
            }
        }
        return null;
    }

    public void EnterAddress(Message message) {

        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());

        sendMessage.setText("Enter wallet address");
        setSendStep("enterammount");
        try {
            tbot.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void EnterAmmount(Message message) {
        Tbot tbot = Tbot.INSTANCE;

        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());

        sendMessage.setText("Enter wallet ammount");
        setSendStep("confirm");
        try {
            tbot.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void SendToken(Message message) {

        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());

        List<String> list = new ArrayList<>();
        String em;

        em = EmojiParser.parseToUnicode("\uD83D\uDC48");
        list.add(em+" Back");
        ReplyKeyboardMarkup replyKeyboardMarkup = getReply(2,list);

        setSendStep("sending");

        String result = "";

        if (Sending()) {result = "successfully";} else {result = "not successful";}
        sendMessage.setText("Sending Token status: " + result);


        sendMessage.setReplyMarkup(replyKeyboardMarkup);

        try {
            tbot.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void confirmSendToken(Message message){
        tbot = Tbot.INSTANCE;
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());

        List<String> list = new ArrayList<>();
        String em;

        em = EmojiParser.parseToUnicode("✅");
        list.add(em + " Confirm Send");

        em = EmojiParser.parseToUnicode("\uD83D\uDC48");
        list.add(em+" Back");
        ReplyKeyboardMarkup replyKeyboardMarkup = getReply(2,list);

        String msg ="You confirm the sending?";

        sendMessage.setText(msg);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        setSendStep("sending");
        try{
            tbot.execute(sendMessage);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private boolean Sending() {

        Web3j web3 = Web3j.build(new HttpService(settings.getNodeUrl()));

        Credentials credentials = getCredentials(getActiveWallet());

        /*
        @Deprecated
        * */
        TokenERC20 token = TokenERC20.load(settings.getTokenAddress(), web3, credentials, getGasPrice(), getGasLimit());
        /*
        ContractGasProvider contractGasProvider = new DefaultGasProvider();
        TokenERC20 token = TokenERC20.load(settings.getTokenAddress(), web3, credentials, contractGasProvider);
        */

        String addressto = TRANSACTION_VALUE.get("addressto");
        int ammount = Integer.valueOf(TRANSACTION_VALUE.get("ammount"));

        TRANSACTION_VALUE.clear();
        try {
            String status = token.transfer(addressto, BigInteger.valueOf(ammount)).send().getStatus();

            if (status.equals("0x1")) return true;

        } catch (TelegramApiException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void confirmDelete(Message message) {

        setDelStep("confirm");

        tbot = Tbot.INSTANCE;
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());

        List<String> list = new ArrayList<>();
        String em;

        em = EmojiParser.parseToUnicode("✅");
        list.add(em + " Confirm Delete");

        em = EmojiParser.parseToUnicode("\uD83D\uDC48");
        list.add(em+" Back");
        ReplyKeyboardMarkup replyKeyboardMarkup = getReply(2,list);

        String msg ="you confirm the deletion of the purse❓❓❓ \n The confirmation will remove the purse forever❗❗❗";

        sendMessage.setText(msg);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
    try{
        tbot.execute(sendMessage);
    } catch (Exception ex) {
        ex.printStackTrace();
    }
    }

    public void Delete(Message message){

        tbot = Tbot.INSTANCE;
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());

        Boolean result = false;
        for (WalletsInstance walletsInstance : Settings.WALLET_INSTANCE_LIST) {
            if (walletsInstance.checkWallet(Settings.ACTIVE_WALLET)) {
                File wallet = walletsInstance.getFilewallet();
                result = wallet.delete();
            }
        }

        List<String> list = new ArrayList<>();
        String em;

        em = EmojiParser.parseToUnicode("\uD83D\uDC48");
        list.add(em+" Back");
        ReplyKeyboardMarkup replyKeyboardMarkup = getReply(2,list);

        String msg = "Your wallet "+Settings.ACTIVE_WALLET +" delete";

        if (!result) msg = "Error delete wallet";

        sendMessage.setText(msg);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        try{
            tbot.execute(sendMessage);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private BigInteger getGasPrice(){
        return Convert.toWei(Settings.GAS_PRICE_VALUE, Convert.Unit.GWEI).toBigInteger();
    }

    private BigInteger getGasLimit(){
        return BigInteger.valueOf(Long.valueOf(Settings.GAS_LIMIT_VALUE));
    }

    private String getActiveWallet(){
        return Settings.ACTIVE_WALLET;
    }

    private void setSendStep(String s){
        Settings.SEND_STEP = s;
    }

    private void setDelStep(String s){
        Settings.DELETE_STEP = s;
    }
}
