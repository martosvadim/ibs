package edu.ibs.webui.client;

import edu.ibs.common.dto.AccountDTO;
import edu.ibs.common.dto.BankBookDTO;

import java.util.List;

/**
 * User: Максим
 * Date: 30.12.12
 * Time: 15:37
 */
public final class ApplicationManager {
    private static ApplicationManager instance;
    private AccountDTO account;
    private List<BankBookDTO> bankBookDTOList;

    private ApplicationManager() {

    }

    public static ApplicationManager getInstance() {
        if (instance == null) {
            instance = new ApplicationManager();
        }
        return instance;
    }

    public AccountDTO getAccount() {
        return account;
    }

    public void setAccount(final AccountDTO account) {
        this.account = account;
    }

    public List<BankBookDTO> getBankBookDTOList() {
        return bankBookDTOList;
    }

    public void setBankBookDTOList(List<BankBookDTO> bankBookDTOList) {
        this.bankBookDTOList = bankBookDTOList;
    }
}
