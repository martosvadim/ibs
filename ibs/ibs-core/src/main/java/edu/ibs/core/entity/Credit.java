package edu.ibs.core.entity;

import java.io.Serializable;
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
@Table(name = "Credit")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "Credit.findAll", query = "SELECT c FROM Credit c"),
	@NamedQuery(name = "Credit.findById", query = "SELECT c FROM Credit c WHERE c.id = :id"),
	@NamedQuery(name = "Credit.findByAmount", query = "SELECT c FROM Credit c WHERE c.amount = :amount"),
	@NamedQuery(name = "Credit.findByNextPayDate", query = "SELECT c FROM Credit c WHERE c.nextPayDate = :nextPayDate")})
public class Credit implements Serializable, AbstractEntity, MoneyEntity {

	private static final long serialVersionUID = 481713712378789123L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@NotNull
	@Column(name = "id")
	private long id;
	@Basic(optional = false)
	@NotNull
	@Column(name = "amount")
	private long amount;
	@Basic(optional = false)
	@NotNull
	@Column(name = "nextPayDate")
	private long nextPayDate;
	@JoinColumn(name = "creditPlanID", referencedColumnName = "id")
	@ManyToOne(optional = false)
	private CreditPlan creditPlan;
	@Transient
	private Money money;

	public Credit() {
	}

	public Credit(CreditPlan creditPlan) {
		this.amount = 0L;
		this.creditPlan = creditPlan;
		validateMoney();
		int multiply = creditPlan.getPeriodMultiply();
		switch (creditPlan.getPeriod()) {
			//todo fill nextPayDate according to given period and multiply
			case DAY: {
				this.nextPayDate = 1;
				break;
			}
			case MONTH: {
				this.nextPayDate = 1;
				break;
			}
			case WEEK: {
				this.nextPayDate = 1;
				break;
			}
			case YEAR: {
				this.nextPayDate = 1;
				break;
			}
		}
	}

	private void validateMoney() {
		if (money == null) {
			money = new Money(amount, creditPlan.getCurrency());
		}
	}

	public CreditPlan getCreditPlan() {
		return creditPlan;
	}

	public long getId() {
		return id;
	}

	public long getNextPayDate() {
		return nextPayDate;
	}

	public void add(Money money) {
		validateMoney();
		this.money.add(money);
		this.amount = money.balance();
	}

	public void subtract(Money money) {
		validateMoney();
		this.money.subtract(money);
		this.amount = money.balance();
	}

	public boolean lt(Money other) {
		validateMoney();
		return money.lt(other);
	}

	public boolean le(Money other) {
		validateMoney();
		return money.le(other);
	}

	public boolean gt(Money other) {
		validateMoney();
		return money.gt(other);
	}

	public boolean ge(Money other) {
		validateMoney();
		return money.ge(other);
	}

	public Money getMoney() {
		validateMoney();
		return money;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Credit other = (Credit) obj;
		if (this.id != other.id) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 29 * hash + (int) (this.id ^ (this.id >>> 32));
		return hash;
	}

	@Override
	public String toString() {
		return "Credit{" + "id=" + id + ", amount=" + amount + ", nextPayDate=" + nextPayDate + ", creditPlan=" + creditPlan + ", money=" + money + '}';
	}
}
