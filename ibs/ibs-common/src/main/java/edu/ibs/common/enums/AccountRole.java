package edu.ibs.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * User: EgoshinME
 * Date: 17.12.12
 * Time: 5:12
 */
public enum AccountRole {

	USER("user"),
	ADMIN("admin");

	private String name;

	private static final Map<String, AccountRole> nameLookUp
			= new HashMap<String, AccountRole>(AccountRole.values().length);

	private static final Map<Integer, AccountRole> ordinalLookUp
			= new HashMap<Integer, AccountRole>(AccountRole.values().length);

	static {
		for (AccountRole role : AccountRole.values()) {
			nameLookUp.put(role.toString(), role);
			ordinalLookUp.put(role.ordinal(), role);
		}
	}

	private AccountRole(String name) {
		this.name = name;
	}

	public static AccountRole forName(String name) {
		return nameLookUp.get(name);
	}

	public static AccountRole forOrdinal(int ordinal) {
		return ordinalLookUp.get(Integer.valueOf(ordinal));
	}

	@Override
	public String toString() {
		return this.name;
	}
}