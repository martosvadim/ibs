package edu.ibs.common.enums;

import java.util.regex.Pattern;

/**
 *
 * @date Jan 12, 2013
 *
 * @author Vadim Martos
 */
public enum ProviderField {

	PHONE("phone"),
	NAME("name"),
	ADDRESS("address");
	private static final String PHONE_STRING_REGEXP = "^[375]{3}[0-9]{9}$";
	private static final Pattern PHONE_STRING_PATTERN = Pattern.compile(PHONE_STRING_REGEXP);
	private static final String NAME_STRING_REGEXP = "[а-яА-Я]{2,32}[-]{0,1}[а-яА-Я]{2,32}";
	private static final Pattern NAME_STRING_PATTERN = Pattern.compile(NAME_STRING_REGEXP);
	private static final String ADDRESS_STRING_REGEXP = "[\\pL0-9., -]{2,64}";
	private static final Pattern ADDRESS_STRING_PATTERN = Pattern.compile(ADDRESS_STRING_REGEXP);
	private String type;

	private ProviderField(String type) {
		this.type = type;
	}

	public String validPattern() {
		switch (this) {
			case ADDRESS: {
				return "Адрес может содержать любые символы кириллицы, цифры, пробелы, запятые и точки";
			}
			case NAME: {
				return "Имя может содержать любые символы кириллицы, и один дефис";
			}
			case PHONE: {
				return "Номер должен начинаться с 375, далее две цифры - код оператора, далее - семизначный номер";
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
				return ADDRESS_STRING_PATTERN.matcher(value).matches();
			}
			case NAME: {
				if (value.startsWith("/s")) {
					return false;
				} else if (value.replaceAll("/s", "").isEmpty()) {
					return false;
				}
				return NAME_STRING_PATTERN.matcher(value).matches();
			}
			case PHONE: {
				return PHONE_STRING_PATTERN.matcher(value).matches();
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
