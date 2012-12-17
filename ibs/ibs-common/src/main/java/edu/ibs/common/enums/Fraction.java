package edu.ibs.common.enums;

/**
 *
 * Contains information about how much digits fraction part of currency
 * should have.
 *
 * User: EgoshinME
 * Date: 17.12.12
 * Time: 5:16
 */
public enum Fraction {

	ZERO(1),
	ONE(10),
	TWO(100),
	THREE(1000);
	private int digit;

	private Fraction(int digit) {
		this.digit = digit;
	}

	public int multiply() {
		return digit;
	}
}
