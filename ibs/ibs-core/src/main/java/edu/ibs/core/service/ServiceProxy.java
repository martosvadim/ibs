package edu.ibs.core.service;

import edu.ibs.core.entity.User;
import edu.ibs.core.entity.User.Role;

/**
 * @date Oct 22, 2012
 *
 * @author Vadim Martos
 */
public final class ServiceProxy {

	private static final CommonService service = new CommonService();

	private ServiceProxy() {
	}

	public static UserServicable user(User user) throws NullPointerException, IllegalArgumentException {
		if (user.getRole().equals(Role.USER)) {
			return service;
		}
		throw new IllegalArgumentException(String.format("User %s is not in role %s", user, Role.USER));
	}

	public static AdminServicable admin(User admin) throws NullPointerException, IllegalArgumentException {
		if (admin.getRole().equals(Role.ADMIN)) {
			return service;
		}
		throw new IllegalArgumentException(String.format("User %s is not in role %s", admin, Role.ADMIN));
	}

	public static User login(String email, String passwd) {
		return service.getUser(email, passwd);
	}

	public static User register(String email, String passwd) {
		return service.create(Role.USER, email, passwd);
	}
}
