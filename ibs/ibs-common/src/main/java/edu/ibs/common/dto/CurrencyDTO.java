package edu.ibs.common.dto;

import edu.ibs.common.enums.Fraction;

/**
 * User: EgoshinME
 * Date: 17.12.12
 * Time: 5:15
 */
public class CurrencyDTO implements IBaseDTO {
	private long id;
	private String name;
	private float factor;
	private Fraction fraction;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getFactor() {
		return factor;
	}

	public void setFactor(float factor) {
		this.factor = factor;
	}

	public Fraction getFraction() {
		return fraction;
	}

	public void setFraction(Fraction fraction) {
		this.fraction = fraction;
	}
}
