package edu.ibs.core.entity;

import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @date Oct 31, 2012
 *
 * @author Vadim Martos
 */
@Entity
@Table(name = "BankBook")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "BankBook.findAll", query = "SELECT b FROM BankBook b"),
	@NamedQuery(name = "BankBook.findById", query = "SELECT b FROM BankBook b WHERE b.id = :id"),
	@NamedQuery(name = "BankBook.findByBalance", query = "SELECT b FROM BankBook b WHERE b.balance = :balance"),
	@NamedQuery(name = "BankBook.findByFreezed", query = "SELECT b FROM BankBook b WHERE b.freezed = :freezed")})
public class BankBook implements Serializable, AbstractEntity {

	private static final long serialVersionUID = 7689532939542907L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id", updatable = false, unique = true)
	private long id;
	@Basic(optional = false)
	@Column(name = "balance")
	private long balance;
	@Basic(optional = false)
	@Column(name = "freezed")
	private boolean freezed;
	@JoinColumn(name = "currencyID", referencedColumnName = "id", updatable = false, nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private Currency currency;
	@JoinColumn(name = "ownerID", referencedColumnName = "id", updatable = false, nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private User owner;

	public BankBook() {
	}

	public BankBook(long balance, boolean freezed, Currency currency, User owner) {
		this.balance = balance;
		this.freezed = freezed;
		this.currency = currency;
		this.owner = owner;
	}

	public long getBalance() {
		return balance;
	}

	public void setBalance(long balance) {
		this.balance = balance;
	}

	public boolean isFreezed() {
		return freezed;
	}

	public void setFreezed(boolean freezed) {
		this.freezed = freezed;
	}

	public Currency getCurrency() {
		return currency;
	}

	public long getId() {
		return id;
	}

	public User getOwner() {
		return owner;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final BankBook other = (BankBook) obj;
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
		return "BankBook{" + "id=" + id + ", balance=" + balance + ", freezed=" + freezed + ", currency=" + currency + ", owner=" + owner + '}';
	}
}
