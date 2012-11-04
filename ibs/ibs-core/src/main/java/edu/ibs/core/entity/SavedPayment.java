package edu.ibs.core.entity;

import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Vadim Martos @date Oct 31, 2012
 */
@Entity
@Table(name = "SavedPayment")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "SavedPayment.findAll", query = "SELECT s FROM SavedPayment s"),
	@NamedQuery(name = "SavedPayment.findById", query = "SELECT s FROM SavedPayment s WHERE s.id = :id")})
public class SavedPayment implements Serializable, AbstractEntity {

	private static final long serialVersionUID = 12154345127589623L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id", unique = true, updatable = false)
	private long id;
	@JoinColumn(name = "transactionID", referencedColumnName = "id", nullable = false, updatable = false)
	@ManyToOne(optional = false)
	private Transaction transaction;
	@JoinColumn(name = "userID", referencedColumnName = "id", nullable = false, updatable = false)
	@ManyToOne(optional = false)
	private User user;

	public SavedPayment() {
	}

	public SavedPayment(Transaction transaction, User user) {
		this.transaction = transaction;
		this.user = user;
	}

	public long getId() {
		return id;
	}

	public Transaction getTransaction() {
		return transaction;
	}

	public User getUser() {
		return user;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final SavedPayment other = (SavedPayment) obj;
		if (this.id != other.id) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 53 * hash + (int) (this.id ^ (this.id >>> 32));
		return hash;
	}

	@Override
	public String toString() {
		return "SavedPayment{" + "id=" + id + ", transaction=" + transaction + ", user=" + user + '}';
	}
}
