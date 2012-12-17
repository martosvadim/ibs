package edu.ibs.core.controller;

import edu.ibs.common.dto.TransactionType;
import edu.ibs.common.enums.Fraction;
import edu.ibs.core.entity.*;
import org.junit.*;

import static org.junit.Assert.*;

/**
 *
 * @author vadim
 */
public class SpecifiedJpaControllerTest {

	private static final SpecifiedJpaController controller = SpecifiedJpaController.instance();
	private User user;
	private Currency usd, eur;
	private CardBook from, to;
	private BankBook fromBB, toBB;
	private CreditPlan plan;
	private Credit credit;

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
		user = new User("vadim", "martos", "AB1953782");
		controller.insert(user);
		usd = new Currency("usd", 1.5f, Fraction.TWO);
		controller.insert(usd);
		eur = new Currency("eur", 3f, Fraction.TWO);
		controller.insert(eur);
		Money m1 = new Money(10000, usd);
		fromBB = new BankBook(m1, user, false);
		controller.insert(fromBB);
		from = new CardBook(fromBB, 1L, "1234");
		controller.insert(from);
		Money m2 = new Money(10000, eur);
		toBB = new BankBook(m2, user, false);
		controller.insert(toBB);
		to = new CardBook(toBB, 2L, "2345");
		controller.insert(to);
	}

	@After
	public void tearDown() throws Exception {
		try {
			controller.delete(CardBook.class, from.getId());
		} catch (Throwable ignore) {
		}
		try {
			controller.delete(CardBook.class, to.getId());
		} catch (Throwable ignore) {
		}
		try {
			controller.delete(BankBook.class, fromBB.getId());
		} catch (Throwable ignore) {
		}
		try {
			controller.delete(BankBook.class, toBB.getId());
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
		Money money = new Money(5000, to.getBankBook().getCurrency());
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
		Money money = new Money(5000, to.getBankBook().getCurrency());
		Transaction tr = controller.pay(from, to, money, TransactionType.PAYMENT);
		Transaction rollback = controller.rollback(tr);
		assertNotNull(rollback);
		assertEquals(rollback.getMoney().integer(), 50);
		assertEquals(rollback.getMoney().currency(), to.getBankBook().getCurrency());
		from = controller.select(CardBook.class, from.getId());
		assertEquals(from.getBankBook().getMoney().integer(), 100);
		to = controller.select(CardBook.class, to.getId());
		assertEquals(to.getBankBook().getMoney().integer(), 100);
		controller.delete(Transaction.class, rollback.getId());
		controller.delete(Transaction.class, tr.getId());
	}
	//todo add test cases for credit
}
