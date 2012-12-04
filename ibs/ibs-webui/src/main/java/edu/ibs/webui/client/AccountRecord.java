package edu.ibs.webui.client;

import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * Created by IntelliJ IDEA.
 * User: Максим
 * Date: 29.10.12
 * Time: 23:05
 * To change this template use File | Settings | File Templates.
 */
public class AccountRecord extends ListGridRecord {
    public AccountRecord() {
    }

    public AccountRecord(final String icon, final String cardType, final String cardNumber, final String currency,
                         final String balance, final String info) {
        setIcon(icon);
        setCardType(cardType);
        setCardNumber(cardNumber);
        setCurrency(currency);
        setBalance(balance);
        setInfo(info);
    }


    public void setIcon(String icon) {
        setAttribute("icon", icon);
    }


    public void setCardType(String cardType) {
        setAttribute("cardType", cardType);
    }


    public void setCardNumber(String cardNumber) {
        setAttribute("cardNumber", cardNumber);
    }


    public void setCurrency(String currency) {
        setAttribute("currency", currency);
    }


    public void setBalance(String balance) {
        setAttribute("balance", balance);
    }


    public void setInfo(String info) {
        setAttribute("info", info);
    }


    public String getIcon() {
        return getAttributeAsString("icon");
    }


    public String getCardType() {
        return getAttributeAsString("cardType");
    }


    public String getCardNumber() {
        return getAttributeAsString("cardNumber");
    }


    public String getCurrency() {
        return getAttributeAsString("currency");
    }


    public String getBalance() {
        return getAttributeAsString("balance");
    }


    public String getInfo() {
        return getAttributeAsString("info");

    }

}
