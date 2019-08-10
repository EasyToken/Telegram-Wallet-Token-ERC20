package info.bcdev.telegramwallet.bot;

import java.math.BigDecimal;

public class TransactionInstance {

    private String addressFrom;
    private String addresTo;
    private String amount;

    public TransactionInstance(){
    }

    public TransactionInstance(String addresTo, String amount) {
        this.addresTo = addresTo;
        this.amount = amount;
    }

    public TransactionInstance(String addressFrom, String addresTo, String amount) {
        this.addressFrom = addressFrom;
        this.addresTo = addresTo;
        this.amount = amount;
    }

    public String getAddressFrom() {
        return addressFrom;
    }

    public void setAddressFrom(String addressFrom) {
        this.addressFrom = addressFrom;
    }

    public String getAddresTo() {
        return addresTo;
    }

    public void setAddresTo(String addresTo) {
        this.addresTo = addresTo;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void Clear(){
        addressFrom = null;
        addresTo = null;
        amount = null;
    }

}
