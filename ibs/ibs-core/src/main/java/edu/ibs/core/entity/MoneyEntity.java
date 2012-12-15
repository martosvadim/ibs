package edu.ibs.core.entity;

/**
 *
 * @author Vadim Martos @date Dec 14, 2012
 */
public interface MoneyEntity {

	public void subtract(Money other);

	public boolean lt(Money other);

	public boolean le(Money other);

	public boolean gt(Money other);

	public boolean ge(Money other);

	public void add(Money other);

	public Money getMoney();
}
