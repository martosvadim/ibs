package edu.ibs.common.dto;

/**
 * User: EgoshinME
 * Date: 17.12.12
 * Time: 5:21
 */
public class CreditDTO implements IBaseDTO {
	private long id;
	private long amount;
	private long lastPayDate;
	private CreditPlanDTO creditPlan;
	private MoneyDTO money;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getAmount() {
		return amount;
	}

	public void setAmount(long amount) {
		this.amount = amount;
	}

	public long getLastPayDate() {
		return lastPayDate;
	}

	public void setLastPayDate(long lastPayDate) {
		this.lastPayDate = lastPayDate;
	}

	public CreditPlanDTO getCreditPlan() {
		return creditPlan;
	}

	public void setCreditPlan(CreditPlanDTO creditPlan) {
		this.creditPlan = creditPlan;
	}

	public MoneyDTO getMoney() {
		return money;
	}

	public void setMoney(MoneyDTO money) {
		this.money = money;
	}
}
