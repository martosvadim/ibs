package edu.ibs.core.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @date Oct 31, 2012
 *
 * @author Vadim Martos
 */
@Entity
@Table(name = "Transaction")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "Transaction.findAll", query = "SELECT t FROM Transaction t"),
	@NamedQuery(name = "Transaction.findById", query = "SELECT t FROM Transaction t WHERE t.id = :id"),
	@NamedQuery(name = "Transaction.history", query = "SELECT t FROM Transaction t WHERE t.type = :type AND (SELECT bb.ownerID from BankBook bb where bb.id = t.toBankBookID) = :ownerID"),
	@NamedQuery(name = "Transaction.findByAmount", query = "SELECT t FROM Transaction t WHERE t.amount = :amount"),
	@NamedQuery(name = "Transaction.findByType", query = "SELECT t FROM Transaction t WHERE t.type = :type")})
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
	@JoinColumn(name = "currencyID", referencedColumnName = "id", updatable = false)
	@ManyToOne(optional = false)
	private Currency currency;
	@JoinColumn(name = "toBankBookID", referencedColumnName = "id", updatable = false)
	@ManyToOne(optional = false)
	private BankBook to;
	@JoinColumn(name = "fromBankBookID", referencedColumnName = "id", updatable = false)
	@ManyToOne(optional = false)
	private BankBook from;

	public Transaction() {
	}

	public Transaction(long amount, TransactionType type, Currency currency, BankBook to, BankBook from) {
		this.amount = amount;
		this.type = type;
		this.currency = currency;
		this.to = to;
		this.from = from;
	}

	public long getAmount() {
		return amount;
	}

	public Currency getCurrency() {
		return currency;
	}

	public BankBook getFrom() {
		return from;
	}

	public long getId() {
		return id;
	}

	public BankBook getTo() {
		return to;
	}

	public TransactionType getType() {
		return type;
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
		return "Transaction{" + "id=" + id + ", amount=" + amount + ", type=" + type + ", currency=" + currency + ", to=" + to + ", from=" + from + '}';
	}

	public static enum TransactionType {

		PAYMENT("payment"),
		TRANSFER("transfer");
		private String name;
		private static final Map<String, TransactionType> nameLookUp = new HashMap<String, TransactionType>(TransactionType.values().length);
		private static final Map<Integer, TransactionType> ordinalLookUp = new HashMap<Integer, TransactionType>(TransactionType.values().length);

		static {
			for (TransactionType type : TransactionType.values()) {
				nameLookUp.put(type.toString(), type);
				ordinalLookUp.put(type.ordinal(), type);
			}
		}

		private TransactionType(String name) {
			this.name = name;
		}

		public static TransactionType forName(String name) {
			return nameLookUp.get(name);
		}

		public static TransactionType forOrdinal(int ordinal) {
			return ordinalLookUp.get(Integer.valueOf(ordinal));
		}

		@Override
		public String toString() {
			return this.name;
		}
	}
}
