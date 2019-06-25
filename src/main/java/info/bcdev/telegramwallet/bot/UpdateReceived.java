package info.bcdev.telegramwallet.bot;

import info.bcdev.telegramwallet.Settings;
import info.bcdev.telegramwallet.ethereum.Wallet;
import info.bcdev.telegramwallet.ethereum.WalletList;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.web3j.crypto.CipherException;

import java.io.IOException;

public class UpdateReceived {

    private Wallet wallet = new Wallet();
    private WalletList walletlist = new WalletList();

    public void Update(Update update) {
        start start = new start();
        Message message = update.getMessage();

        if (message != null && message.hasText()) {

            if (Settings.SESSION_PAGE == null){
                setSessionPage("wallets");
                setActiveWallet("");
                try {
                    walletlist.GetAccounts(message);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (CipherException e) {
                    e.printStackTrace();
                }
            }

            if (message.getText().equals("/start")) {
                setSessionPage("start");
                start.CmdStart(message);
            /*} else if (message.getText().startsWith("/Send")) {
                try {
                    wallet.Send(message);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }*/
            } else if (message.getText().equals("\uD83D\uDCBC Wallets")) {
                setSessionPage("wallets");
                setActiveWallet("");
                try {
                    walletlist.GetAccounts(message);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (CipherException e) {
                    e.printStackTrace();
                }
            } else if (message.getText().startsWith("\uD83D\uDCB0 0x")) {
                setSessionPage("wallet");
                try {
                    wallet.Wallet(update);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (CipherException e) {
                    e.printStackTrace();
                }
            } else if (message.getText().startsWith("\uD83D\uDCB3 SendToken")){
                wallet.EnterAddress(message);
            } else if (message.getText().equals("\uD83D\uDCCB Add New Wallet")) {
                if (walletlist.createWallet()){
                    try {
                        walletlist.GetAccounts(message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (CipherException e) {
                        e.printStackTrace();
                    }
                }
            } else if (message.getText().equals("\uD83D\uDC48 Back")){
                if (getSessionPage() != null) {
                    switch (getSessionPage()) {
                        case "wallets":
                            start.CmdStart(message);
                            break;
                        case "wallet":
                            try {
                                walletlist.GetAccounts(message);
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (CipherException e) {
                                e.printStackTrace();
                            }
                            break;
                        default:
                            start.CmdStart(message);
                            break;
                    }
                } else {
                    start.CmdStart(message);
                }
            } else {
                if (getSendStep() != null) {
                    switch (getSendStep()) {
                        case "enterammount":
                            Wallet.TRANSACTION_VALUE.put("addressto",message.getText());
                            wallet.EnterAmmount(message);
                            break;
                        case "sending":
                            Wallet.TRANSACTION_VALUE.put("ammount",message.getText());
                            wallet.SendToken(message);
                            break;
                    }
                }
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

}
