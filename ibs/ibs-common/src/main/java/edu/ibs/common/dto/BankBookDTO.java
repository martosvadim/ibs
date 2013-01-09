package edu.ibs.common.dto;

/**
 * User: EgoshinME
 * Date: 17.12.12
 * Time: 4:57
 */
public class BankBookDTO implements IBaseDTO {

	private long id;
	private String balance;
	private boolean freezed;
	private CurrencyDTO currency;
	private UserDTO owner;
	private long dateExpire;
	private String description;
	private volatile MoneyDTO money;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public boolean isFreezed() {
		return freezed;
	}

	public void setFreezed(boolean freezed) {
		this.freezed = freezed;
	}

	public CurrencyDTO getCurrency() {
		return currency;
	}

	public void setCurrency(CurrencyDTO currency) {
		this.currency = currency;
	}

	public UserDTO getOwner() {
		return owner;
	}

	public void setOwner(UserDTO owner) {
		this.owner = owner;
	}

	public long getDateExpire() {
		return dateExpire;
	}

	public void setDateExpire(long dateExpire) {
		this.dateExpire = dateExpire;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public MoneyDTO getMoney() {
		return money;
	}

	public void setMoney(MoneyDTO money) {
		this.money = money;
	}
}
