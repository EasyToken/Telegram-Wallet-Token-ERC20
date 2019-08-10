package info.bcdev.telegramwallet.bot;

import info.bcdev.telegramwallet.bot.session.SendStep;
import info.bcdev.telegramwallet.bot.session.Session;
import info.bcdev.telegramwallet.bot.session.SessionPage;
import info.bcdev.telegramwallet.ethereum.pages.*;
import info.bcdev.telegramwallet.ethereum.Wallet;
import info.bcdev.telegramwallet.ethereum.WalletList;
import info.bcdev.telegramwallet.bot.session.SendType;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;

import static info.bcdev.telegramwallet.bot.session.Session.*;
import static info.bcdev.telegramwallet.bot.session.SessionPage.*;

public class UpdateReceived {

    private PageStart pageStart = new PageStart();
    private Wallet wallet = new Wallet();
    private WalletList walletlist = new WalletList();
    private PageGas pageGas = new PageGas();
    private PageSettings pageSettings = new PageSettings();
    private PageCreateWallet pageCreateWallet = new PageCreateWallet();
    private Long chatID;

    public void Update(Update update) {

        Message message = update.getMessage();

        if (message != null && message.hasText()) {

            chatID = message.getChatId();

            String messageText = message.getText();

            if (BOT_ACCESS.checkAccess(chatID)) {

                if (Session.SESSION_PAGE == null) loadWalletList();

                if (messageText.equals("/PageStart")) {
                    loadStartPage();
                } else if (messageText.equals("\uD83D\uDCBC Wallets")) {
                    loadWalletList();
                } else if (messageText.equals("⚙ PageSettings")) {
                    editSettings(message);
                } else if (messageText.matches("\uD83D\uDCB0 0x[a-z0-9]+")) {
                    String walletaddress = messageText
                            .replace("\uD83D\uDCB0 ","")
                            .replaceAll(" ","");
                    setActiveWallet(walletaddress);
                    loadWallet();
                } else if (messageText.equals("\uD83D\uDDD2 TransactionList")) {
                    setSessionPage(TRANSACTIONS_LIST);
                    wallet.getTransactionList();
                } else if (messageText.equals("\uD83D\uDCB3 SendToken")) {
                    setSendType(SendType.SENDTOKEN);
                    wallet.enterAddress();
                } else if (messageText.equals("\uD83D\uDCB3 SendEther")) {
                    setSendType(SendType.SENDETHER);
                    wallet.enterAddress();
                } else if (messageText.matches("/(send|thanks|tips) 0x[a-z0-9]+ [0-9|/.]+")) {
                    setSendType(SendType.SENDETHERSC);
                    String[] messageList = messageText.split(" ");
                    wallet.selectAddress(chatID, messageList[1], messageList[2]);
                } else if (messageText.equals("✅ Confirm Transactions")) {
                    setSessionPage(WALLET);
                    wallet.Send(chatID);
                } else if (messageText.equals("\uD83D\uDED1 StopSending")) {
                    loadWallet();
                } else if (messageText.equals("\uD83D\uDCCB Create Wallet")) {
                    setSessionPage(CONFIRM_CREATE);
                    pageCreateWallet.confirmCreate(message);
                } else if (messageText.equals("✅ Confirm Create")) {
                    setSessionPage(CREATE);
                    pageCreateWallet.Create(message);
                } else if (messageText.equals("\uD83D\uDD17 RecoveryWallet")) {
                    setSessionPage(RECOVERY_WALLET);
                    new PageRecovery().enterSeed(message);
                } else if (messageText.equals("\uD83D\uDECE About")) {
                    setSessionPage(ABOUT);
                    new PageAbout().openAbout(message);
                } else if (messageText.equals("\uD83D\uDCB8 Thanks") || messageText.equals("/thanks")) {
                    setSessionPage(TNX);
                    new PageTnx().openTnx(message);
                } else if (messageText.equals("\uD83D\uDC48 Back")) {
                    if (getSessionPage() != null) {
                        switch (getSessionPage()) {
                            case WALLETS:
                                loadStartPage();
                                break;
                            case WALLET:
                                loadWalletList();
                                break;
                            case CONFIRM_SEND:
                                loadWallet();
                                break;
                            case SENDING:
                                loadWallet();
                                break;
                            case DELETE:
                                loadWallet();
                                break;
                            case CONFIRM_DELETE:
                                loadWalletList();
                                break;
                            case EDIT_GAS:
                                editSettings(message);
                                break;
                            case EDIT_GAS_PRICE:
                                setSessionPage(EDIT_GAS);
                                pageGas.gasEdit(message);
                                break;
                            case EDIT_GAS_LIMIT:
                                setSessionPage(EDIT_GAS);
                                pageGas.gasEdit(message);
                                break;
                            case PAGE_SETTINGS:
                                loadWalletList();
                                break;
                            case TRANSACTIONS_LIST:
                                loadWallet();
                                break;
                            case RECOVERY_WALLET:
                                loadWalletList();
                                break;
                            case ABOUT:
                                loadStartPage();
                                break;
                            case TNX:
                                loadStartPage();
                                break;
                            case CREATE:
                                loadWalletList();
                                break;
                            case CONFIRM_CREATE:
                                loadWalletList();
                                break;
                            default:
                                loadStartPage();
                                break;
                        }
                    } else {
                        loadStartPage();
                    }
                } else if (messageText.equals("❌ DeleteWallet")) {
                    setSessionPage(DELETE);
                    wallet.confirmDelete(message);
                } else if (messageText.equals("✅ Confirm Delete")) {
                    setSessionPage(CONFIRM_DELETE);
                    switch (getDelStep()) {
                        case "confirm":
                            wallet.Delete(message);
                            break;
                    }
                } else if (messageText.equals("\uD83D\uDCDD Edit PageGas")) {
                    setSessionPage(EDIT_GAS);
                    pageGas.gasEdit(message);
                } else if (messageText.equals("GasPrice Value")) {
                    setSessionPage(EDIT_GAS_PRICE);
                    pageGas.gasPriceEdit(message);
                } else if (messageText.equals("GasLimit Value")) {
                    setSessionPage(EDIT_GAS_LIMIT);
                    pageGas.gasLimitEdit(message);
                } else {
                    switch (getSessionPage()) {
                        case WALLET:
                            if (getSendStep() != null || !getSendStep().equals("")) {
                                switch (getSendStep()) {
                                    case ENTER_AMMOUNT:
                                        SEND_INSTANCE.setAddresTo(messageText);
                                        wallet.enterAmmount();
                                        break;
                                    case CONFIRM:
                                        setSessionPage(CONFIRM_SEND);
                                        SEND_INSTANCE.setAmount(messageText);
                                        wallet.confirmSend(message);
                                        break;
                                }
                            }
                            break;
                        case EDIT_GAS_PRICE:
                            pageGas.setGasPrice(messageText);
                            pageGas.gasEdit(message);
                            break;
                        case EDIT_GAS_LIMIT:
                            pageGas.setGasLimit(messageText);
                            pageGas.gasEdit(message);
                            break;
                        case RECOVERY_WALLET:
                            new PageRecovery().recoveryWallet(chatID, messageText);
                            break;
                    }
                }
            }
        } else if (update.hasCallbackQuery()) {

            CallbackQuery callbackQuery = update.getCallbackQuery();

            chatID = callbackQuery.getMessage().getChatId();
            wallet.setChatID(chatID);

            if (callbackQuery.getData() != null)
            {
                String messageText = callbackQuery.getData();
                if (messageText.matches("0x[a-z0-9]+")) {
                    setActiveWallet(messageText);
                    wallet.confirmSendSC(chatID);
                }
            }
        } else {
            System.out.println("Error");
        }
    }

    private void loadStartPage(){
        setSessionPage(START_PAGE);
        pageStart.setChatID(chatID);
        pageStart.CmdStart();
    }

    private void loadWalletList(){
        setSessionPage(WALLETS);
        setActiveWallet("");
        walletlist.setChatID(chatID);
        walletlist.loadWalletList();
    }

    private void loadWallet(){
        setSessionPage(WALLET);
        wallet.setChatID(chatID);
        try {
            wallet.loadWallet();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void editSettings(Message message){
        setSessionPage(PAGE_SETTINGS);
        pageSettings.editSettings(message);
    }

    private SessionPage getSessionPage(){
        return Session.SESSION_PAGE;
    }

    private void setSessionPage(SessionPage sp){
        Session.SESSION_PAGE = sp;
    }

    private void setActiveWallet(String w){
        Session.ACTIVE_WALLET = w;
    }

    private void setSendType(SendType t){
        Session.SEND_TYPE = t;
    }

    private SendStep getSendStep(){
        return Session.SEND_STEP;
    }

    private String getDelStep(){
        return Session.DELETE_STEP;
    }

}
