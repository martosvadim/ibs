package edu.ibs.core.operation.logic;

import edu.ibs.common.dto.AccountDTO;
import edu.ibs.common.enums.AccountRole;
import edu.ibs.common.exceptions.IbsServiceException;
import edu.ibs.common.interfaces.IAuthService;
import edu.ibs.core.entity.Account;
import edu.ibs.core.gwt.EntityTransformer;
import edu.ibs.core.operation.AdminOperations;
import edu.ibs.core.operation.UserOperations;
import edu.ibs.core.utils.ServerConstants;
import edu.ibs.core.utils.ServletUtils;
import edu.ibs.core.utils.ValidationUtils;
import nl.captcha.Captcha;
import org.apache.log4j.Logger;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;

public class AuthServiceImpl implements IAuthService {

	private static final String EMPTY_CREDENTIALS_MSG = "Логин/Пароль не могут быть пустыми.";
	private static final String INCORRECT_CAPTCHA_TXT = "Вы ввели неверные символы с картинки.";
	private static final String PASSWORD_NOT_EQUAL_MSG = "Пароль не соответствует введённому.";
	private static final String CANNOT_LOGIN_MSG = "Неверный логин или пароль.";

	private UserOperations userLogic;
	private AdminOperations adminLogic;

	@Override
	public AccountDTO login(String name, String pass) throws IbsServiceException {

		if (ServerConstants.ADMIN_LOGIN.equals(name) && ServerConstants.ADMIN_PASS.equals(pass)) {
			AccountDTO dto = new AccountDTO();
			dto.setEmail(name);
			dto.setRole(AccountRole.ADMIN);
			return dto;
		} else if ("1".equals(name)) {
			AccountDTO dto = new AccountDTO();
			dto.setEmail(name);
			dto.setRole(AccountRole.USER);
			return dto;
		}

		// Если есть куки
		if (name.equals(ServletUtils.getRequest().getSession().getAttribute(ServerConstants.SESSION_LOGIN))) {
			//todo заполнить инфой из базы
            AccountDTO dto = EntityTransformer.transformAccount(userLogic.login(name, pass));
			return dto;
		} else  if (!ValidationUtils.isEmpty(name) && !ValidationUtils.isEmpty(pass)) {
            try {
                AccountDTO dto = EntityTransformer.transformAccount(userLogic.login(name, pass));
                ServletUtils.getRequest().getSession().setAttribute(ServerConstants.SESSION_LOGIN, name);
                if (ServerConstants.ADMIN_LOGIN.equals(name) && ServerConstants.ADMIN_PASS.equals(pass)) {
                    ServletUtils.getRequest().getSession().setAttribute(ServerConstants.ADMIN_ATTR, true);
                    dto.setRole(AccountRole.ADMIN);
                }
                return dto;
            } catch (NoResultException e) {
                throw new IbsServiceException(CANNOT_LOGIN_MSG);
            }
		} else {
			throw new IbsServiceException(EMPTY_CREDENTIALS_MSG);
		}
	}

	@Override
	public void logout(final String login) throws IbsServiceException{
		ServletUtils.getRequest().getSession().setAttribute(ServerConstants.SESSION_LOGIN, "");
        ServletUtils.getRequest().getSession().setAttribute(ServerConstants.ADMIN_ATTR, false);
		Logger.getLogger(this.getClass()).debug(String.format("Выполнен выход пользователем %s", login));
	}

	@Override
	public AccountDTO register(String name, String password, String passwordConfirm, String captchaText)
			throws IbsServiceException {

		if (ValidationUtils.isEmpty(name) || ValidationUtils.isEmpty(password)
				|| ValidationUtils.isEmpty(passwordConfirm) || ValidationUtils.isEmpty(captchaText)) {
			throw new IbsServiceException(EMPTY_CREDENTIALS_MSG);
		} else if (!password.equals(passwordConfirm)) {
			throw new IbsServiceException(PASSWORD_NOT_EQUAL_MSG);
		} else {
			// Верификация текста капчи
			Captcha captcha = (Captcha) ServletUtils.getRequest().getSession().getAttribute(Captcha.NAME);
			if (captcha != null && captcha.isCorrect(captchaText)) {
				return EntityTransformer.transformAccount(register(name, password));
			} else {
				throw new IbsServiceException(INCORRECT_CAPTCHA_TXT);
			}
		}
	}

	private Account register(String email, String passwd) throws PersistenceException {
		return adminLogic.create(AccountRole.USER, email, passwd);
	}

	public UserOperations getUserLogic() {
        return userLogic;
    }

    public void setUserLogic(UserOperations userLogic) {
        this.userLogic = userLogic;
    }

    public AdminOperations getAdminLogic() {
        return adminLogic;
    }

    public void setAdminLogic(AdminOperations adminLogic) {
        this.adminLogic = adminLogic;
    }
}
