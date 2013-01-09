package edu.ibs.core.operation.logic;

import edu.ibs.common.enums.AccountRole;
import edu.ibs.common.enums.CardBookType;
import edu.ibs.common.enums.Fraction;
import edu.ibs.core.controller.SpecifiedJpaController;
import edu.ibs.core.entity.*;
import java.util.Date;
import java.util.List;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author vadim
 */
public class CommonServiceTest {

    private final CommonService service = new CommonService();
    private final SpecifiedJpaController controller = SpecifiedJpaController.instance();
    Account acc1, acc2;
    User u1, u2;
    BankBook bb1, bb2;
    CardBook cb1, cb2;
    Currency usd, eur;

    public CommonServiceTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        u1 = new User("Test1", "User1", "MP3452165");
        controller.insert(u1);
        u2 = new User("Test2", "User2", "MP5125634");
        controller.insert(u2);

        acc1 = new Account("test1@gmail.com", "test1", AccountRole.USER);
        acc1.setUser(u1);
        controller.insert(acc1);
        acc2 = new Account("test2@gmail.com", "test2", AccountRole.USER);
        acc2.setUser(u2);
        controller.insert(acc2);

        usd = new Currency("USD-test", 8560.2f, Fraction.TWO);
        controller.insert(usd);
        eur = new Currency("EUR-test", 10320.4f, Fraction.TWO);
        controller.insert(eur);

        bb1 = new BankBook(u1, new Money(10000l, usd));
        controller.insert(bb1);
        bb2 = new BankBook(u2, new Money(10000l, eur));
        controller.insert(bb2);

        cb1 = new CardBook(bb1);
        controller.insert(cb1);
        cb2 = new CardBook(bb2);
        controller.insert(cb2);
    }

    @After
    public void tearDown() {
        controller.delete(cb1.getClass(), cb1.getId());
        controller.delete(cb2.getClass(), cb2.getId());

        controller.delete(bb1.getClass(), bb1.getId());
        controller.delete(bb2.getClass(), bb2.getId());

        controller.delete(eur.getClass(), eur.getId());
        controller.delete(usd.getClass(), usd.getId());

        controller.delete(acc1.getClass(), acc1.getId());
        controller.delete(acc2.getClass(), acc2.getId());

        controller.delete(u1.getClass(), u1.getId());
        controller.delete(u2.getClass(), u2.getId());
    }

    @Test
    public void registerTest() {
        String email = "testuser@gmail.com";
        Account acc = null;
        try {
            assertTrue(service.isFree(email));
            acc = service.register(email, "12314");
            assertFalse(service.isFree(email));
            Account actual = controller.getUserAccount(acc.getEmail(), acc.getPassword());
            assertEquals(acc, actual);
        } finally {
            if (acc != null) {
                controller.delete(acc.getClass(), acc.getId());
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void registerInvalidEmailTest() {
        String email = "testuser.gmail@com";
        Account acc = null;
        try {
            assertFalse(service.isValid(email));
            assertFalse(service.isFree(email));
            acc = service.register(email, "pass");
            assertFalse(service.isFree(email));
            assertNull(acc);
            Account actual = controller.getUserAccount(acc.getEmail(), acc.getPassword());
            assertNull(actual);
        } finally {
            if (acc != null) {
                controller.delete(acc.getClass(), acc.getId());
            }
        }
    }

    @Test
    public void loginTest() {
        String email = "testuser@gmail.com", pass = "pass";
        Account acc = null;
        try {
            acc = service.register(email, pass);
            Account a2 = service.login(email, pass);
            assertEquals(acc, a2);
            Account a3 = service.login(email, pass + 1);
            assertNull(a3);
            a3 = service.login(email + 1, pass);
            assertNull(a3);
        } finally {
            if (acc != null) {
                controller.delete(acc.getClass(), acc.getId());
            }
        }
    }

    @Test
    public void updateAccountTest() {
        String email = "testuser@gmail.com", pass = "ad";
        Account acc = null;
        User u = null;
        try {
            acc = service.register(email, pass);
            assertNull(acc.getUser());
            u = new User("Test", "User", "AB1953782");
            acc.setUser(u);
            service.update(acc);
            Account a1 = service.login(email, pass);
            assertEquals(u, a1.getUser());
            u = a1.getUser();
            String addr = "addr";
            u.setAddress(addr);
            service.update(a1);
            acc = service.login(email, pass);
            assertNotNull(acc.getUser());
            assertNotNull(acc.getUser().getAddress());
            assertEquals(addr, acc.getUser().getAddress());
        } finally {
            if (u != null && u.getId() != 0) {
                controller.delete(u.getClass(), u.getId());
            }
            if (acc != null) {
                controller.delete(acc.getClass(), acc.getId());
            }
        }
    }

    @Test
    public void requestCardTest() {
        CardRequest request = null;
        BankBook bb = null;
        try {
            int count = service.getAllRequests().size();
            bb = new BankBook(u1, new Money(2, usd));
            Date from = new Date();
            request = service.requestDebitCard(u1, bb);
            Date to = new Date();
            bb = request.getBankBook();
            assertNotNull(bb);
            assertFalse(request.isWatched());
            assertFalse(request.isApproved());
            assertEquals(request.getType(), CardBookType.DEBIT);

            List<CardRequest> requests = service.getRequests(from, to);
            assertNotNull(requests);
            assertEquals(1, requests.size());

            List<BankBook> bankBooks = service.getBankBooks(u1);
            assertNotNull(bankBooks);
            assertEquals(2, bankBooks.size());

            requests = service.getAllRequests();
            assertNotNull(requests);
            assertEquals(count + 1, requests.size());

            CardRequest actual = service.getCardRequestsOf(u1, false).iterator().next();
            assertNotNull(actual);
            assertFalse(actual.isApproved());
            assertFalse(request.isWatched());

            service.decline(actual, "Fake request");
            actual = service.getCardRequestsOf(u1, true).iterator().next();
            assertNotNull(actual);
            assertFalse(actual.isApproved());
            assertTrue(actual.isWatched());

            requests = service.getRequests(from, to);
            assertNotNull(requests);
            assertEquals(0, requests.size());

            requests = service.getAllRequests();
            assertNotNull(requests);
            assertEquals(count, requests.size());
        } finally {
            if (bb != null && bb.getId() > 0) {
                controller.delete(bb.getClass(), bb.getId());
            } else if (request != null && request.getId() > 0) {
                controller.delete(request.getClass(), request.getId());
            }
        }
    }

    @Test
    public void createCardBookTest() {
        CardBook cb = null;
        try {
            cb = service.createDebitCardBook(u2, bb1);
            Assert.assertNotNull(cb);
            Assert.assertEquals(cb.getOwner(), u2);
            Assert.assertEquals(cb.getBankBook(), bb1);
            Assert.assertNull(cb.getCredit());
        } finally {
            if (cb != null && cb.getId() > 0) {
                controller.delete(cb.getClass(), cb.getId());
            }
        }
    }
}
