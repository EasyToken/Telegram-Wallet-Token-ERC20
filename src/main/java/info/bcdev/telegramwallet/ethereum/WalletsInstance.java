package info.bcdev.telegramwallet.ethereum;

import org.web3j.crypto.Credentials;

import java.io.File;

public class WalletsInstance {

    private String walletaddress;
    private Credentials credentials;
    private File filewallet;

    public WalletsInstance(String walletaddress, Credentials credentials, File filewallet) {
        this.walletaddress = walletaddress;
        this.credentials = credentials;
        this.filewallet = filewallet;
    }

    public String getWalletaddress() {
        return walletaddress;
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public File getFilewallet() {
        return filewallet;
    }

    public Boolean checkWallet(String walletaddress){
        if (walletaddress != null){
            if (this.walletaddress.equals(walletaddress)){
                return true;
            }
        }
        return false;
    }
}
