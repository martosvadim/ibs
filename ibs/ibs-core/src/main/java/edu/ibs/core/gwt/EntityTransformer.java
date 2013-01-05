package edu.ibs.core.gwt;

import edu.ibs.common.dto.AccountDTO;
import edu.ibs.common.dto.BankBookDTO;
import edu.ibs.common.dto.CardBookDTO;
import edu.ibs.common.dto.CreditDTO;
import edu.ibs.common.dto.CreditPlanDTO;
import edu.ibs.common.dto.CurrencyDTO;
import edu.ibs.common.dto.MoneyDTO;
import edu.ibs.common.dto.UserDTO;
import edu.ibs.core.entity.Account;
import edu.ibs.core.entity.BankBook;
import edu.ibs.core.entity.CardBook;
import edu.ibs.core.entity.Credit;
import edu.ibs.core.entity.CreditPlan;
import edu.ibs.core.entity.Currency;
import edu.ibs.core.entity.Money;
import edu.ibs.core.entity.User;

/**
 * User: EgoshinME
 * Date: 17.12.12
 * Time: 6:19
 */
public final class EntityTransformer {

	public static AccountDTO transformAccount(final Account account) {
		AccountDTO dto = new AccountDTO();
		if (account != null) {
			dto.setId(account.getId());
			dto.setAvatar(account.getAvatar());
			dto.setEmail(account.getEmail());
			dto.setPassword(account.getPassword());
			dto.setRole(account.getRole());
			dto.setSecurityAnswer(account.getSecurityAnswer());
			dto.setSecurityQuestion(account.getSecurityQuestion());
			dto.setUser(transformUser(account.getUser()));
		}
		return dto;
	}

	public static UserDTO transformUser(final User user) {
		UserDTO dto = new UserDTO();
		if (user != null) {
			dto.setAddress(user.getAddress());
			dto.setDescription(user.getDescription());
			dto.setEmail(user.getDescription());
			dto.setFirstName(user.getFirstName());
			dto.setFreezed(user.isFreezed());
			dto.setId(user.getId());
			dto.setLastName(user.getLastName());
			dto.setPassportNumber(user.getPassportNumber());
			dto.setPassportScan(user.getPassportScan());
			dto.setPhone1(user.getPhone1());
			dto.setPhone2(user.getPhone2());
			dto.setPhone3(user.getPhone3());
			dto.setZipCode(user.getZipCode());
		}
		return dto;
	}

    public static CardBookDTO transformCardBook(final CardBook cardBook) {
        CardBookDTO dto = new CardBookDTO();
		if (cardBook != null) {
			dto.setDateExpire(cardBook.getDateExpire());
			dto.setFreezed(cardBook.isFreezed());
			dto.setId(cardBook.getId());
			dto.setPin(cardBook.getPin());
			dto.setType(cardBook.getType());
			dto.setBankBook(transformBankBook(cardBook.getBankBook()));
			dto.setCredit(transformCredit(cardBook.getCredit()));
		}
        return dto;
    }

	public static CreditDTO transformCredit(final Credit credit) {
        CreditDTO dto = new CreditDTO();
		if (credit != null) {
			dto.setId(credit.getId());
			dto.setNextPayDate(credit.getNextPayDate());
			dto.setMoney(transformMoney(credit.getMoney()));
			dto.setCreditPlan(transformCreditPlan(credit.getCreditPlan()));
		}
        return dto;
    }

	public static CreditPlanDTO transformCreditPlan(CreditPlan creditPlan) {
        CreditPlanDTO dto = new CreditPlanDTO();
		if (creditPlan != null) {
			dto.setId(creditPlan.getId());
			dto.setCurrency(transformCurrency(creditPlan.getCurrency()));
			dto.setFreezed(creditPlan.isFreezed());
			dto.setLimit(transformMoney(creditPlan.getLimit()));
			dto.setName(creditPlan.getName());
			dto.setPercent(creditPlan.getPercent());
			dto.setPeriod(creditPlan.getPeriod());
			dto.setPeriodMultiply(creditPlan.getPeriodMultiply());
		}
        return dto;
    }

	public static MoneyDTO transformMoney(final Money money) {
        MoneyDTO dto = new MoneyDTO();
		if (money != null) {
        	dto.setCurrency(transformCurrency(money.currency()));
        	//todo Как конвертировать?
		}
        return dto;
    }

	public static CurrencyDTO transformCurrency(final Currency currency) {
        CurrencyDTO dto = new CurrencyDTO();
		if (currency != null) {
			dto.setFactor(currency.getFloatFactor());
			dto.setFraction(currency.getFraction());
			dto.setId(currency.getId());
			dto.setName(currency.getName());
		}
        return dto;
    }

    public static BankBookDTO transformBankBook(final BankBook bankBook) {
        BankBookDTO dto = new BankBookDTO();
		if (bankBook != null) {
			dto.setId(bankBook.getId());
			dto.setCurrency(transformCurrency(bankBook.getCurrency()));
			dto.setDateExpire(bankBook.getDateExpire());
			dto.setDescription(bankBook.getDescription());
			dto.setFreezed(bankBook.isFreezed());
			dto.setMoney(transformMoney(bankBook.getMoney()));
			dto.setOwner(transformUser(bankBook.getOwner()));
		}
        return dto;
    }
}
