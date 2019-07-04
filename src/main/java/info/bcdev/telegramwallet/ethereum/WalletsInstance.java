package info.bcdev.telegramwallet.ethereum;

import org.web3j.crypto.Credentials;

public class WalletsInstance {

    private String walletaddress;
    private Credentials credentials;

    public WalletsInstance(String walletaddress, Credentials credentials) {
        this.walletaddress = walletaddress;
        this.credentials = credentials;
    }

    public String getWalletaddress() {
        return walletaddress;
    }

    public Credentials getCredentials() {
        return credentials;
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
