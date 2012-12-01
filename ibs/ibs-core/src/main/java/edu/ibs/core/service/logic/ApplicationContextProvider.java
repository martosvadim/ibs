package edu.ibs.core.service.logic;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @date Dec 1, 2012
 *
 * @author Vadim Martos
 */
public class ApplicationContextProvider {

	private static final ApplicationContext context = new ClassPathXmlApplicationContext("classpath:beans.xml");

	private ApplicationContextProvider() {
	}

	public static ApplicationContext provide() {
		return context;
	}
}
