package edu.ibs.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * User: EgoshinME Date: 17.12.12 Time: 5:24
 */
public enum Period {

	DAY("day"),
	WEEK("week"),
	MONTH("month"),
	YEAR("year");
	private static final Map<String, Period> nameLookUp = new HashMap<String, Period>(Period.values().length);
	private static final long DAY_IN_MS = 1000 * 60 * 60 * 24L;
	private static final long WEEK_IN_MS = DAY_IN_MS * 7L;
	private static final long MONTH_IN_MS = DAY_IN_MS * 31L;
	private static final long YEAR_IN_MS = DAY_IN_MS * 365L;
	private final String name;

	static {
		for (Period type : Period.values()) {
			nameLookUp.put(type.toString(), type);
		}
	}

	private Period(String name) {
		this.name = name;
	}

	public static Period forName(String name) {
		return nameLookUp.get(name);
	}

	public static long getMilliseconds(Period p) {
		switch (p) {
			case DAY: {
				return DAY_IN_MS;
			}
			case WEEK: {
				return WEEK_IN_MS;
			}
			case MONTH: {
				return MONTH_IN_MS;
			}
			case YEAR: {
				return YEAR_IN_MS;
			}
			default: {
				return 0L;
			}
		}
	}

	public static long calculatePeriod(Period p, int multiply) {
		return getMilliseconds(p) * multiply;
	}

	@Override
	public String toString() {
		return this.name;
	}
}
