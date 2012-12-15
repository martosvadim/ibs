package edu.ibs.core.controller;

import edu.ibs.core.entity.Account;
import edu.ibs.core.entity.Account.Role;
import edu.ibs.core.entity.User;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.PersistenceException;
import static org.junit.Assert.*;
import org.junit.*;

/**
 *
 * @author vadim
 */
public class CSUIDJpaControllerTest {

	private CSUIDJpaController data = new CSUIDJpaController();
	private Account u1, u2;

	public CSUIDJpaControllerTest() {
	}

	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}

	@Before
	public void setUp() {
		u1 = new Account("u1@gmail.com", "u1", Role.USER);
		u2 = new Account("u2@gmail.com", "u2", Role.USER);
	}

	@After
	public void tearDown() {
		try {
			data.delete(User.class, u1.getId());
		} catch (Throwable ignore) {
		}
		try {
			data.delete(User.class, u2.getId());
		} catch (Throwable ignore) {
		}
	}

	/**
	 * Test of insert method, of class CSUIDJpaController.
	 */
	@Test(expected = PersistenceException.class)
	public void testInsert() {
		data.insert(u1);
		u2 = data.select(Account.class, u1.getId());
		assertEquals(u1, u2);
		data.insert(u2);
	}

	/**
	 * Test of batchUpdate method, of class CSUIDJpaController.
	 */
	@Test
	public void testBatchUpdate() throws PersistenceException {
		String q1 = "vadim", q2 = "tratata";
		data.insert(u1);
		data.insert(u2);
		u1.setSecurityQuestion(q1);
		u2.setSecurityQuestion(q2);
		List<Account> users = new ArrayList<Account>(2);
		users.add(u1);
		users.add(u2);
		data.batchUpdate(users);
		u1 = data.select(Account.class, u1.getId());
		u2 = data.select(Account.class, u2.getId());
		assertEquals(q1, u1.getSecurityQuestion());
		assertEquals(q2, u2.getSecurityQuestion());
	}

	/**
	 * Test of update method, of class CSUIDJpaController.
	 */
	@Test
	public void testUpdate() throws Exception {
		data.insert(u1);
		String q = "vadik";
		u1.setSecurityQuestion(q);
		data.update(u1);
		u1 = data.select(Account.class, u1.getId());
		assertEquals(q, u1.getSecurityQuestion());
	}

	/**
	 * Test of delete method, of class CSUIDJpaController.
	 */
	@Test
	public void testDelete() throws Exception {
		data.insert(u1);
		data.delete(User.class, u1.getId());
		assertNotNull(u1);
		u1 = data.select(Account.class, u1.getId());
		assertNull(u1);
	}

	/**
	 * Test of exist method, of class CSUIDJpaController.
	 */
	@Test
	public void testExist() {
		data.insert(u1);
		boolean exist = data.exist(User.class, u1.getId());
		assertTrue(exist);
		exist = data.exist(User.class, -2L);
		assertFalse(exist);
	}

	/**
	 * Test of selectAll method, of class CSUIDJpaController.
	 */
	@Test
	public void testSelectAll_Class() {
		data.insert(u1);
		data.insert(u2);
		List<User> users = data.selectAll(User.class);
		assertNotNull(users);
		assertEquals(2, users.size());
		assertArrayEquals(users.toArray(), new Object[]{u1, u2});
	}

	/**
	 * Test of selectAll method, of class CSUIDJpaController.
	 */
	@Test
	public void testSelectAll_3args() {
		data.insert(u1);
		data.insert(u2);
		List<User> users = data.selectAll(User.class, 1, 0);
		assertNotNull(users);
		assertArrayEquals(users.toArray(), new Object[]{u1});
		users = data.selectAll(User.class, 1, 1);
		assertNotNull(users);
		assertArrayEquals(users.toArray(), new Object[]{u2});
		users = data.selectAll(User.class, 1, 2);
		assertNotNull(users);
		assertArrayEquals(users.toArray(), new Object[]{});
	}

	/**
	 * Test of select method, of class CSUIDJpaController.
	 */
	@Test
	public void testSelect() {
		data.insert(u1);
		u2 = data.select(Account.class, u1.getId());
		assertEquals(u1, u2);
	}

	/**
	 * Test of count method, of class CSUIDJpaController.
	 */
	@Test
	public void testCount() throws PersistenceException {
		assertEquals(0, data.count(User.class));
		data.insert(u1);
		assertEquals(1, data.count(User.class));
		data.insert(u2);
		assertEquals(2, data.count(User.class));
		data.delete(User.class, u1.getId());
		assertEquals(1, data.count(User.class));
	}
}
