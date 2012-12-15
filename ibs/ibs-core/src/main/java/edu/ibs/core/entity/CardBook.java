package edu.ibs.core.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
	@NotNull
	@Column(name = "id")
	private long id;
	@Basic(optional = false)
	@Column(name = "type", updatable = false)
	@Enumerated(EnumType.STRING)
	private CardBookType type;
	@Basic(optional = false)
	@NotNull
	@Column(name = "dateExpire", updatable = false)
	private long dateExpire;
	@Basic(optional = false)
	@NotNull
	@Column(name = "freezed")
	private boolean freezed;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 255)
	@Column(name = "pin", updatable = false)
	private String pin;
	@JoinColumn(name = "bankBookID", referencedColumnName = "id", updatable = false)
	@ManyToOne(optional = false)
	private BankBook bankBook;
	@JoinColumn(name = "creditID", referencedColumnName = "id", updatable = false)
	@OneToOne(optional = true)
	private Credit credit;

	public CardBook() {
	}

	private CardBook(CardBookType type, long dateExpire, boolean freezed, String pin, BankBook bankBook, Credit credit) {
		this.type = type;
		this.dateExpire = dateExpire;
		this.freezed = freezed;
		this.pin = pin;
		this.bankBook = bankBook;
		this.credit = credit;
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

	public static enum CardBookType {

		DEBIT("debit"),
		CREDIT("credit");
		private String name;
		private static final Map<String, CardBookType> nameLookUp = new HashMap<String, CardBookType>(CardBookType.values().length);
		private static final Map<Integer, CardBookType> ordinalLookUp = new HashMap<Integer, CardBookType>(CardBookType.values().length);

		static {
			for (CardBookType type : CardBookType.values()) {
				nameLookUp.put(type.toString(), type);
				ordinalLookUp.put(type.ordinal(), type);
			}
		}

		private CardBookType(String name) {
			this.name = name;
		}

		public static CardBookType forName(String name) {
			return nameLookUp.get(name);
		}

		public static CardBookType forOrdinal(int ordinal) {
			return ordinalLookUp.get(Integer.valueOf(ordinal));
		}

		@Override
		public String toString() {
			return this.name;
		}
	}
}
