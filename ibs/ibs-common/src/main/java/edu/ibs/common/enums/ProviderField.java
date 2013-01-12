package edu.ibs.common.enums;

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
	private String type;

	private ProviderField(String type) {
		this.type = type;
	}

	public boolean validate(String value) {
		switch (this) {
			case ADDRESS: {
				return false;
			}
			case NAME: {
				return false;
			}
			case PHONE: {
				return false;
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
