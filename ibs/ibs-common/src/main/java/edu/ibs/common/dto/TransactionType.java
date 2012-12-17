package edu.ibs.common.dto;

import java.util.HashMap;
import java.util.Map;

/**
 * User: EgoshinME
 * Date: 17.12.12
 * Time: 5:28
 */
public enum TransactionType {
	PAYMENT("payment"),
	TRANSFER("transfer");

	private String name;

	private static final Map<String, TransactionType> nameLookUp
			= new HashMap<String, TransactionType>(TransactionType.values().length);

	private static final Map<Integer, TransactionType> ordinalLookUp
			= new HashMap<Integer, TransactionType>(TransactionType.values().length);

	static {
		for (TransactionType type : TransactionType.values()) {
			nameLookUp.put(type.toString(), type);
			ordinalLookUp.put(type.ordinal(), type);
		}
	}

	private TransactionType(String name) {
		this.name = name;
	}

	public static TransactionType forName(String name) {
		return nameLookUp.get(name);
	}

	public static TransactionType forOrdinal(int ordinal) {
		return ordinalLookUp.get(Integer.valueOf(ordinal));
	}

	@Override
	public String toString() {
		return this.name;
	}
}
