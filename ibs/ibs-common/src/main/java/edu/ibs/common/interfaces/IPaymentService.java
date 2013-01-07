package edu.ibs.common.interfaces;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import edu.ibs.common.dto.*;
import edu.ibs.common.enums.CardBookType;
import edu.ibs.common.exceptions.IbsServiceException;

import java.util.List;

/**
 * User: EgoshinME
 * Date: 27.12.12
 * Time: 4:27
 */
@RemoteServiceRelativePath("pay.rpc")
public interface IPaymentService extends RemoteService {
	BankBookDTO createBankBook(String userId) throws IbsServiceException;
	void pay(CardBookDTO from, long to, MoneyDTO money) throws IbsServiceException;
    List<CardBookDTO> getCardBooks(UserDTO user) throws IbsServiceException;
	List<CurrencyDTO> getCurrencies() throws IbsServiceException;
	void requestCard(UserDTO userDTO, String bankBookId, CardBookType cardBookType, CurrencyDTO currencyDTO) throws IbsServiceException;
    List<BankBookDTO> getBankBooks(final UserDTO userDTO) throws IbsServiceException;
}
