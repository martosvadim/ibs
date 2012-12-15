package edu.ibs.core.service;

import edu.ibs.core.entity.Account;
import edu.ibs.core.entity.Account.Role;
import edu.ibs.core.service.logic.ApplicationContextProvider;

/**
 * @date Oct 22, 2012
 *
 * @author Vadim Martos
 */
public final class ServiceProxy {

	private static final UserServicable userLogic = (UserServicable) ApplicationContextProvider.provide().getBean("userLogic");
	private static final AdminServicable adminLogic = (AdminServicable) ApplicationContextProvider.provide().getBean("adminLogic");

	private ServiceProxy() {
	}

	public static UserServicable user(Account acc) throws NullPointerException, IllegalArgumentException {
		if (acc.getRole().equals(Role.USER)) {
			return userLogic;
		}
		throw new IllegalArgumentException(String.format("User %s is not in role %s", acc, Role.USER));
	}

	public static AdminServicable admin(Account acc) throws NullPointerException, IllegalArgumentException {
		if (acc.getRole().equals(Role.ADMIN)) {
			return adminLogic;
		}
		throw new IllegalArgumentException(String.format("User %s is not in role %s", acc, Role.ADMIN));
	}

	public static Account login(String email, String passwd) {
		return userLogic.login(email, passwd);
	}

	public static Account registerUser(String email, String passwd) {
		return adminLogic.create(Role.USER, email, passwd);
	}
}
