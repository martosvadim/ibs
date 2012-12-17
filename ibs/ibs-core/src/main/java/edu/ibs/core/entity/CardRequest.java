package edu.ibs.core.entity;

import edu.ibs.common.enums.CardBookType;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 *
 * @date Dec 13, 2012
 *
 * @author Vadim Martos
 */
@Entity
@Table(name = "CardRequest")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "CardRequest.findAll", query = "SELECT c FROM CardRequest c"),
	@NamedQuery(name = "CardRequest.findById", query = "SELECT c FROM CardRequest c WHERE c.id = :id"),
	@NamedQuery(name = "CardRequest.findByType", query = "SELECT c FROM CardRequest c WHERE c.type = :type"),
	@NamedQuery(name = "CardRequest.findByDateCreated", query = "SELECT c FROM CardRequest c WHERE c.dateCreated = :dateCreated"),
	@NamedQuery(name = "CardRequest.findByDateWatched", query = "SELECT c FROM CardRequest c WHERE c.dateWatched = :dateWatched"),
	@NamedQuery(name = "CardRequest.findByApproved", query = "SELECT c FROM CardRequest c WHERE c.approved = :approved"),
	@NamedQuery(name = "CardRequest.findByReason", query = "SELECT c FROM CardRequest c WHERE c.reason = :reason")})
public class CardRequest implements Serializable, AbstractEntity {

	private static final long serialVersionUID = 791271095876512309L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id", nullable = false, updatable = false, unique = true)
	private long id;
	@JoinColumn(name = "userID", referencedColumnName = "id", updatable = false, nullable = false)
	@ManyToOne(optional = false)
	private User user;
	@JoinColumn(name = "bankBookID", referencedColumnName = "id", updatable = false, nullable = false)
	@OneToOne(optional = true)
	private BankBook bankBook;
	@Basic(optional = false)
	@Column(name = "type", updatable = false, nullable = false)
	@Enumerated(EnumType.STRING)
	private CardBookType type;
	@Basic(optional = false)
	@Column(name = "dateCreated", updatable = false, nullable = false)
	private long dateCreated;
	@Column(name = "dateWatched")
	private long dateWatched;
	@Basic(optional = false)
	@Column(name = "approved", nullable = true)
	private boolean approved;
	@Column(name = "reason")
	private String reason;
	@JoinColumn(name = "creditPlanID", referencedColumnName = "id", updatable = false)
	@ManyToOne(optional = true)
	private CreditPlan plan;
	@JoinColumn(name = "cardBookID", referencedColumnName = "id", updatable = true)
	@OneToOne(optional = true)
	private CardBook cardBook;

	public CardRequest() {
	}

	private CardRequest(User user, BankBook bankBook, CardBookType type, CreditPlan plan) {
		this.user = user;
		this.bankBook = bankBook;
		this.type = type;
		this.plan = plan;
		this.approved = false;
		this.dateCreated = System.currentTimeMillis();
	}

	public CardRequest(User user, BankBook bankBook, CreditPlan plan) {
		this(user, bankBook, CardBookType.CREDIT, plan);
	}

	public CardRequest(User user, BankBook bankBook) {
		this(user, bankBook, CardBookType.DEBIT, null);
	}

	public boolean isApproved() {
		return approved;
	}

	public BankBook getBankBook() {
		return bankBook;
	}

	public long getDateCreated() {
		return dateCreated;
	}

	public long getDateWatched() {
		return dateWatched;
	}

	@Override
	public long getId() {
		return id;
	}

	public CreditPlan getPlan() {
		return plan;
	}

	public CardBook getCardBook() {
		return cardBook;
	}

	public String getReason() {
		return reason;
	}

	public CardBookType getType() {
		return type;
	}

	public User getUser() {
		return user;
	}

	public void approve(CardBook cardBook) {
		this.cardBook = cardBook;
		this.approved = true;
		this.dateWatched = System.currentTimeMillis();
	}

	public void decline(String reason) {
		this.approved = false;
		this.reason = reason;
		this.dateWatched = System.currentTimeMillis();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final CardRequest other = (CardRequest) obj;
		if (this.id != other.id) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 61 * hash + (int) (this.id ^ (this.id >>> 32));
		return hash;
	}

	@Override
	public String toString() {
		return "CardRequest{" + "id=" + id + ", user=" + user + ", bankBook=" + bankBook + ", type=" + type + ", dateCreated=" + dateCreated + ", dateWatched=" + dateWatched + ", approved=" + approved + ", reason=" + reason + ", plan=" + plan + ", plancardBook=" + cardBook + '}';
	}
}
