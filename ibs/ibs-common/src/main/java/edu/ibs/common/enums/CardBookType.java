package edu.ibs.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * User: EgoshinME
 * Date: 17.12.12
 * Time: 5:20
 */
public enum CardBookType {
	DEBIT("debit"),
	CREDIT("credit");
	private String name;
	private static final Map<String, CardBookType> nameLookUp = new HashMap<String, CardBookType>(CardBookType.values().length);
	private static final Map<Integer, CardBookType> ordinalLookUp = new HashMap<Integer, CardBookType>(CardBookType.values().length);

	static {
		for (CardBookType type : CardBookType.values()) {
			nameLookUp.put(type.toString(), type);
			ordinalLookUp.put(type.ordinal(), type);
		}
	}

	private CardBookType(String name) {
		this.name = name;
	}

	public static CardBookType forName(String name) {
		return nameLookUp.get(name);
	}

	public static CardBookType forOrdinal(int ordinal) {
		return ordinalLookUp.get(Integer.valueOf(ordinal));
	}

	@Override
	public String toString() {
		return this.name;
	}
}
