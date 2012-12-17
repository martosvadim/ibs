package edu.ibs.core.service.logic;

import edu.ibs.common.dto.UserDTO;
import edu.ibs.core.entity.Account;
import edu.ibs.core.entity.User;
import edu.ibs.common.interfaces.IAuthService;
import edu.ibs.core.service.AdminServicable;
import edu.ibs.core.service.UserServicable;
import org.springframework.web.servlet.mvc.Controller;

import javax.persistence.PersistenceException;

public class AuthServiceImpl implements IAuthService {

	private UserServicable userLogic;
	private AdminServicable adminLogic;

	@Override
	public UserDTO login(String name, String pass) {
		return transform(userLogic.login(name, pass));
	}

	@Override
	public UserDTO register(String name, String password, String passwordConfirm, String captchaText) {
		//todo validation
		return transform(register(name, password));
	}

	private Account register(String email, String passwd) throws PersistenceException {
		return adminLogic.create(Account.Role.USER, email, passwd);
	}

	//todo сделать отдельный класс для конвертера?
	/**
	 * Конвертация
	 *
	 * @param user пользователь
	 * @return объект для передачи данных
	 */
	private UserDTO transform(final Account user) {
        UserDTO dto = new UserDTO();
        return dto;
	}

    public UserServicable getUserLogic() {
        return userLogic;
    }

    public void setUserLogic(UserServicable userLogic) {
        this.userLogic = userLogic;
    }

    public AdminServicable getAdminLogic() {
        return adminLogic;
    }

    public void setAdminLogic(AdminServicable adminLogic) {
        this.adminLogic = adminLogic;
    }
}
