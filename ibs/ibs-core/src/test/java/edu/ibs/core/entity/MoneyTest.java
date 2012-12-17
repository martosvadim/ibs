/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ibs.core.entity;

import edu.ibs.common.enums.Fraction;
import org.junit.*;

import static org.junit.Assert.assertEquals;

/**
 *
 * @author vadim
 */
public class MoneyTest {

	private static final long USD_INTEGER = 231L;
	private static final int USD_FRACTION = 73;
	private static float USD_FACTOR = 85.3113f;
	private static final long EUR_INTEGER = 523L;
	private static final int EUR_FRACTION = 3;
	private static float EUR_FACTOR = 103.4513f;
	private Money usd;
	private Money eur;

	public MoneyTest() {
	}

	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}

	@Before
	public void setUp() {
		Currency usdCurr = new Currency("usd", USD_FACTOR, Fraction.TWO);
		usd = new Money(USD_INTEGER, USD_FRACTION, usdCurr);
		Currency eurCurr = new Currency("eur", EUR_FACTOR, Fraction.TWO);
		eur = new Money(EUR_INTEGER, EUR_FRACTION, eurCurr);
	}

	@After
	public void tearDown() {
	}

	/**
	 * Test of integer method, of class Money.
	 */
	@Test
	public void testInteger() {
		assertEquals(usd.integer(), USD_INTEGER);
		assertEquals(eur.integer(), EUR_INTEGER);
	}

	/**
	 * Test of fraction method, of class Money.
	 */
	@Test
	public void testFraction() {
		assertEquals(usd.fraction(), USD_FRACTION);
		assertEquals(eur.fraction(), EUR_FRACTION);
	}

	/**
	 * Test of convert method, of class Money.
	 */
	@Test
	public void testConvert() {
		Money inEuro = usd.convert(eur.currency());
		//expected 191.09655992 
		assertEquals(inEuro.integer(), 191);
		assertEquals(inEuro.fraction(), 9);
		//after double convertation you will lose 1 cent
		Money inUsd = inEuro.convert(usd.currency());
		assertEquals(inUsd.integer(), USD_INTEGER);
		assertEquals(inUsd.fraction(), USD_FRACTION - 1);
	}

	/**
	 * Test of balance method, of class Money.
	 */
	@Test
	public void testBalance() {
		assertEquals(usd.balance(), USD_INTEGER * usd.currency().getFraction().multiply() + USD_FRACTION);
	}

	/**
	 * Test of add method, of class Money.
	 */
	@Test
	public void testAdd() {
		Money total = usd.add(usd);
		int multiply = usd.currency().getFraction().multiply();
		long balance = USD_INTEGER * 2 * multiply + USD_FRACTION * 2;
		assertEquals(total.integer(), balance / multiply);
		assertEquals(total.fraction(), balance % multiply);
		//after double convertation you will lose 1 cent
		total = usd.add(usd.convert(eur.currency()));
		balance -= 1;
		assertEquals(total.integer(), balance / multiply);
		assertEquals(total.fraction(), balance % multiply);
	}

	/**
	 * Test of subtract method, of class Money.
	 */
	@Test
	public void testSubtract() {
		Money total = usd.subtract(usd);
		assertEquals(total.integer(), 0);
		assertEquals(total.fraction(), 0);
		//after double convertation you will lose 1 cent
		total = usd.subtract(usd.convert(eur.currency()));
		assertEquals(total.integer(), 0);
		assertEquals(total.fraction(), 1);
	}

	/**
	 * Test of ge method, of class Money.
	 */
	@Test
	public void testGe() {
	}

	/**
	 * Test of gt method, of class Money.
	 */
	@Test
	public void testGt() {
	}

	/**
	 * Test of lt method, of class Money.
	 */
	@Test
	public void testLt() {
	}

	/**
	 * Test of le method, of class Money.
	 */
	@Test
	public void testLe() {
	}

	/**
	 * Test of compareTo method, of class Money.
	 */
	@Test
	public void testCompareTo() {
	}

	/**
	 * Test of compare method, of class Money.
	 */
	@Test
	public void testCompare() {
	}
}
