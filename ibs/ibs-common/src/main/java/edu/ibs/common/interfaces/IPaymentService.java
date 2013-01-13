package edu.ibs.common.interfaces;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import edu.ibs.common.dto.*;
import edu.ibs.common.enums.CardBookType;
import edu.ibs.common.exceptions.IbsServiceException;

import java.util.Date;
import java.util.List;

/**
 * User: EgoshinME
 * Date: 27.12.12
 * Time: 4:27
 */
@RemoteServiceRelativePath("pay.rpc")
public interface IPaymentService extends RemoteService {
	BankBookDTO createBankBook(UserDTO userDTO, CurrencyDTO currencyDTO) throws IbsServiceException;
    void pay(CardBookDTO from, String toId, Float money, TransactionType ttype) throws IbsServiceException;
	void pay(CardBookDTO from, String toId, Float money, TransactionType ttype, String desc) throws IbsServiceException;
    List<CardBookDTO> getCardBooks(UserDTO user) throws IbsServiceException;
	List<CurrencyDTO> getCurrencies() throws IbsServiceException;
	void requestCard(UserDTO userDTO, String bankBookId, CardBookType cardBookType) throws IbsServiceException;
    List<BankBookDTO> getBankBooks(final UserDTO userDTO) throws IbsServiceException;
	List<CardRequestDTO> getCardRequests() throws IbsServiceException;
	CardBookDTO approveCardRequest(CardRequestDTO dto) throws IbsServiceException;
	void declineCardRequest(CardRequestDTO dto, String reason) throws IbsServiceException;
	Boolean addMoney(BankBookDTO bankBookDTO, Float amount) throws IbsServiceException;
	BankBookDTO getBankBook(AccountDTO accountDTO, long id) throws IbsServiceException;
	void refreshCurrencies() throws IbsServiceException;
    UserDTO getUser(String email) throws IbsServiceException;
    List<ProviderDTO> getContragentList() throws IbsServiceException;
    List<TransactionDTO> getHistory(UserDTO userDto, TransactionType tt) throws IbsServiceException;
    List<TransactionDTO> getHistory(UserDTO userDto, Date from, Date to) throws IbsServiceException;
}
