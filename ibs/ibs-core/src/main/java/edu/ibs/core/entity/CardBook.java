package edu.ibs.core.entity;

import edu.ibs.common.enums.CardBookType;
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
@Table(name = "CardBook")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "CardBook.findAll", query = "SELECT c FROM CardBook c"),
	@NamedQuery(name = "CardBook.findById", query = "SELECT c FROM CardBook c WHERE c.id = :id"),
	@NamedQuery(name = "CardBook.findByType", query = "SELECT c FROM CardBook c WHERE c.type = :type"),
	@NamedQuery(name = "CardBook.findByDateExpire", query = "SELECT c FROM CardBook c WHERE c.dateExpire = :dateExpire"),
	@NamedQuery(name = "CardBook.findByFreezed", query = "SELECT c FROM CardBook c WHERE c.freezed = :freezed"),
	@NamedQuery(name = "CardBook.findByPin", query = "SELECT c FROM CardBook c WHERE c.pin = :pin")})
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
	private String pin;
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

	private CardBook(CardBookType type, long dateExpire, boolean freezed, String pin, BankBook bankBook, Credit credit) {
		this.type = type;
		this.dateExpire = dateExpire;
		this.freezed = freezed;
		this.pin = pin;
		this.bankBook = bankBook;
		this.credit = credit;
		this.owner = bankBook.getOwner();
	}

	public CardBook(BankBook bankBook, long dateExpire, String pin) {
		this(CardBookType.DEBIT, dateExpire, false, pin, bankBook, null);
	}

	public CardBook(BankBook bankBook, Credit credit, long dateExpire, String pin) {
		this(CardBookType.CREDIT, dateExpire, false, pin, bankBook, credit);
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

	public String getPin() {
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
}
