package info.bcdev.telegramwallet.bot;

import info.bcdev.telegramwallet.Settings;
import info.bcdev.telegramwallet.ethereum.Gas;
import info.bcdev.telegramwallet.ethereum.Wallet;
import info.bcdev.telegramwallet.ethereum.WalletList;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.web3j.crypto.CipherException;

import java.io.IOException;

public class UpdateReceived {

    private Wallet wallet = new Wallet();
    private WalletList walletlist = new WalletList();
    private Gas gas = new Gas();

    public void Update(Update update) {
        start start = new start();
        Message message = update.getMessage();

        if (message != null && message.hasText()) {

            if (Settings.SESSION_PAGE == null) loadWalletList(message);

            if (message.getText().equals("/start")) {
                setSessionPage("start");
                start.CmdStart(message);
            } else if (message.getText().equals("\uD83D\uDCBC Wallets")) {
                loadWalletList(message);
            } else if (message.getText().matches("\uD83D\uDCB0 0x[a-z0-9]+")) {
                String walletaddress = message.getText().split(" ")[1];
                setActiveWallet(walletaddress);
                loadWallet(message);
            } else if (message.getText().equals("\uD83D\uDCB3 SendToken")){
                wallet.EnterAddress(message);
            } else if (message.getText().equals("✅ Confirm Send")){
                wallet.SendToken(message);
            } else if (message.getText().equals("\uD83D\uDCCB Create Wallet")) {
                createWallet(message);
            } else if (message.getText().equals("\uD83D\uDC48 Back")){
                if (getSessionPage() != null) {
                    switch (getSessionPage()) {
                        case "wallets":
                            start.CmdStart(message);
                            break;
                        case "wallet":
                            loadWalletList(message);
                            break;
                        case "confirmsend":
                            loadWallet(message);
                            break;
                        case "sending":
                            loadWallet(message);
                            break;
                        case "delete":
                            loadWallet(message);
                            break;
                        case "confirmdelete":
                            loadWalletList(message);
                            break;
                        case "editgas":
                            loadWalletList(message);
                            break;
                        default:
                            start.CmdStart(message);
                            break;
                    }
                } else {
                    start.CmdStart(message);
                }
            } else if (message.getText().equals("❌ DeleteWallet")) {
                setSessionPage("delete");
                wallet.confirmDelete(message);
            } else if (message.getText().equals("✅ Confirm Delete")) {
                setSessionPage("confirmdelete");
                switch (getDelStep()){
                    case "confirm":
                        wallet.Delete(message);
                        break;
                }
            } else if (message.getText().equals("Edit Gas")) {
                setSessionPage("editgas");
                gas.gasEdit(message);
            } else if (message.getText().equals("GasPrice Value")) {
                setSessionPage("editgasprice");
                gas.gasPriceEdit(message);
            } else if (message.getText().equals("GasLimit Value")) {
                setSessionPage("editgaslimit");
                gas.gasLimitEdit(message);
            } else {
                switch (getSessionPage()) {
                    case "wallet":
                    if (getSendStep() != null || !getSendStep().equals("")) {
                        switch (getSendStep()) {
                            case "enterammount":
                                Wallet.TRANSACTION_VALUE.put("addressto", message.getText());
                                wallet.EnterAmmount(message);
                                break;
                            case "confirm":
                                setSessionPage("confirmsend");
                                Wallet.TRANSACTION_VALUE.put("ammount", message.getText());
                                wallet.confirmSendToken(message);
                                break;
                        }
                    }
                    break;
                    case "editgasprice":
                        gas.setGasPrice(message.getText());
                        gas.gasEdit(message);
                        break;
                    case "editgaslimit":
                        gas.setGasLimit(message.getText());
                        gas.gasEdit(message);
                        break;
                }
            }
        }
    }

    private void loadWalletList(Message message){
        setSessionPage("wallets");
        setActiveWallet("");
        try {
            walletlist.loadWalletList(message);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CipherException e) {
            e.printStackTrace();
        }
    }

    private void loadWallet(Message message){
        setSessionPage("wallet");
        try {
            wallet.loadWallet(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createWallet(Message message){
        if (walletlist.createWallet()){
            try {
                walletlist.loadWalletList(message);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (CipherException e) {
                e.printStackTrace();
            }
        }
    }

    private String getSessionPage(){
        return Settings.SESSION_PAGE;
    }

    private void setSessionPage(String p){
        Settings.SESSION_PAGE = p;
    }

    private void setActiveWallet(String w){
        Settings.ACTIVE_WALLET = w;
    }

    private String getSendStep(){
        return Settings.SEND_STEP;
    }

    private String getDelStep(){
        return Settings.DELETE_STEP;
    }

}
