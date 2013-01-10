package edu.ibs.core.entity;

import edu.ibs.common.enums.Period;
import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @date Dec 13, 2012
 *
 * @author Vadim Martos
 */
@Entity
@Table(name = "CreditPlan")
@XmlRootElement
public class CreditPlan implements Serializable, AbstractEntity {

	private static final long serialVersionUID = 47183913713123L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id")
	private long id;
	@Basic(optional = false)
	@Column(name = "percent")
	private int percent;
//	@Basic(optional = false)
//	@Column(name = "period", updatable = false)
//	@Enumerated(EnumType.STRING)
	@Basic(optional = false)
	@Column(name = "period", updatable = false)
	private long period;
	@Basic(optional = false)
	@Column(name = "moneyLimit")
	private long moneyLimit;
	@Basic(optional = false)
	@Column(name = "name")
	private String name;
	@Basic(optional = false)
	@Column(name = "freezed")
	private boolean freezed;
	@JoinColumn(name = "currencyID", referencedColumnName = "id")
	@ManyToOne(optional = false)
	private Currency currency;
	@Transient
	private Money limit;

	public CreditPlan() {
	}

	public CreditPlan(String name, Money limit, Period period, int periodMultiply, int percent) {
		this(name, limit, Period.calculatePeriod(period, periodMultiply), percent);
	}

	public CreditPlan(String name, Money limit, long period, int percent) {
		this.percent = percent;
		this.period = period;
		this.moneyLimit = limit.balance();
		this.currency = limit.currency();
		this.name = name;
	}

	private void validateMoney() {
		if (limit == null) {
			limit = new Money(moneyLimit, currency);
		}
	}

	public Currency getCurrency() {
		return currency;
	}

	public boolean isFreezed() {
		return freezed;
	}

	public void setFreezed(boolean freezed) {
		this.freezed = freezed;
	}

	public long getId() {
		return id;
	}

	public Money getLimit() {
		validateMoney();
		return limit;
	}

	public int getPercent() {
		return percent;
	}

	public long getPeriod() {
		return period;
	}

	public String getName() {
		return name;
	}
}
