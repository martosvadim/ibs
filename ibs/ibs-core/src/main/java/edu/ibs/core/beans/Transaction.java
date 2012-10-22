package edu.ibs.core.beans;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Vadim Martos
 */
public class Transaction {

	private final long id;
	private final long date;
	private final BankBook from;
	private final BankBook to;
	private final Currency currency;
	private final long amount;
	private final Type type;

	public Transaction(long id, long date, BankBook from, BankBook to, Currency currency, long amount, Type type) {
		this.id = id;
		this.date = date;
		this.from = from;
		this.to = to;
		this.currency = currency;
		this.amount = amount;
		this.type = type;
	}

	public static enum Type {

		PAYMENT,
		TRANSFER;
		private static final Map<String, Type> nameLookUp = new HashMap<String, Type>(Type.values().length);
		private static final Map<Integer, Type> ordinalLookUp = new HashMap<Integer, Type>(Type.values().length);

		static {
			for (Type type : Type.values()) {
				nameLookUp.put(type.toString(), type);
				ordinalLookUp.put(type.ordinal(), type);
			}
		}

		public static Type forName(String name) {
			return nameLookUp.get(name);
		}

		public static Type forOrdinal(int ordinal) {
			return ordinalLookUp.get(Integer.valueOf(ordinal));
		}

		@Override
		public String toString() {
			return this.name().toLowerCase();
		}
	}
}
