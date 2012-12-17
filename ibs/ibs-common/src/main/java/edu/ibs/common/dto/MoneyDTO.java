package edu.ibs.common.dto;

import java.math.BigDecimal;

/**
 * User: EgoshinME
 * Date: 17.12.12
 * Time: 5:15
 */
public class MoneyDTO implements IBaseDTO {
	private BigDecimal amount;
	private CurrencyDTO currency;

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public CurrencyDTO getCurrency() {
		return currency;
	}

	public void setCurrency(CurrencyDTO currency) {
		this.currency = currency;
	}
}
