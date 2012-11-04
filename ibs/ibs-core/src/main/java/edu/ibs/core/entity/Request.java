package edu.ibs.core.entity;

import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Vadim Martos @date Oct 31, 2012
 */
@Entity
@Table(name = "Request")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "Request.findAll", query = "SELECT r FROM Request r"),
	@NamedQuery(name = "Request.findById", query = "SELECT r FROM Request r WHERE r.id = :id"),
	@NamedQuery(name = "Request.findByDate", query = "SELECT r FROM Request r WHERE r.date > :from AND r.date < :to"),
	@NamedQuery(name = "Request.findByInfo", query = "SELECT r FROM Request r WHERE r.info = :info")})
public class Request implements Serializable, AbstractEntity {

	private static final long serialVersionUID = 1320952348912323L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id", unique = true, updatable = false)
	private long id;
	@Basic(optional = false)
	@Column(name = "date")
	private long date;
	@JoinColumn(name = "ownerID", referencedColumnName = "id", updatable = false, nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private User fromUser;
	@Column(name = "info")
	private String info;

	public Request() {
	}

	public Request(User fromUser, String info, long date) {
		this.fromUser = fromUser;
		this.info = info;
		this.date = date;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public long getDate() {
		return date;
	}

	public long getId() {
		return id;
	}

	public User getOwner() {
		return fromUser;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Request other = (Request) obj;
		if (this.id != other.id) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 17 * hash + (int) (this.id ^ (this.id >>> 32));
		return hash;
	}

	@Override
	public String toString() {
		return "Request{" + "id=" + id + ", date=" + date + ", info=" + info + '}';
	}
}
