package edu.ibs.core.entity;

import edu.ibs.common.dto.TransactionType;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * @date Oct 31, 2012
 *
 * @author Vadim Martos
 */
@Entity
@Table(name = "Transaction")
@XmlRootElement
public class Transaction implements Serializable, AbstractEntity {

	private static final long serialVersionUID = 1389809123753412L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id", unique = true, updatable = false)
	private long id;
	@Basic(optional = false)
	@Column(name = "amount", updatable = false)
	private long amount;
	@Basic(optional = false)
	@Column(name = "type", updatable = false)
	@Enumerated(EnumType.STRING)
	private TransactionType type;
	@Basic(optional = false)
	@Column(name = "date", updatable = false)
	private long date;
	@JoinColumn(name = "currencyID", referencedColumnName = "id", updatable = false)
	@ManyToOne(optional = false)
	private Currency currency;
	@JoinColumn(name = "toCardBookID", referencedColumnName = "id", updatable = false)
	@ManyToOne(optional = false)
	private CardBook to;
	@JoinColumn(name = "fromCardBookID", referencedColumnName = "id", updatable = false)
	@ManyToOne(optional = false)
	private CardBook from;
	@Transient
	private Money money;
	@Column(name = "description")
	private String description;

	public Transaction() {
	}

	public Transaction(Money money, TransactionType type, CardBook from, CardBook to) {
		this.amount = money.balance();
		this.type = type;
		this.currency = money.currency();
		this.to = to;
		this.from = from;
		this.money = money;
		this.date = System.currentTimeMillis();
	}

	private void validateMoney() {
		if (money == null) {
			money = new Money(amount, currency);
		}
	}

	public Money getMoney() {
		validateMoney();
		return money;
	}

	@Override
	public long getId() {
		return id;
	}

	public CardBook getFrom() {
		return from;
	}

	public CardBook getTo() {
		return to;
	}

	public TransactionType getType() {
		return type;
	}

	public long getDate() {
		return date;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Transaction other = (Transaction) obj;
		if (this.id != other.id) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 23 * hash + (int) (this.id ^ (this.id >>> 32));
		return hash;
	}

	@Override
	public String toString() {
		return "Transaction{" + "id=" + id + ", amount=" + amount + ", type=" + type + ", currency=" + currency + ", to=" + to + ", from=" + from + ", money=" + money + '}';
	}
}
