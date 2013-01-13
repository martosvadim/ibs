package edu.ibs.core.entity;

import edu.ibs.common.dto.UserDTO;
import edu.ibs.common.enums.ProviderField;
import static edu.ibs.common.enums.ProviderField.*;
import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Vadim Martos @date Oct 31, 2012
 */
@Entity
@Table(name = "User")
@XmlRootElement
public class User implements Serializable, AbstractEntity {

//	private static final Pattern PASSPORT_NUMBER_PATTERN = Pattern.compile(PASSPORT_NUMBER_REGEXP);
//	private static final Pattern PHONE_STRING_PATTERN = Pattern.compile(PHONE_STRING_REGEXP);
//	private static final Pattern NAME_STRING_PATTERN = Pattern.compile(NAME_STRING_REGEXP);
//	private static final Pattern ADDRESS_STRING_PATTERN = Pattern.compile(ADDRESS_STRING_REGEXP);
	private static final long serialVersionUID = 432949376663998234L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id", unique = true, updatable = false)
	private long id;
	@Basic(optional = false)
	@Column(name = "firstName")
	private String firstName;
	@Basic(optional = false)
	@Column(name = "lastName")
	private String lastName;
	@Basic(optional = false)
	@Column(name = "passportNumber")
	private String passportNumber;
	@Basic(optional = true)
	@Lob
	@Column(name = "passportScan", nullable = true)
	private byte[] passportScan;
	@Basic(optional = false)
	@Column(name = "freezed")
	private boolean freezed;
	@Basic(optional = true)
	@Column(name = "address")
	private String address;
	@Basic(optional = true)
	@Column(name = "zipCode")
	private Integer zipCode;
	@Basic(optional = true)
	@Column(name = "phone1")
	private BigInteger phone1;
	@Basic(optional = true)
	@Column(name = "phone2")
	private BigInteger phone2;
	@Basic(optional = true)
	@Column(name = "phone3")
	private BigInteger phone3;
	@Basic(optional = true)
	@Column(name = "description")
	private String description;

	public User() {
	}

	public User(String firstName, String lastName, String passportNumber) throws IllegalArgumentException {
		if (!User.validatePassportNumber(passportNumber)) {
			throw new IllegalArgumentException(String.format("Невалидный ноер паспорта %s, %s", passportNumber, VALID_PASSPORT_MSG));
		}
		if (!User.validateName(firstName)) {
			throw new IllegalArgumentException(String.format("Невалидное имя %s, %s", firstName, VALID_NAME_MSG));
		}
		if (!User.validateName(lastName)) {
			throw new IllegalArgumentException(String.format("Невалидное имя %s, %s", lastName, VALID_NAME_MSG));
		}
		this.firstName = firstName;
		this.lastName = lastName;
		this.passportNumber = passportNumber;
	}

	public User(UserDTO dto) throws IllegalArgumentException {
		this(dto.getFirstName(), dto.getLastName(), dto.getPassportNumber());
		setAddress(dto.getAddress());
		setDescription(dto.getDescription());
		setPhone1(dto.getPhone1());
		setPhone2(dto.getPhone2());
		setPhone3(dto.getPhone3());
		setPassportScan(dto.getPassportScan());
		setZipCode(dto.getZipCode());
		this.id = dto.getId();
	}

	public static boolean validatePassportNumber(String passportNumber) {
		return ProviderField.PASSPORT.validate(passportNumber);
	}

	public static boolean validateAddress(String address) {
		return ProviderField.ADDRESS.validate(address);
	}

	public static boolean validateName(String name) {
		return ProviderField.NAME.validate(name);
	}

	public static boolean validatePhone(String phone) {
		return ProviderField.PHONE.validate(phone);
	}

	public String getAddress() {
		return address;
	}

	public String getDescription() {
		return description;
	}

	public String getFirstName() {
		return firstName;
	}

	public boolean isFreezed() {
		return freezed;
	}

	public long getId() {
		return id;
	}

	public String getLastName() {
		return lastName;
	}

	public String getPassportNumber() {
		return passportNumber;
	}

	public byte[] getPassportScan() {
		return passportScan;
	}

	public BigInteger getPhone1() {
		return phone1;
	}

	public BigInteger getPhone2() {
		return phone2;
	}

	public BigInteger getPhone3() {
		return phone3;
	}

	public Integer getZipCode() {
		return zipCode;
	}

	public void setAddress(String address) {
		if (!User.validateAddress(address)) {
			throw new IllegalArgumentException(String.format("Невалидный адрес %s, %s", address, VALID_ADDRESS_MSG));
		}
		this.address = address;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setPassportScan(byte[] passportScan) {
		this.passportScan = passportScan;
	}

	public void setPhone1(BigInteger phone) {
		if (phone == null || !User.validatePhone(phone.toString())) {
			throw new IllegalArgumentException(String.format("Невалидный номер телефона %s, %s", phone, VALID_PHONE_MSG));
		}
		this.phone1 = phone;
	}

	public void setPhone2(BigInteger phone) {
		if (phone == null || !User.validatePhone(phone.toString())) {
			throw new IllegalArgumentException(String.format("Невалидный номер телефона %s, %s", phone, VALID_PHONE_MSG));
		}
		this.phone2 = phone;
	}

	public void setPhone3(BigInteger phone) {
		if (phone == null || !User.validatePhone(phone.toString())) {
			throw new IllegalArgumentException(String.format("Невалидный номер телефона %s, %s", phone, VALID_PHONE_MSG));
		}
		this.phone3 = phone;
	}

	public void setZipCode(Integer zipCode) {
		this.zipCode = zipCode;
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
		int hash = 5;
		hash = 61 * hash + (int) (this.id ^ (this.id >>> 32));
		return hash;
	}

	@Override
	public String toString() {
		return "User{" + "firstName=" + firstName + ", lastName=" + lastName + '}';
	}
}
