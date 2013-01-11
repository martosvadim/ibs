package edu.ibs.core;

import edu.ibs.common.enums.Fraction;
import edu.ibs.core.controller.SpecifiedJpaController;
import edu.ibs.core.entity.Currency;
import edu.ibs.core.gwt.EntityTransformer;
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
		SpecifiedJpaController c = SpecifiedJpaController.instance();
		Currency cu = c.getCurrency("USD");
		System.out.println(String.valueOf(cu.getFactor()));
		System.out.println(String.valueOf((double)cu.getFloatFactor()));
		System.out.println(String.valueOf(EntityTransformer.transformCurrency(cu).getFactor()));
		System.out.println(String.valueOf(EntityTransformer.transformCurrency(new Currency("", cu.getFloatFactor(), Fraction.TWO)).getFactor()));
	}
}
