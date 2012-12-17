package edu.ibs.common.dto;

import java.math.BigInteger;

/**
 * Пользователь
 * @author EgoshinME
 */
public class UserDTO implements IBaseDTO {

	/**
	 * Идентификатор
	 */
	private long id;
    /**
     * Имя
     */
    private String firstName;
    /**
     * Фамилия
     */
    private String lastName;
    /**
     * Почтовый адрес
     */
    private String email;
    /**
     * Данные
     */
    private String description;
	/**
	 * Номер паспорта
	 */
	private String passportNumber;
	/**
	 * Скан паспорта
	 */
	private byte[] passportScan;
	/**
	 * Адрес
	 */
	private String address;
	/**
	 * Почтовый код
	 */
	private int zipCode;
	/**
	 * Телефон
	 */
	private BigInteger phone1;
	/**
	 * Телефон
	 */
	private BigInteger phone2;
	/**
	 * Телефон
	 */
	private BigInteger phone3;
	/**
	 * wtf ?
	 */
	private boolean freezed;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public String getPassportNumber() {
		return passportNumber;
	}

	public void setPassportNumber(final String passportNumber) {
		this.passportNumber = passportNumber;
	}

	public byte[] getPassportScan() {
		return passportScan;
	}

	public void setPassportScan(final byte[] passportScan) {
		this.passportScan = passportScan;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(final String address) {
		this.address = address;
	}

	public int getZipCode() {
		return zipCode;
	}

	public void setZipCode(final int zipCode) {
		this.zipCode = zipCode;
	}

	public BigInteger getPhone1() {
		return phone1;
	}

	public void setPhone1(final BigInteger phone1) {
		this.phone1 = phone1;
	}

	public BigInteger getPhone2() {
		return phone2;
	}

	public void setPhone2(final BigInteger phone2) {
		this.phone2 = phone2;
	}

	public BigInteger getPhone3() {
		return phone3;
	}

	public void setPhone3(final BigInteger phone3) {
		this.phone3 = phone3;
	}

	public boolean isFreezed() {
		return freezed;
	}

	public void setFreezed(final boolean freezed) {
		this.freezed = freezed;
	}

	public long getId() {
		return id;
	}

	public void setId(final long id) {
		this.id = id;
	}
}
