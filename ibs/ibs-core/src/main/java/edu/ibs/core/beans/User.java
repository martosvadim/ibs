package edu.ibs.core.beans;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Vadim Martos
 */
public class User {

	private final long id;
	private final Role role;
	private String firstName;
	private String lastName;
	private final String email;
	private String description;

	public User(long id, Role role, String firstName, String lastName, String email, String description) {
		this.id = id;
		this.role = role;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.description = description;
	}

	public User(long id, Role role, String email) {
		this(id, role, null, null, email, null);
	}

	public String getDescription() {
		return description;
	}

	public String getEmail() {
		return email;
	}

	public String getFirstName() {
		return firstName;
	}

	public long getId() {
		return id;
	}

	public String getLastName() {
		return lastName;
	}

	public Role getRole() {
		return role;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final User other = (User) obj;
		if (this.id != other.id) {
			return false;
		}
		if (this.role != other.role) {
			return false;
		}
		if ((this.email == null) ? (other.email != null) : !this.email.equals(other.email)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 17 * hash + (int) (this.id ^ (this.id >>> 32));
		hash = 17 * hash + (this.role != null ? this.role.hashCode() : 0);
		hash = 17 * hash + (this.email != null ? this.email.hashCode() : 0);
		return hash;
	}

	public static enum Role {

		USER,
		ADMIN;
		private static final Map<String, Role> nameLookUp = new HashMap<String, Role>(Role.values().length);
		private static final Map<Integer, Role> ordinalLookUp = new HashMap<Integer, Role>(Role.values().length);

		static {
			for (Role role : Role.values()) {
				nameLookUp.put(role.toString(), role);
				ordinalLookUp.put(role.ordinal(), role);
			}
		}

		public static Role forName(String name) {
			return nameLookUp.get(name);
		}

		public static Role forOrdinal(int ordinal) {
			return ordinalLookUp.get(Integer.valueOf(ordinal));
		}

		@Override
		public String toString() {
			return name().toLowerCase();
		}
	}
}
