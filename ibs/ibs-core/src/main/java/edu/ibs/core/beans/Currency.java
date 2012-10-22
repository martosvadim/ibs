package edu.ibs.core.beans;

/**
 *
 * @author Vadim Martos
 */
public class Currency {

	public static final Currency DEFAULT_COUNTRY_CURRENCY = new Currency(1l, "by", 1f);
	private final long id;
	private final String name;
	private float factor;

	public Currency(long id, String name, float factor) {
		this.id = id;
		this.name = name;
		this.factor = factor;
	}

	public Currency(long id, String name) {
		this(id, name, -1f);
	}

	public float getFactor() {
		return factor;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setFactor(float factor) {
		this.factor = factor;
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
		if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 13 * hash + (int) (this.id ^ (this.id >>> 32));
		hash = 13 * hash + (this.name != null ? this.name.hashCode() : 0);
		return hash;
	}
}
