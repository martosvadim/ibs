package edu.ibs.common.enums;

import com.google.gwt.regexp.shared.RegExp;

/**
 *
 * @date Jan 12, 2013
 *
 * @author Vadim Martos
 */
public enum ProviderField {

	PHONE("phone"),
	NAME("name"),
	ADDRESS("address"),
	PASSPORT("passport");
	public static final String PASSPORT_NUMBER_REGEXP = "^[A-Z]{2}[0-9]{7}$";
	public static final String PHONE_REGEXP = "^[375]{3}[0-9]{9}$";
	public static final String NAME_REGEXP = "^[а-яА-Я]{2,32}[-]{0,1}[а-яА-Я]{2,32}$";
	public static final String ADDRESS_REGEXP = "^[а-яА-Я0-9., -]{2,64}$";
	public static final String VALID_PASSPORT_MSG = "номер паспорта должен содержать серия - две латинские буквы в верхнем регистре, и 7-значный номер";
	public static final String VALID_PHONE_MSG = "номер должен начинаться с 375, далее две цифры - код оператора, далее - семизначный номер";
	public static final String VALID_NAME_MSG = "имя может содержать любые символы кириллицы, и один дефис";
	public static final String VALID_ADDRESS_MSG = "адрес может содержать любые символы кириллицы, цифры, пробелы, запятые и точки";
//	private static final RegExp PASSPORT_PATTERN = RegExp.compile(PASSPORT_NUMBER_REGEXP);
//	private static final RegExp PHONE_PATTERN = RegExp.compile(PHONE_STRING_REGEXP);
//	private static final RegExp NAME_PATTERN = RegExp.compile(NAME_STRING_REGEXP);
//	private static final RegExp ADDRESS_PATTERN = RegExp.compile(ADDRESS_STRING_REGEXP);
	private String type;

	private ProviderField(String type) {
		this.type = type;
	}

	public String validPattern() {
		switch (this) {
			case ADDRESS: {
				return VALID_ADDRESS_MSG;
			}
			case NAME: {
				return VALID_NAME_MSG;
			}
			case PHONE: {
				return VALID_PHONE_MSG;
			}
			case PASSPORT: {
				return VALID_PASSPORT_MSG;
			}
			default: {
				return "";
			}
		}
	}

	public boolean validate(String value) {
		if (value == null || value.isEmpty() || value.trim().isEmpty()) {
			return false;
		}
		switch (this) {
			case ADDRESS: {
				if (value.startsWith("/s")) {
					return false;
				} else if (value.replaceAll("/s", "").isEmpty()) {
					return false;
				}
				return RegExp.compile(ADDRESS_REGEXP).test(value);
			}
			case NAME: {
				if (value.startsWith("/s")) {
					return false;
				} else if (value.replaceAll("/s", "").isEmpty()) {
					return false;
				}
				return RegExp.compile(NAME_REGEXP).test(value);
			}
			case PHONE: {
				return RegExp.compile(PHONE_REGEXP).test(value);
			}
			case PASSPORT: {
				return RegExp.compile(PASSPORT_NUMBER_REGEXP).test(value);
			}
			default: {
				return false;
			}
		}
	}

	@Override
	public String toString() {
		return type;
	}
}
