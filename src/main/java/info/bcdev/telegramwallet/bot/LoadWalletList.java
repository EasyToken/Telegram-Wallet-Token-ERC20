package info.bcdev.telegramwallet.bot;

import info.bcdev.telegramwallet.ethereum.WalletsInstance;
import info.bcdev.telegramwallet.qr.QRCode;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static info.bcdev.telegramwallet.bot.session.Session.SETTINGS;

public class LoadWalletList {

    public List<WalletsInstance> getWalletList(){

        List<WalletsInstance> walletsInstanceList = new ArrayList<>();
        QRCode qrCode = new QRCode();

        File KeyDir = new File(SETTINGS.getWalletDir());
        if (!KeyDir.exists()) {
            KeyDir.mkdirs();
        } else {

            File[] listfiles = loadFiles(KeyDir);

            for (int i = 0; i < listfiles.length; i++){

                //////////////////////
                if (listfiles[i].getName().matches("UTC--.+\\.json")) {
                    Credentials credentials = null;
                    try {
                        credentials = WalletUtils.loadCredentials(SETTINGS.getWalletPassword(), SETTINGS.getWalletDir() + "" + listfiles[i].getName());
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (CipherException e) {
                        e.printStackTrace();
                    }
                    ///////////////////////////////////
                    String walletaddress = credentials.getAddress();
                    File fileqrcode = qrCode.qrGen(walletaddress, SETTINGS.getQRCodeDir()).toFile();
                    walletsInstanceList.add(new WalletsInstance(walletaddress, credentials, listfiles[i], fileqrcode));
                }
            }
        }
        return walletsInstanceList;
    }

    private File[] loadFiles(File KeyDir){
        return KeyDir.listFiles();
    }

}
