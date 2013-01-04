package edu.ibs.core.operation.logic;

import edu.ibs.core.controller.SpecifiedJpaController;
import edu.ibs.core.entity.Account;
import org.junit.*;

/**
 *
 * @author vadim
 */
public class CommonServiceTest {
	
	private final CommonService service = new CommonService();
	private final SpecifiedJpaController controller = SpecifiedJpaController.instance();
	
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
	}
	
	@After
	public void tearDown() {
	}
	
	@Test
	public void registerTest() {
		String email = "vadim.martos@gmail.com";
		Account acc = null;
		try {
			Assert.assertTrue(service.isFree(email));
			acc = service.register(email, "12314");
			Assert.assertFalse(service.isFree(email));
			Account acc1 = controller.getUserAccount(acc.getEmail(), acc.getPassword());
			Assert.assertEquals(acc, acc1);
		} finally {
			if (acc != null) {
				controller.delete(acc.getClass(), acc.getId());
			}
		}
	}
}
