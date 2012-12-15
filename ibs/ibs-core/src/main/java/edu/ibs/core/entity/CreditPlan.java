package edu.ibs.core.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
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
@NamedQueries({
	@NamedQuery(name = "CreditPlan.findAll", query = "SELECT c FROM CreditPlan c"),
	@NamedQuery(name = "CreditPlan.findById", query = "SELECT c FROM CreditPlan c WHERE c.id = :id"),
	@NamedQuery(name = "CreditPlan.findByPercent", query = "SELECT c FROM CreditPlan c WHERE c.percent = :percent"),
	@NamedQuery(name = "CreditPlan.findByPeriod", query = "SELECT c FROM CreditPlan c WHERE c.period = :period"),
	@NamedQuery(name = "CreditPlan.findByPeriodMultiply", query = "SELECT c FROM CreditPlan c WHERE c.periodMultiply = :periodMultiply"),
	@NamedQuery(name = "CreditPlan.findByMoneyLimit", query = "SELECT c FROM CreditPlan c WHERE c.moneyLimit = :moneyLimit"),
	@NamedQuery(name = "CreditPlan.findByFreezed", query = "SELECT c FROM CreditPlan c WHERE c.freezed = :freezed")})
public class CreditPlan implements Serializable, AbstractEntity {

	private static final long serialVersionUID = 47183913713123L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@NotNull
	@Column(name = "id")
	private long id;
	@Basic(optional = false)
	@NotNull
	@Column(name = "percent")
	private int percent;
	@Basic(optional = false)
	@Column(name = "period", updatable = false)
	@Enumerated(EnumType.STRING)
	private Period period;
	@Basic(optional = false)
	@NotNull
	@Column(name = "periodMultiply")
	private int periodMultiply;
	@Basic(optional = false)
	@NotNull
	@Column(name = "moneyLimit")
	private long moneyLimit;
	@Basic(optional = false)
	@NotNull
	@Column(name = "name")
	private String name;
	@Basic(optional = false)
	@NotNull
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
		this.percent = percent;
		this.period = period;
		this.periodMultiply = periodMultiply;
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

	public Period getPeriod() {
		return period;
	}

	public int getPeriodMultiply() {
		return periodMultiply;
	}

	public String getName() {
		return name;
	}

	public static enum Period {

		DAY("day"),
		WEEK("week"),
		MONTH("month"),
		YEAR("year");
		private static final Map<String, Period> nameLookUp = new HashMap<String, Period>(Period.values().length);
		private static final Map<Integer, Period> ordinalLookUp = new HashMap<Integer, Period>(Period.values().length);
		private final String name;

		static {
			for (Period type : Period.values()) {
				nameLookUp.put(type.toString(), type);
				ordinalLookUp.put(type.ordinal(), type);
			}
		}

		private Period(String name) {
			this.name = name;
		}

		public static Period forName(String name) {
			return nameLookUp.get(name);
		}

		public static Period forOrdinal(int ordinal) {
			return ordinalLookUp.get(Integer.valueOf(ordinal));
		}

		@Override
		public String toString() {
			return this.name;
		}
	}
}
