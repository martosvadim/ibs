package edu.ibs.core;

import edu.ibs.core.controller.CSUIDJpaController;
import edu.ibs.core.entity.BankBook;
import edu.ibs.core.entity.Currency;
import edu.ibs.core.entity.Money;
import edu.ibs.core.entity.User;
import org.apache.log4j.PropertyConfigurator;

/**
 * Hello world!
 *
 */
public class App {

	static {
		PropertyConfigurator.configure(App.class.getClassLoader().getResource("log4j.properties"));
	}

	public static void main(String[] args) throws Exception {
		CSUIDJpaController jpa = new CSUIDJpaController();
		BankBook b = jpa.select(BankBook.class, 1l);
		User u = new User(User.Role.USER, "vadim.martos@gmail.com", "asd");
		jpa.insert(u);
		Currency curr = new Currency("USD", 8350f, Currency.Fraction.TWO);
		jpa.insert(curr);
		Money money = new Money(100, curr);
		BankBook book = new BankBook(money, u, false);
		jpa.insert(book);
		int i = 0;
	}
}
