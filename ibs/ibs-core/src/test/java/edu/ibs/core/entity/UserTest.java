package edu.ibs.core.entity;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.*;

/**
 *
 * @author vadim
 */
public class UserTest {

    public UserTest() {
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
    public void testPassportNumber() {
        assertFalse(User.validatePassportNumber(""));
        assertFalse(User.validatePassportNumber(null));
        assertFalse(User.validatePassportNumber("AP"));
        assertFalse(User.validatePassportNumber("AP123"));
        assertFalse(User.validatePassportNumber(" AP1234356"));
        assertFalse(User.validatePassportNumber("AP1234356 "));
        assertFalse(User.validatePassportNumber("ap1234356"));
        assertFalse(User.validatePassportNumber("AB1234356s"));
        assertFalse(User.validatePassportNumber("AB12343563"));
        assertFalse(User.validatePassportNumber("ABV1234356"));
        assertFalse(User.validatePassportNumber("1AB1234356"));
        assertTrue(User.validatePassportNumber("AB1234356"));
    }
}
