package edu.ibs.core.entity;

import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Vadim Martos @date Oct 31, 2012
 */
@Entity
@Table(name = "Currency")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "Currency.findAll", query = "SELECT c FROM Currency c"),
	@NamedQuery(name = "Currency.findById", query = "SELECT c FROM Currency c WHERE c.id = :id"),
	@NamedQuery(name = "Currency.findByName", query = "SELECT c FROM Currency c WHERE c.name = :name"),
	@NamedQuery(name = "Currency.findByFactor", query = "SELECT c FROM Currency c WHERE c.factor = :factor")})
public class Currency implements Serializable, AbstractEntity {

	public static final long DEFAULT_COUNTRY_CURRENCY_ID = 1L;
	private static final long serialVersionUID = 78234520985323L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id", updatable = false, unique = true)
	private long id;
	@Basic(optional = false)
	@Column(name = "name", unique = true, updatable = false)
	private String name;
	@Basic(optional = false)
	@Column(name = "factor")
	private float factor;

	public Currency() {
	}

	public Currency(String name, float factor) {
		this.name = name;
		this.factor = factor;
	}

	public float getFactor() {
		return factor;
	}

	public void setFactor(float factor) {
		this.factor = factor;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Currency other = (Currency) obj;
		if (this.id != other.id) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 97 * hash + (int) (this.id ^ (this.id >>> 32));
		return hash;
	}
}