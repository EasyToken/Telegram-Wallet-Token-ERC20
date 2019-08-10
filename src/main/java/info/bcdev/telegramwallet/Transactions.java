package info.bcdev.telegramwallet;

import io.reactivex.Flowable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.3.0.
 */
public class Transactions extends Contract {
    private static final String BINARY = "608060405234801561001057600080fd5b506040516106e43803806106e48339818101604052604081101561003357600080fd5b508051602090910151600080546001600160a01b03199081163317909155600592909255600680546001600160a01b03909216918316919091179055600180549091163017905561065b806100896000396000f3fe6080604052600436106100865760003560e01c806383075e3d1161005957806383075e3d14610145578063853828b61461015a5780639ed3edf01461016f578063f0c55d0414610196578063f2fde38b146101bc57610086565b8063096a8ab71461008b5780631d2f3ba4146100b757806320fd1e3e146100ea57806374d2d6261461011b575b600080fd5b34801561009757600080fd5b506100b5600480360360208110156100ae57600080fd5b50356101ef565b005b3480156100c357600080fd5b506100b5600480360360208110156100da57600080fd5b50356001600160a01b031661023d565b3480156100f657600080fd5b506100ff6102a8565b604080516001600160a01b039092168252519081900360200190f35b34801561012757600080fd5b506100b56004803603602081101561013e57600080fd5b50356102b7565b34801561015157600080fd5b506100b5610355565b34801561016657600080fd5b506100b56103c3565b34801561017b57600080fd5b50610184610466565b60408051918252519081900360200190f35b6100b5600480360360208110156101ac57600080fd5b50356001600160a01b031661046c565b3480156101c857600080fd5b506100b5600480360360208110156101df57600080fd5b50356001600160a01b0316610508565b6000546001600160a01b031633146102385760405162461bcd60e51b81526004018080602001828103825260228152602001806106056022913960400191505060405180910390fd5b600555565b6000546001600160a01b031633146102865760405162461bcd60e51b81526004018080602001828103825260228152602001806106056022913960400191505060405180910390fd5b600680546001600160a01b0319166001600160a01b0392909216919091179055565b6006546001600160a01b031681565b6000546001600160a01b031633146103005760405162461bcd60e51b81526004018080602001828103825260228152602001806106056022913960400191505060405180910390fd5b80600254101561030f57600080fd5b6006546040516001600160a01b039091169082156108fc029083906000818181858888f19350505050158015610349573d6000803e3d6000fd5b506103526105bf565b50565b6000546001600160a01b0316331461039e5760405162461bcd60e51b81526004018080602001828103825260228152602001806106056022913960400191505060405180910390fd5b6000546001600160a01b031633146103b557600080fd5b6000546001600160a01b0316ff5b6000546001600160a01b0316331461040c5760405162461bcd60e51b81526004018080602001828103825260228152602001806106056022913960400191505060405180910390fd5b60006002541161041b57600080fd5b6006546001546040516001600160a01b0392831692919091163180156108fc02916000818181858888f1935050505015801561045b573d6000803e3d6000fd5b506104646105bf565b565b60055481565b600480546001600160a01b0383166001600160a01b031991821617909155600380549091163317905560055434036104a3816105c6565b6104ab6105bf565b6004546003546005546040805185815260208101929092524282820152516001600160a01b039384169392909216917fe87dda5943046f619a0430c9111cc0dadd1b23de5336345e37c89f83dbaae0e79181900360600190a35050565b6000546001600160a01b031633146105515760405162461bcd60e51b81526004018080602001828103825260228152602001806106056022913960400191505060405180910390fd5b6001600160a01b03811661056457600080fd5b600080546040516001600160a01b03808516939216917f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e091a3600080546001600160a01b0319166001600160a01b0392909216919091179055565b3031600255565b6004546040516001600160a01b039091169082156108fc029083906000818181858888f19350505050158015610600573d6000803e3d6000fd5b505056fe4f6e6c79206f776e65722063616e2063616c6c20746869732066756e6374696f6e2ea265627a7a72305820e84b57d9d149c6c7740f419428105ce9de7eac808f869750d1d534ee6c5c165e64736f6c63430005090032";

    public static final String FUNC_SETTRANSACTIONFEE = "setTransactionFee";

    public static final String FUNC_SETADDRESSWITHDRAWAL = "setAddressWithdrawal";

    public static final String FUNC_ADDRESSWITHDRAWAL = "addressWithdrawal";

    public static final String FUNC_WITHDRAWALTO = "WithdrawalTo";

    public static final String FUNC_DESTROYCONTRACT = "DestroyContract";

    public static final String FUNC_WITHDRAWALL = "withdrawAll";

    public static final String FUNC_TRANSACTIONFEE = "transactionFee";

    public static final String FUNC_TRANSACTION = "Transaction";

    public static final String FUNC_TRANSFEROWNERSHIP = "transferOwnership";

    public static final Event TRANSACTIONSLOG_EVENT = new Event("TransactionsLog", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event OWNERSHIPTRANSFERRED_EVENT = new Event("OwnershipTransferred", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}));
    ;

    @Deprecated
    protected Transactions(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Transactions(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected Transactions(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected Transactions(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteCall<TransactionReceipt> setTransactionFee(BigInteger _transactionFee) {
        final Function function = new Function(
                FUNC_SETTRANSACTIONFEE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_transactionFee)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> setAddressWithdrawal(String _addressWithdrawal) {
        final Function function = new Function(
                FUNC_SETADDRESSWITHDRAWAL, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_addressWithdrawal)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<String> addressWithdrawal() {
        final Function function = new Function(FUNC_ADDRESSWITHDRAWAL, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> WithdrawalTo(BigInteger _amount) {
        final Function function = new Function(
                FUNC_WITHDRAWALTO, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_amount)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> DestroyContract() {
        final Function function = new Function(
                FUNC_DESTROYCONTRACT, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> withdrawAll() {
        final Function function = new Function(
                FUNC_WITHDRAWALL, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> transactionFee() {
        final Function function = new Function(FUNC_TRANSACTIONFEE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> Transaction(String _receiver, BigInteger weiValue) {
        final Function function = new Function(
                FUNC_TRANSACTION, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_receiver)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    public RemoteCall<TransactionReceipt> transferOwnership(String newOwner) {
        final Function function = new Function(
                FUNC_TRANSFEROWNERSHIP, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(newOwner)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public List<TransactionsLogEventResponse> getTransactionsLogEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(TRANSACTIONSLOG_EVENT, transactionReceipt);
        ArrayList<TransactionsLogEventResponse> responses = new ArrayList<TransactionsLogEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            TransactionsLogEventResponse typedResponse = new TransactionsLogEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._from = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse._to = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse._amount = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse._fee = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse._datetime = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<TransactionsLogEventResponse> transactionsLogEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, TransactionsLogEventResponse>() {
            @Override
            public TransactionsLogEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(TRANSACTIONSLOG_EVENT, log);
                TransactionsLogEventResponse typedResponse = new TransactionsLogEventResponse();
                typedResponse.log = log;
                typedResponse._from = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse._to = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse._amount = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse._fee = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse._datetime = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<TransactionsLogEventResponse> transactionsLogEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(TRANSACTIONSLOG_EVENT));
        return transactionsLogEventFlowable(filter);
    }

    public List<OwnershipTransferredEventResponse> getOwnershipTransferredEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(OWNERSHIPTRANSFERRED_EVENT, transactionReceipt);
        ArrayList<OwnershipTransferredEventResponse> responses = new ArrayList<OwnershipTransferredEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            OwnershipTransferredEventResponse typedResponse = new OwnershipTransferredEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.previousOwner = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.newOwner = (String) eventValues.getIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<OwnershipTransferredEventResponse> ownershipTransferredEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, OwnershipTransferredEventResponse>() {
            @Override
            public OwnershipTransferredEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(OWNERSHIPTRANSFERRED_EVENT, log);
                OwnershipTransferredEventResponse typedResponse = new OwnershipTransferredEventResponse();
                typedResponse.log = log;
                typedResponse.previousOwner = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.newOwner = (String) eventValues.getIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<OwnershipTransferredEventResponse> ownershipTransferredEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(OWNERSHIPTRANSFERRED_EVENT));
        return ownershipTransferredEventFlowable(filter);
    }

    @Deprecated
    public static Transactions load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Transactions(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static Transactions load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Transactions(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static Transactions load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new Transactions(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static Transactions load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new Transactions(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<Transactions> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, BigInteger _transactionFee, String _addressWithdrawal) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_transactionFee), 
                new org.web3j.abi.datatypes.Address(_addressWithdrawal)));
        return deployRemoteCall(Transactions.class, web3j, credentials, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<Transactions> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, BigInteger _transactionFee, String _addressWithdrawal) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_transactionFee), 
                new org.web3j.abi.datatypes.Address(_addressWithdrawal)));
        return deployRemoteCall(Transactions.class, web3j, transactionManager, contractGasProvider, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<Transactions> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, BigInteger _transactionFee, String _addressWithdrawal) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_transactionFee), 
                new org.web3j.abi.datatypes.Address(_addressWithdrawal)));
        return deployRemoteCall(Transactions.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<Transactions> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, BigInteger _transactionFee, String _addressWithdrawal) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_transactionFee), 
                new org.web3j.abi.datatypes.Address(_addressWithdrawal)));
        return deployRemoteCall(Transactions.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static class TransactionsLogEventResponse {
        public Log log;

        public String _from;

        public String _to;

        public BigInteger _amount;

        public BigInteger _fee;

        public BigInteger _datetime;
    }

    public static class OwnershipTransferredEventResponse {
        public Log log;

        public String previousOwner;

        public String newOwner;
    }
}
