package info.bcdev.telegramwallet.ethereum;

import info.bcdev.lib.web3j.Transactions.CTransaction;
import info.bcdev.telegramwallet.Transactions;
import info.bcdev.telegramwallet.Settings;
import info.bcdev.telegramwallet.bot.session.Session;
import info.bcdev.telegramwallet.erc20.TokenERC20;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.StaticGasProvider;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigInteger;

public class SendCoin {

    private Web3j web3j;
    private Credentials credentials;
    private String gasPrice;
    private String gasLimit;

    public SendCoin(Web3j web3j, Credentials credentials, String gasPrice, String gasLimit) {
        this.web3j = web3j;
        this.credentials = credentials;
        this.gasPrice = gasPrice;
        this.gasLimit = gasLimit;
    }

    public boolean sendingToken(String tokenAddress, String addressTo, String ammount) {

        ContractGasProvider contractGasProvider = new StaticGasProvider(getGasPrice(),getGasLimit());
        TokenERC20 token = TokenERC20.load(tokenAddress, web3j, credentials, contractGasProvider);

        int ammountInt = Integer.valueOf(ammount);

        try {
            String status = token.transfer(addressTo, BigInteger.valueOf(ammountInt)).send().getStatus();

            if (status.equals("0x1")) return true;

        } catch (TelegramApiException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public EthSendTransaction sendingEther(String addressTo, String ammount){

        CTransaction cTransaction = new CTransaction(web3j, credentials, gasPrice, gasLimit);
        try {
            return cTransaction.sendTransaction(Session.ACTIVE_WALLET, addressTo, ammount);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public TransactionReceipt sendingEtherSC(String addressTo, String ammount){

        ContractGasProvider contractGasProvider = new StaticGasProvider(getGasPrice(),getGasLimit());

        Transactions transactions = Transactions.load( "0x02E6f2387B5d55d7627346268BAfec9960C95531",web3j, credentials, contractGasProvider);

        BigInteger ammountInt = Convert.toWei(ammount, Convert.Unit.ETHER).toBigInteger();

        try {
            TransactionReceipt transactionReceipt = transactions.Transaction(addressTo, ammountInt).send();
            if (transactionReceipt.isStatusOK()){
                return transactionReceipt;
            }
        } catch (Exception ex) {
            if (ex.getClass() == TransactionException.class){
                System.out.println("Transaction Error: "+ ex.getMessage());
            } else {
                System.out.println("Error: "+ex.getMessage());
            }
        }
        return null;
    }

    private BigInteger getGasPrice(){
        return Convert.toWei(Settings.GAS_PRICE_VALUE, Convert.Unit.GWEI).toBigInteger();
    }

    private BigInteger getGasLimit(){
        return BigInteger.valueOf(Long.valueOf(Settings.GAS_LIMIT_VALUE));
    }
}
