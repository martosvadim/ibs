package edu.ibs.core.controller;

import edu.ibs.core.entity.Transaction.TransactionType;
import edu.ibs.core.entity.*;
import edu.ibs.core.entity.User.Role;
import static org.junit.Assert.*;
import org.junit.*;

/**
 *
 * @author vadim
 */
public class SpecifiedJpaControllerTest {

	private static final SpecifiedJpaController controller = SpecifiedJpaController.instance();
	private User user;
	private Currency usd, eur;
	private BankBook from, to;

	public SpecifiedJpaControllerTest() {
	}

	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
		controller.close();
	}

	@Before
	public void setUp() {
		user = new User(Role.USER, "user", "passwd");
		controller.insert(user);
		usd = new Currency("usd", 1.5f, Currency.Fraction.TWO);
		controller.insert(usd);
		eur = new Currency("eur", 3f, Currency.Fraction.TWO);
		controller.insert(eur);
		Money m1 = new Money(10000, usd);
		from = new BankBook(m1, user, false);
		controller.insert(from);
		Money m2 = new Money(10000, eur);
		to = new BankBook(m2, user, false);
		controller.insert(to);
	}

	@After
	public void tearDown() throws Exception {
		try {
			controller.delete(BankBook.class, from.getId());
		} catch (Throwable ignore) {
		}
		try {
			controller.delete(BankBook.class, to.getId());
		} catch (Throwable ignore) {
		}
		try {
			controller.delete(Currency.class, usd.getId());
		} catch (Throwable ignore) {
		}
		try {
			controller.delete(Currency.class, eur.getId());
		} catch (Throwable ignore) {
		}
		try {
			controller.delete(User.class, user.getId());
		} catch (Throwable ignore) {
		}
	}

	@Test
	public void payTest() throws Exception {
		Money money = new Money(5000, to.getCurrency());
		Transaction tr = controller.pay(from, to, money, TransactionType.PAYMENT);
		assertNotNull(tr);
		long tid = tr.getId();
		assertEquals(controller.select(BankBook.class, from.getId()).getMoney().integer(), 0);
		assertEquals(controller.select(BankBook.class, to.getId()).getMoney().integer(), 150);
		tr = controller.pay(from, to, money, TransactionType.PAYMENT);
		assertNull(tr);
		controller.delete(Transaction.class, tid);
	}

	@Test
	public void rollbackTest() throws Exception {
		Money money = new Money(5000, to.getCurrency());
		Transaction tr = controller.pay(from, to, money, TransactionType.PAYMENT);
		Transaction rollback = controller.rollback(tr);
		assertNotNull(rollback);
		assertEquals(rollback.getMoney().integer(), 50);
		assertEquals(rollback.getMoney().currency(), to.getCurrency());
		from = controller.select(BankBook.class, from.getId());
		assertEquals(from.getMoney().integer(), 100);
		to = controller.select(BankBook.class, to.getId());
		assertEquals(to.getMoney().integer(), 100);
		controller.delete(Transaction.class, rollback.getId());
		controller.delete(Transaction.class, tr.getId());
	}
}
