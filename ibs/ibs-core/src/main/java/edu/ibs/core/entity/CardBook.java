package edu.ibs.core.entity;

import edu.ibs.common.enums.CardBookType;
import java.io.Serializable;
import java.util.Random;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @date Dec 13, 2012
 *
 * @author Vadim Martos
 */
@Entity
@Table(name = "CardBook")
@XmlRootElement
public class CardBook implements Serializable, AbstractEntity {

	private static final long serialVersionUID = 11234125367622134L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id", updatable = false, unique = true)
	private long id;
	@Basic(optional = false)
	@Column(name = "type", updatable = false, nullable = false)
	@Enumerated(EnumType.STRING)
	private CardBookType type;
	@Basic(optional = false)
	@Column(name = "dateExpire", updatable = false, nullable = false)
	private long dateExpire;
	@Basic(optional = false)
	@Column(name = "freezed", nullable = false)
	private boolean freezed;
	@Basic(optional = false)
	@Column(name = "pin", updatable = false, nullable = false)
	private int pin;
	@JoinColumn(name = "bankBookID", referencedColumnName = "id", updatable = false, nullable = false)
	@ManyToOne(optional = false)
	private BankBook bankBook;
	@JoinColumn(name = "creditID", referencedColumnName = "id", updatable = false)
	@OneToOne(optional = true)
	private Credit credit;
	@JoinColumn(name = "ownerID", referencedColumnName = "id", updatable = true)
	@OneToOne(optional = false)
	private User owner;

	public CardBook() {
	}

	private CardBook(CardBookType type, BankBook bankBook, Credit credit) {
		this.type = type;
		this.dateExpire = generateExpireDate();
		this.freezed = false;
		this.pin = generatePin();
		this.bankBook = bankBook;
		this.credit = credit;
		this.owner = bankBook.getOwner();
	}

	public CardBook(BankBook bankBook) {
		this(CardBookType.DEBIT, bankBook, null);
	}

	public CardBook(BankBook bankBook, Credit credit) {
		this(CardBookType.CREDIT, bankBook, credit);
	}

	@Override
	public long getId() {
		return id;
	}

	public BankBook getBankBook() {
		return bankBook;
	}

	public Credit getCredit() {
		return credit;
	}

	public long getDateExpire() {
		return dateExpire;
	}

	public boolean isFreezed() {
		return freezed;
	}

	public int getPin() {
		return pin;
	}

	public CardBookType getType() {
		return type;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User user) {
		this.owner = user;
	}

	public void setFreezed(boolean freezed) {
		this.freezed = freezed;
	}

	public Money availableMoney() {
		switch (type) {
			case CREDIT: {
				return credit.getMoney();
			}
			case DEBIT: {
				return bankBook.getMoney();
			}
			default: {
				return null;
			}
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final CardBook other = (CardBook) obj;
		if (this.id != other.id) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 19 * hash + (int) (this.id ^ (this.id >>> 32));
		return hash;
	}

	@Override
	public String toString() {
		return "CardBook{" + "id=" + id + ", type=" + type + ", dateExpire=" + dateExpire + ", freezed=" + freezed + ", pin=" + pin + ", bankBook=" + bankBook + ", credit=" + credit + '}';
	}

	public static int generatePin() {
		return (new Random().nextInt(10000) + 1000) % 10000;
	}

	public static long generateExpireDate() {
		return System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 365; //a year
	}
}
