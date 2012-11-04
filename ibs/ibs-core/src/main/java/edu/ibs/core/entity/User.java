package edu.ibs.core.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Vadim Martos @date Oct 31, 2012
 */
@Entity
@Table(name = "User")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "User.findAll", query = "SELECT u FROM User u"),
	@NamedQuery(name = "User.findById", query = "SELECT u FROM User u WHERE u.id = :id"),
	@NamedQuery(name = "User.findByRole", query = "SELECT u FROM User u WHERE u.role = :role"),
	@NamedQuery(name = "User.findByFirstName", query = "SELECT u FROM User u WHERE u.firstName = :firstName"),
	@NamedQuery(name = "User.findByLastName", query = "SELECT u FROM User u WHERE u.lastName = :lastName"),
	@NamedQuery(name = "User.findByEmail", query = "SELECT u FROM User u WHERE u.email = :email"),
	@NamedQuery(name = "User.findByDescription", query = "SELECT u FROM User u WHERE u.description = :description")})
public class User implements Serializable, AbstractEntity {

	private static final long serialVersionUID = 432949376663998234L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id", unique = true, updatable = false)
	private long id;
	@Basic(optional = false)
	@Column(name = "role")
	@Enumerated(EnumType.STRING)
	private Role role;
	@Column(name = "firstName")
	private String firstName;
	@Column(name = "lastName")
	private String lastName;
	@Basic(optional = false)
	@Column(name = "email", unique = true, updatable = false)
	private String email;
	@Column(name = "description")
	private String description;

	public User() {
	}

	public User(Role role, String email) {
		this.role = role;
		this.email = email;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public long getId() {
		return id;
	}

	public Role getRole() {
		return role;
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
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 83 * hash + (int) (this.id ^ (this.id >>> 32));
		return hash;
	}

	@Override
	public String toString() {
		return "User{" + "id=" + id + ", role=" + role + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email + ", description=" + description + '}';
	}

	public static enum Role {

		USER("user"),
		ADMIN("admin");
		private String name;
		private static final Map<String, Role> nameLookUp = new HashMap<String, Role>(Role.values().length);
		private static final Map<Integer, Role> ordinalLookUp = new HashMap<Integer, Role>(Role.values().length);

		static {
			for (Role role : Role.values()) {
				nameLookUp.put(role.toString(), role);
				ordinalLookUp.put(role.ordinal(), role);
			}
		}

		private Role(String name) {
			this.name = name;
		}

		public static Role forName(String name) {
			return nameLookUp.get(name);
		}

		public static Role forOrdinal(int ordinal) {
			return ordinalLookUp.get(Integer.valueOf(ordinal));
		}

		@Override
		public String toString() {
			return this.name;
		}
	}
}
