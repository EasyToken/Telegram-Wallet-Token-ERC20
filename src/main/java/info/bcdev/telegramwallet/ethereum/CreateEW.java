package info.bcdev.telegramwallet.ethereum;

import info.bcdev.telegramwallet.Settings;
import info.bcdev.telegramwallet.Main;
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

    private static final SecureRandom secureRandom = new SecureRandom();

     Settings settings;
    private String dir;
    private String passwordwallet;

    public CreateEW(Settings settings) {
        this.settings = settings;
        dir = settings.getWalletDir();
        passwordwallet = settings.getWalletPassword();
    }

    public Map<String, String> CreateEW() {

        File keydir = new File(dir);

        byte[] initialEntropy = new byte[16];

        secureRandom.nextBytes(initialEntropy);

        String seedCode = MnemonicUtils.generateMnemonic(initialEntropy);

        System.out.println(seedCode);

        Map<String, String> result = null;

        Credentials credentials = Generate(seedCode);

        if (credentials != null) {
            try {
                String FileWallet = WalletUtils.generateWalletFile(passwordwallet, credentials.getEcKeyPair(), keydir, false);
                //System.out.println("BIP44 FILE loadWallet: "+FileWallet);

                result = new HashMap<>();
                result.put("seed", seedCode);
                result.put("address", credentials.getAddress());
                result.put("filewallet", FileWallet);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (CipherException e) {
                e.printStackTrace();
            }
            return result;
        }
        return null;
    }

    public Map<String, String> CreateEW(String seedCode) {

        File keydir = new File(dir);

        System.out.println(seedCode);

        Map<String, String> result = new HashMap<>();

        Credentials credentials = Generate(seedCode);

        if (credentials != null) {
            try {
                String FileWallet = WalletUtils.generateWalletFile(passwordwallet, credentials.getEcKeyPair(), keydir, false);
                result.put("seed", seedCode);
                result.put("address", credentials.getAddress());
                result.put("filewallet", FileWallet);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (CipherException e) {
                e.printStackTrace();
            }
            return result;
        }
        return null;
    }

    private Credentials Generate(String seedCode) {
        Credentials credentials = null;
        try {
            DeterministicSeed seed = new DeterministicSeed(seedCode, null, passwordwallet, 1409478661L);

            DeterministicKeyChain chain = DeterministicKeyChain.builder().seed(seed).build();
            List<ChildNumber> keyPath = HDUtils.parsePath("M/44H/60H/0H/0/0");
            DeterministicKey key = chain.getKeyByPath(keyPath, true);
            BigInteger privKey = key.getPrivKey();

            credentials = Credentials.create(privKey.toString(16));
        } catch (UnreadableWalletException e) {
            e.printStackTrace();
        }
        return credentials;
    }

    }
