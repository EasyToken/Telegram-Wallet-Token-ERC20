package info.bcdev.telegramwallet.ethereum;

import com.vdurmont.emoji.EmojiParser;
import info.bcdev.lib.web3j.Balance;
import info.bcdev.telegramwallet.bot.session.SendStep;
import info.bcdev.telegramwallet.bot.session.Session;
import info.bcdev.telegramwallet.Settings;
import info.bcdev.telegramwallet.bot.BotInstance;
import info.bcdev.telegramwallet.bot.KeyBoards;
import info.bcdev.telegramwallet.erc20.TokenERC20;
import info.bcdev.telegramwallet.ethereum.pages.PageTransactionList;
import info.bcdev.telegramwallet.bot.session.SendType;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.StaticGasProvider;
import org.web3j.utils.Convert;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

import static info.bcdev.telegramwallet.Settings.GAS_LIMIT_VALUE;
import static info.bcdev.telegramwallet.bot.session.SendStep.*;
import static info.bcdev.telegramwallet.bot.session.Session.*;

public class Wallet implements KeyBoards, BotInstance {
    private SendMessage sendMessage = new SendMessage();

    private Long chatID;

    public void setChatID(Long chatID) {
        this.chatID = chatID;
    }

    public void loadWallet() throws IOException {

        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatID);

        SendPhoto sendPhoto = new SendPhoto().setChatId(chatID);

        String walletaddress = Session.ACTIVE_WALLET;

        Credentials credentials = getCredentials(walletaddress);

        Web3j web3j = Web3j.build(new HttpService(SETTINGS.getNodeUrl()));

        BigDecimal balance_eth = new Balance(web3j).getEtherBalance(walletaddress);

        try {

            List<String> list = new ArrayList<>();
            String em;
            em = EmojiParser.parseToUnicode("\uD83D\uDCB3");
            list.add(em + " SendEther");
            if (SETTINGS.getTokenActive()) {
                list.add(em + " SendToken");
            }
            em = EmojiParser.parseToUnicode("\uD83D\uDDD2");
            list.add(em + " TransactionList");
            em = EmojiParser.parseToUnicode("❌");
            list.add(em + " DeleteWallet");

            em = EmojiParser.parseToUnicode("\uD83D\uDC48");
            list.add(em+" Back");
            ReplyKeyboardMarkup replyKeyboardMarkup = getReply(2,list);

            String caption = "\n Address: \n"+walletaddress;

            sendPhoto.setPhoto(getFileQRCode(walletaddress));
            sendPhoto.setCaption(caption);

            String msg = "\n ***Balance:*** \n"+balance_eth+" ETH";
            sendMessage.setText(loadToken(msg, walletaddress, web3j, credentials));
            sendMessage.setReplyMarkup(replyKeyboardMarkup);

            tbot.execute(sendPhoto);
            tbot.execute(sendMessage);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private Credentials getCredentials(String walletaddress){
        for (WalletsInstance walletsInstance : WALLET_INSTANCE_LIST){
            if(walletsInstance.checkWallet(walletaddress)){
                return walletsInstance.getCredentials();
            }
        }
        return null;
    }

    private String loadToken(String msg, String walletaddress, Web3j web3j, Credentials credentials){
        if (SETTINGS.getTokenActive()){

        ContractGasProvider contractGasProvider = new StaticGasProvider(getGasPrice(),getGasLimit());
        TokenERC20 token = TokenERC20.load(SETTINGS.getTokenAddress(), web3j, credentials, contractGasProvider);

            try {
                String name = token.name().send();
                String symbol = token.symbol().send();
                BigInteger balance = token.balanceOf(walletaddress).send();

            msg += "\n **Name Token:** "+name+" "
                    + "\n ***Token Balance:*** \n"+balance+" "+symbol;
            return msg;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return msg;
    }

    private File getFileQRCode(String walletaddress){
        for (WalletsInstance walletsInstance : WALLET_INSTANCE_LIST){
            if(walletsInstance.checkWallet(walletaddress)){
                return walletsInstance.getFileqrcode();
            }
        }
        return null;
    }

    public void getTransactionList(){
        PageTransactionList pageTransactionList = new PageTransactionList();
        pageTransactionList.getTransactionList(chatID);
    }

    public void enterAddress() {

        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatID);

        sendMessage.setText("Enter address to: ");
        setSendStep(ENTER_AMMOUNT);

        List<String> list = new ArrayList<>();
        String em = EmojiParser.parseToUnicode("\uD83D\uDED1");
        list.add(em+" StopSending");
        ReplyKeyboardMarkup replyKeyboardMarkup = getReply(1,list);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);

        try {
            tbot.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void enterAmmount() {

        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatID);

        sendMessage.setText("Address to: " + SEND_INSTANCE.getAddresTo()+
                            "\nEnter send ammount: ");
        setSendStep(CONFIRM);

        List<String> list = new ArrayList<>();
        String em = EmojiParser.parseToUnicode("\uD83D\uDED1");
        list.add(em+" StopSending");
        ReplyKeyboardMarkup replyKeyboardMarkup = getReply(1,list);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);

        try {
            tbot.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void confirmSend(Message message){
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());

        List<String> list = new ArrayList<>();
        String em;

        em = EmojiParser.parseToUnicode("✅");
        list.add(em + " Confirm Transactions");

        em = EmojiParser.parseToUnicode("\uD83D\uDED1");
        list.add(em+" StopSending");
        ReplyKeyboardMarkup replyKeyboardMarkup = getReply(2,list);

        String msg ="Address to: " + SEND_INSTANCE.getAddresTo()+
                "\nAmmount: " + SEND_INSTANCE.getAmount()+
                "\nFee Ethereum Network: "+ getFeeEtherNetwork() +" ETH "+
                "\nFee Transaction: "+ getFeeTransaction() +" ETH "+
                "\nYou confirm the sending?";

        sendMessage.setText(msg);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        setSendStep(SENDING);
        try{
            tbot.execute(sendMessage);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void selectAddress(Long chatID, String addressTo, String ammount){

/*        TRANSACTION_VALUE.put("addressto", addressTo);
        TRANSACTION_VALUE.put("ammount", ammount);*/

        SEND_INSTANCE.setAddresTo(addressTo);
        SEND_INSTANCE.setAmount(ammount);

        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatID);

        InlineKeyboardMarkup inlineKeyboardMarkup = getInline(1, getWalletList());

        sendMessage.setReplyMarkup(inlineKeyboardMarkup);

        String msg = "Address To: "+addressTo +
                "\nAmmount: "+ ammount +
                "\nSelect Address From";

        sendMessage.setText(msg);

        try {
            tbot.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void confirmSendSC(Long chatID){
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatID);

        List<String> list = new ArrayList<>();
        String em;

        em = EmojiParser.parseToUnicode("✅");
        list.add(em + " Confirm Transactions");

        em = EmojiParser.parseToUnicode("\uD83D\uDED1");
        list.add(em+" StopSending");
        ReplyKeyboardMarkup replyKeyboardMarkup = getReply(2,list);

        String msg ="Address to: " + SEND_INSTANCE.getAddresTo()+
                "\nAmmount: " + SEND_INSTANCE.getAmount()+
                "\nFee Ethereum Network: "+ getFeeEtherNetwork() +" ETH "+
                "\nFee Transaction: "+ getFeeTransaction() +" ETH "+
                "\nYou confirm the sending?";

        sendMessage.setText(msg);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        setSendStep(SENDING);
        try{
            tbot.execute(sendMessage);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void Send(Long chatID) {

        Web3j web3j = Web3j.build(new HttpService(SETTINGS.getNodeUrl()));
        Credentials credentials = getCredentials(getActiveWallet());

        SendCoin sendCoin = new SendCoin(web3j, credentials, Settings.GAS_PRICE_VALUE, Settings.GAS_LIMIT_VALUE);

        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatID);

        List<String> list = new ArrayList<>();

        String em = EmojiParser.parseToUnicode("\uD83D\uDC48");
        list.add(em+" Back");
        ReplyKeyboardMarkup replyKeyboardMarkup = getReply(2,list);

        setSendStep(SENDING);

        String msgResult = "";

        SendType sendType = getSendType();

        switch (sendType){
            case SENDETHERSC:
                msgResult = TransactionEth(sendCoin, msgResult);
                break;
            case SENDTOKEN:
                msgResult = TransactionToken(sendCoin, msgResult);
                break;
            case SENDETHER:
                msgResult = TransactionEth(sendCoin,msgResult);
                break;
        }

        sendMessage.setText("Transactions status: " + msgResult);

        sendMessage.setReplyMarkup(replyKeyboardMarkup);

        try {
            tbot.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        } finally {
            SEND_INSTANCE.Clear();
        }
    }

    private String getFeeEtherNetwork(){
        BigDecimal gp = Convert.toWei(Settings.GAS_PRICE_VALUE, Convert.Unit.GWEI);
        String fee_str = String.valueOf(gp.longValue() * Long.valueOf(GAS_LIMIT_VALUE));
        return Convert.fromWei(fee_str, Convert.Unit.ETHER).toString();
    }

    private String getFeeTransaction(){
        BigDecimal value = Convert.toWei(SEND_INSTANCE.getAmount(), Convert.Unit.ETHER);
        String fee_str = String.valueOf(value.longValue() / 1000 * 1);
        return Convert.fromWei(fee_str, Convert.Unit.ETHER).toString();
    }

    private String Transaction(SendCoin sendCoin, String msgResult){
        EthSendTransaction ethSendTransaction = sendCoin.sendingEther(
                SEND_INSTANCE.getAddresTo(),
                SEND_INSTANCE.getAmount());
        if (ethSendTransaction != null) {
            msgResult = "successfully\n" +
                    "Hash transaction: " +
                    "["+ethSendTransaction.getTransactionHash()+"](https://etherscan.io/tx/"+ethSendTransaction.getTransactionHash()+")";
        } else {
            msgResult = "Transaction Error";
        }
        return msgResult;
    }

    private String TransactionEth(SendCoin sendCoin, String msgResult){
        TransactionReceipt transactionReceipt = sendCoin.sendingEtherSC(
                SEND_INSTANCE.getAddresTo(),
                SEND_INSTANCE.getAmount());
        if (transactionReceipt != null ){
            msgResult = "successfully\n" +
                    "Hash transaction: " +
                    "["+transactionReceipt.getTransactionHash()+"](https://etherscan.io/tx/"+transactionReceipt.getTransactionHash()+")";
        } else {
            msgResult = "Transaction Error";
        }
        return msgResult;
    }

    private String TransactionToken(SendCoin sendCoin, String msgResult){
        if (sendCoin.sendingToken(
                SETTINGS.getTokenAddress(),
                SEND_INSTANCE.getAddresTo(),
                SEND_INSTANCE.getAmount()))
        {
            msgResult = "successfully";
        } else {
            msgResult = "not successful";
        }
        return msgResult;
    }

    public void confirmDelete(Message message) {

        setDelStep("confirm");

        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());

        List<String> list = new ArrayList<>();
        String em;

        em = EmojiParser.parseToUnicode("✅");
        list.add(em + " Confirm Delete");

        em = EmojiParser.parseToUnicode("\uD83D\uDC48");
        list.add(em+" Back");
        ReplyKeyboardMarkup replyKeyboardMarkup = getReply(2,list);

        String msg ="You confirm the deletion of the wallet❓❓❓ \n The confirmation will remove the purse forever❗❗❗";

        sendMessage.setText(msg);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
    try{
        tbot.execute(sendMessage);
    } catch (Exception ex) {
        ex.printStackTrace();
    }
    }

    public void Delete(Message message){

        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());

        Boolean result = false;
        for (WalletsInstance walletsInstance : WALLET_INSTANCE_LIST) {
            if (walletsInstance.checkWallet(Session.ACTIVE_WALLET)) {
                File wallet = walletsInstance.getFilewallet();
                result = wallet.delete();
                File qrcode = walletsInstance.getFileqrcode();
                qrcode.delete();
            }
        }

        List<String> list = new ArrayList<>();
        String em;

        em = EmojiParser.parseToUnicode("\uD83D\uDC48");
        list.add(em+" Back");
        ReplyKeyboardMarkup replyKeyboardMarkup = getReply(2,list);

        String msg = "Your wallet "+ Session.ACTIVE_WALLET +" delete";

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

    private Map<String,String> getWalletList(){
        Map<String,String> addressList = new HashMap<>();
        for (WalletsInstance walletsInstance : WALLET_INSTANCE_LIST){
            String address = walletsInstance.getWalletaddress();
            addressList.put(address, address);
        }
        return addressList;
    }

    private BigInteger getGasLimit(){
        return BigInteger.valueOf(Long.valueOf(Settings.GAS_LIMIT_VALUE));
    }

    private String getActiveWallet(){
        return Session.ACTIVE_WALLET;
    }

    private SendType getSendType(){
        return Session.SEND_TYPE;
    }

    private void setSendStep(SendStep ss){
        Session.SEND_STEP = ss;
    }

    private void setDelStep(String s){
        Session.DELETE_STEP = s;
    }
}
