package info.bcdev.telegramwallet.ethereum;

import info.bcdev.telegramwallet.Main;
import info.bcdev.telegramwallet.Settings;
import org.bitcoinj.crypto.ChildNumber;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.HDUtils;
import org.bitcoinj.wallet.DeterministicKeyChain;
import org.bitcoinj.wallet.DeterministicSeed;
import org.bitcoinj.wallet.UnreadableWalletException;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.MnemonicUtils;
import org.web3j.crypto.WalletUtils;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateEW {

    private String filename;

    private static final SecureRandom secureRandom = new SecureRandom();

    private Settings settings = Main.settings;

    private String dir = settings.getWalletDir();
    private String passwordwallet = settings.getWalletPassword();


    public Map<String, String> CreateEW() {

    File keydir = new File(dir);

    byte[] initialEntropy = new byte[16];
    secureRandom.nextBytes(initialEntropy);

    String seedCode = MnemonicUtils.generateMnemonic(initialEntropy);

// BitcoinJ
    DeterministicSeed seed = null;
    Map<String, String> result = null;
    try {
        seed = new DeterministicSeed(seedCode, null, passwordwallet, 1409478661L);

        DeterministicKeyChain chain = DeterministicKeyChain.builder().seed(seed).build();
        List<ChildNumber> keyPath = HDUtils.parsePath("M/44H/60H/0H/0/0");
        DeterministicKey key = chain.getKeyByPath(keyPath, true);
        BigInteger privKey = key.getPrivKey();

// Web3j
        Credentials credentials = Credentials.create(privKey.toString(16));
        //System.out.println("seedCode: " + seedCode);
        //System.out.println("Generate BitcoinJ address: " +credentials.getAddress());
        //System.out.println("PrivateKey: " +credentials.getEcKeyPair().getPrivateKey());
        //System.out.println("PublicKey: " +credentials.getEcKeyPair().getPublicKey());

        String FileWallet = WalletUtils.generateWalletFile(passwordwallet, credentials.getEcKeyPair(), keydir, false);
        //System.out.println("BIP44 FILE Wallet: "+FileWallet);

        result = new HashMap<>();
        result.put("seed", seedCode);
        result.put("address", credentials.getAddress());
        result.put("filewallet", FileWallet);

    } catch (UnreadableWalletException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    } catch (CipherException e) {
        e.printStackTrace();
    }
    return result;
}

}
