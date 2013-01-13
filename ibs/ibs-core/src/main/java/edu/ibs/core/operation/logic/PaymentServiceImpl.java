package edu.ibs.core.operation.logic;

import edu.ibs.common.dto.*;
import edu.ibs.common.enums.CardBookType;
import edu.ibs.common.exceptions.IbsServiceException;
import edu.ibs.common.interfaces.IPaymentService;
import edu.ibs.core.controller.exception.FreezedException;
import edu.ibs.core.controller.exception.NotEnoughMoneyException;
import edu.ibs.core.currencies.CurrenciesCache;
import edu.ibs.core.entity.*;
import edu.ibs.core.gwt.EntityTransformer;
import edu.ibs.core.operation.AdminOperations;
import edu.ibs.core.operation.UserOperations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * User: EgoshinME Date: 27.12.12 Time: 4:30
 */
public class PaymentServiceImpl implements IPaymentService {

	private UserOperations userLogic;
	private AdminOperations adminLogic;
	private CurrenciesCache currenciesCache;

	@Override
	public BankBookDTO createBankBook(UserDTO userDTO, CurrencyDTO currencyDTO) throws IbsServiceException {
		BankBookDTO dto = null;
		try {
			if (userDTO != null && userDTO.getId() > 0) {
				User user = new User(userDTO);
				if (user != null) {
					Currency currency = new Currency(currencyDTO);
					Money money = new Money(0, currency);
					BankBook bankBook = adminLogic.createBankBook(user, money);
					dto = EntityTransformer.transformBankBook(bankBook);
				} else {
					throw new IbsServiceException("Введите существующий идентификатор.");
				}
			}
		} catch (IbsServiceException e) {
			throw e;
		} catch (Throwable t) {
			throw new IbsServiceException("Не удалось создать банковский счёт.");
		}
		return dto;
	}

	@Override
	public void pay(CardBookDTO from, String toId, Float amount, TransactionType ttype) throws IbsServiceException {

		try {
			Money money = parseMoney(amount, new Currency(from.getBankBook().getCurrency()));
			//todo fill transaction description here
			userLogic.pay(new CardBook(from), Long.parseLong(toId), money, ttype, null);
		} catch (FreezedException e) {
			throw new IbsServiceException("Счёт заморожен.");
		} catch (NotEnoughMoneyException e) {
			throw new IbsServiceException("Не достаточно средств.");
		} catch (Throwable t) {
			throw new IbsServiceException("При оплате возникла ошибка.");
		}
	}

	@Override
	public List<CardBookDTO> getCardBooks(final UserDTO userDto) throws IbsServiceException {
		List<CardBookDTO> list = new LinkedList<CardBookDTO>();
		if (userDto != null && userDto.getId() > 0) {
			User user = new User(userDto);
			try {
				for (CardBook cardBook : getUserLogic().getCardBooks(user)) {
					list.add(EntityTransformer.transformCardBook(cardBook));
				}
			} catch (Throwable t) {
				throw new IbsServiceException("Ошибка при получении списка карт.");
			}
		}
		return list;
	}

	@Override
	public List<CurrencyDTO> getCurrencies() throws IbsServiceException {
		try {
			List<Currency> currencies = adminLogic.getCurrencies();
			List<CurrencyDTO> result = new ArrayList<CurrencyDTO>();
			if (currencies != null) {
				for (Currency currency : currencies) {
					result.add(EntityTransformer.transformCurrency(currency));
				}
			}
			return result;
		} catch (Throwable t) {
			throw new IbsServiceException("Возникла ошибка при получении списка валют.");
		}
	}

	@Override
	public void requestCard(UserDTO userDTO, String bankBookId, CardBookType cardBookType)
			throws IbsServiceException {

		try {
			if (bankBookId != null && bankBookId.length() > 0) {
				if (CardBookType.DEBIT.equals(cardBookType)) {
					User user = new User(userDTO);
					for (BankBook bankBook : userLogic.getBankBooks(user)) {
						if (bankBook.getId() == Long.parseLong(bankBookId)) {
							userLogic.requestDebitCard(user, bankBook);
							break;
						}
					}

				} else if (CardBookType.CREDIT.equals(cardBookType)) {
				}
			}
		} catch (Throwable t) {
			throw new IbsServiceException("Ошибка при обработке запроса на карт-счет.");
		}
	}

	@Override
	public List<BankBookDTO> getBankBooks(UserDTO userDTO) throws IbsServiceException {
		List<BankBookDTO> result = new LinkedList<BankBookDTO>();
		if (userDTO != null) {
			User user = new User(userDTO);
			for (BankBook bankBook : userLogic.getBankBooks(user)) {
				result.add(EntityTransformer.transformBankBook(bankBook));
			}
		}
		return result;
	}

	@Override
	public List<CardRequestDTO> getCardRequests() throws IbsServiceException {
		List<CardRequestDTO> result = new LinkedList<CardRequestDTO>();
		try {
			for (CardRequest cardRequest : adminLogic.getAllRequests()) {
				result.add(EntityTransformer.transformCardRequest(cardRequest));
			}
		} catch (Throwable t) {
			throw new IbsServiceException("Ошибка при получении списка заявок на карт-счета.");
		}
		return result;
	}

	@Override
	public CardBookDTO approveCardRequest(CardRequestDTO dto) throws IbsServiceException {
		CardBookDTO cardBookDTO = new CardBookDTO();
		try {
			CardRequest cardRequest = new CardRequest(dto);
			cardBookDTO = EntityTransformer.transformCardBook(adminLogic.approve(cardRequest));
		} catch (Throwable t) {
			throw new IbsServiceException("Ошибка при создании карт-счета.");
		}
		return cardBookDTO;
	}

	@Override
	public void declineCardRequest(CardRequestDTO dto, String reason) throws IbsServiceException {
		try {
			adminLogic.decline(new CardRequest(dto), reason);
		} catch (Throwable t) {
			throw new IbsServiceException("Ошибка при отклонении заявки на создание карт-счета.");
		}
	}

	@Override
	public Boolean addMoney(BankBookDTO bankBookDTO, Float amount) throws IbsServiceException {
		if (bankBookDTO != null && bankBookDTO.getId() != 0) {
			try {
				BankBook bankBook = new BankBook(bankBookDTO);
				Money money = parseMoney(amount, bankBook.getCurrency());
				return adminLogic.addMoney(bankBook, money);
			} catch (FreezedException fe) {
				throw new IbsServiceException(fe.getLocalizedMessage());
			} catch (Throwable t) {
				throw new IbsServiceException("Не удалось пополнить счёт.");
			}
		}
		return false;
	}

	private Money parseMoney(Float amount, Currency currency) throws IbsServiceException {
		try {
			BigDecimal bd = new BigDecimal(amount.toString());
			long part1 = amount.longValue();
			String part2Str = bd.subtract(new BigDecimal(part1)).toString();
			int index = part2Str.indexOf('.');
			if (index == 0) {
				index = part2Str.indexOf(',');
			}
			part2Str = part2Str.substring(index + 1);
//            int part2 = Integer.valueOf(part2Str);
			return Money.parseMoney(String.valueOf(part1), part2Str, currency);
		} catch (Throwable t) {
			throw new IbsServiceException("Неверный формат суммы.");
		}
	}

	@Override
	public BankBookDTO getBankBook(AccountDTO accountDTO, long id) throws IbsServiceException {
		try {
			BankBook bankBook = adminLogic.getBankBook(new Account(accountDTO), id);
			if (bankBook == null || id == 1) {
				throw new IbsServiceException("Счёт не существует.");
			}
			return EntityTransformer.transformBankBook(bankBook);
		} catch (IbsServiceException e) {
			throw e;
		} catch (Throwable t) {
			throw new IbsServiceException("Ошибка при получении банковского счёта.");
		}
	}

	@Override
	public void refreshCurrencies() throws IbsServiceException {
		try {
			getCurrenciesCache().setList(adminLogic.getCurrencies());
			getCurrenciesCache().fillCurrencies();
		} catch (Throwable t) {
			throw new IbsServiceException("Не удалось обновить курсы валют.");
		}
	}

	@Override
	public UserDTO getUser(String email) throws IbsServiceException {
		UserDTO userDTO = null;
		if (email != null && email.length() > 0) {
			try {
				userDTO = EntityTransformer.transformUser(adminLogic.getUser(email));
			} catch (Throwable t) {
				throw new IbsServiceException("Ошибка при получении пользователя.");
			}
		}
		return userDTO;
	}

	@Override
	public List<CardBookDTO> getContragentList() throws IbsServiceException {
		List<CardBookDTO> list = new ArrayList<CardBookDTO>();
		try {
			for (Provider provider : adminLogic.getProviderList()) {
				list.add(EntityTransformer.transformCardBook(provider.getCard()));
			}
		} catch (Throwable t) {
			throw new IbsServiceException("Ошибка получения списка контрагентов.");
		}
		return list;
	}

	public List<TransactionDTO> getHistory(UserDTO userDto, TransactionType tt) throws IbsServiceException {
		try {
			List<Transaction> list = userLogic.getAllHistory(new User(userDto));
			List<TransactionDTO> ret = new ArrayList<TransactionDTO>();
			for (Transaction t : list) {
				ret.add(EntityTransformer.transformTransaction(t));
			}
			return ret;
		} catch (Throwable t) {
			throw new IbsServiceException("Ошибка при получении выписки.");
		}
	}

	public List<TransactionDTO> getHistory(UserDTO userDto, Date from, Date to) throws IbsServiceException {
		try {
			List<Transaction> list = userLogic.getAllHistory(new User(userDto), from, to);
			List<TransactionDTO> ret = new ArrayList<TransactionDTO>();
			for (Transaction t : list) {
				ret.add(EntityTransformer.transformTransaction(t));
			}
			return ret;
		} catch (Throwable t) {
			throw new IbsServiceException("Ошибка при получении выписки.");
		}
	}

	public UserOperations getUserLogic() {
		return userLogic;
	}

	public void setUserLogic(final UserOperations userLogic) {
		this.userLogic = userLogic;
	}

	public AdminOperations getAdminLogic() {
		return adminLogic;
	}

	public void setAdminLogic(final AdminOperations adminLogic) {
		this.adminLogic = adminLogic;
	}

	public CurrenciesCache getCurrenciesCache() {
		return currenciesCache;
	}

	public void setCurrenciesCache(CurrenciesCache currenciesCache) {
		this.currenciesCache = currenciesCache;
	}
}
