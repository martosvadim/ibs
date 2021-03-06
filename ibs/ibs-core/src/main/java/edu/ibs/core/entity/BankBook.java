package edu.ibs.core.entity;

import edu.ibs.common.dto.BankBookDTO;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * @date Oct 31, 2012
 *
 * @author Vadim Martos
 */
@Entity
@Table(name = "BankBook")
@XmlRootElement
public class BankBook implements Serializable, AbstractEntity, MoneyEntity {

	private static final long serialVersionUID = 7689532939542907L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id", updatable = false, unique = true)
	private long id;
	@Basic(optional = false)
	@Column(name = "balance", nullable = false)
	private long balance;
	@Basic(optional = false)
	@Column(name = "freezed", nullable = false)
	private boolean freezed;
	@JoinColumn(name = "currencyID", referencedColumnName = "id", updatable = false, nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private Currency currency;
	@JoinColumn(name = "ownerID", referencedColumnName = "id", updatable = false, nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private User owner;
	@Basic(optional = false)
	@Column(name = "dateExpire")
	private long dateExpire;
	@Column(name = "description")
	private String description;
	@Transient
	private volatile Money money;

	public BankBook() {
	}

	public BankBook(Money money, User owner, boolean freezed) {
		this.money = money;
		this.balance = money.balance();
		this.freezed = freezed;
		this.currency = money.currency();
		this.owner = owner;
	}

	public BankBook(User owner, Money money) {
		this(money, owner, false);
	}

    public BankBook(BankBookDTO bankBookDTO) {
        this(new User(bankBookDTO.getOwner()), new Money(0, new Currency(bankBookDTO.getCurrency())));
        this.id = bankBookDTO.getId();
    }

    private void validateMoney() {
		if (this.money == null) {
			this.money = new Money(this.balance, this.currency);
		}
	}

	public void subtract(Money other) {
		validateMoney();
		this.money = money.subtract(other);
		this.balance = money.balance();
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

	public void add(Money other) {
		validateMoney();
		this.money = money.add(other);
		this.balance = money.balance();
	}

	public Money getMoney() {
		validateMoney();
		return money;
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

	@Override
	public long getId() {
		return id;
	}

	public User getOwner() {
		return owner;
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

	public void copyFrom(BankBook other) {
		this.id = other.id;
		this.balance = other.balance;
		this.currency = other.currency;
		this.freezed = other.freezed;
		this.owner = other.owner;
		this.money = new Money(balance, currency);
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
