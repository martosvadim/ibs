package edu.ibs.common.dto;

import edu.ibs.common.enums.Period;

/**
 * User: EgoshinME
 * Date: 17.12.12
 * Time: 5:23
 */
public class CreditPlanDTO implements IBaseDTO {
	private long id;
	private int percent;
	private Period period;
	private int periodMultiply;
	private long moneyLimit;
	private String name;
	private boolean freezed;
	private CurrencyDTO currency;
	private MoneyDTO limit;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getPercent() {
		return percent;
	}

	public void setPercent(int percent) {
		this.percent = percent;
	}

	public Period getPeriod() {
		return period;
	}

	public void setPeriod(Period period) {
		this.period = period;
	}

	public int getPeriodMultiply() {
		return periodMultiply;
	}

	public void setPeriodMultiply(int periodMultiply) {
		this.periodMultiply = periodMultiply;
	}

	public long getMoneyLimit() {
		return moneyLimit;
	}

	public void setMoneyLimit(long moneyLimit) {
		this.moneyLimit = moneyLimit;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public MoneyDTO getLimit() {
		return limit;
	}

	public void setLimit(MoneyDTO limit) {
		this.limit = limit;
	}
}
