package edu.ibs.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * User: EgoshinME
 * Date: 17.12.12
 * Time: 5:24
 */
public enum Period {
	DAY("day"),
	WEEK("week"),
	MONTH("month"),
	YEAR("year");
	private static final Map<String, Period> nameLookUp = new HashMap<String, Period>(Period.values().length);
	private static final Map<Integer, Period> ordinalLookUp = new HashMap<Integer, Period>(Period.values().length);
	private final String name;

	static {
		for (Period type : Period.values()) {
			nameLookUp.put(type.toString(), type);
			ordinalLookUp.put(type.ordinal(), type);
		}
	}

	private Period(String name) {
		this.name = name;
	}

	public static Period forName(String name) {
		return nameLookUp.get(name);
	}

	public static Period forOrdinal(int ordinal) {
		return ordinalLookUp.get(Integer.valueOf(ordinal));
	}

	@Override
	public String toString() {
		return this.name;
	}
}
