package edu.ibs.common.enums;

import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author vadim
 */
public class ProviderFieldTest {

	public ProviderFieldTest() {
	}

	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}

	@Before
	public void setUp() {
	}

	@After
	public void tearDown() {
	}

	@Test
	public void testPhoneValidate() {
		ProviderField phone = ProviderField.PHONE;
		assertTrue(phone.validate("375447655483"));
		assertTrue(phone.validate("375224655483"));
		assertFalse(phone.validate("275224655483"));
		assertFalse(phone.validate("325224655483"));
		assertFalse(phone.validate("372224655483"));
		assertFalse(phone.validate("37522465548a"));
		assertFalse(phone.validate(null));
		assertFalse(phone.validate(" 375224655483"));
		assertFalse(phone.validate("375224655483 "));
		assertFalse(phone.validate(" 37522465548"));
		assertFalse(phone.validate("37522465548 "));
		assertFalse(phone.validate("a37522465548"));
		assertFalse(phone.validate(""));
		assertFalse(phone.validate(" "));
		assertFalse(phone.validate("            "));
	}

	@Test
	public void testNameValidate() {
		ProviderField name = ProviderField.NAME;
		assertFalse(name.validate("Vadim"));
		assertTrue(name.validate("Вадим"));
		assertTrue(name.validate("Марто"));
		assertTrue(name.validate("Мартос"));
		assertFalse(name.validate("Вадим Мартос"));
		assertTrue(name.validate("Вадим-Мартос"));
		assertFalse(name.validate(" Вадим-Мартос"));
		assertFalse(name.validate("В-адим-Мартос"));
		assertFalse(name.validate("     "));
		assertFalse(name.validate(""));
		assertFalse(name.validate(null));
		assertFalse(name.validate("-вадим"));
		assertFalse(name.validate("вадим"));
		assertFalse(name.validate("!вадим"));
		assertFalse(name.validate("-------"));
		assertFalse(name.validate("Vaдим"));
	}

	public void testAddressValidate() {
		ProviderField address = ProviderField.ADDRESS;
		assertTrue(address.validate("Барановичи"));
		assertTrue(address.validate("Барановичи, Беларусь"));
		assertTrue(address.validate("Барановичи, Беларусь, ул. Глинки"));
		assertTrue(address.validate("Барановичи, Беларусь, ул. Глинки, д. 13а"));
		assertFalse(address.validate(""));
		assertFalse(address.validate("  "));
		assertFalse(address.validate("..."));
		assertFalse(address.validate(".Барановичи"));
		assertFalse(address.validate(null));
	}
}
