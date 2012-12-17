package edu.ibs.core.service.logic;

import edu.ibs.common.dto.AccountDTO;
import edu.ibs.common.enums.AccountRole;
import edu.ibs.common.exceptions.IbsServiceException;
import edu.ibs.common.interfaces.IAuthService;
import edu.ibs.core.entity.Account;
import edu.ibs.core.gwt.EntityTransformer;
import edu.ibs.core.service.AdminServicable;
import edu.ibs.core.service.UserServicable;
import edu.ibs.core.utils.ValidationUtils;
import javax.persistence.PersistenceException;

public class AuthServiceImpl implements IAuthService {

	private UserServicable userLogic;
	private AdminServicable adminLogic;

	@Override
	public AccountDTO login(String name, String pass) throws IbsServiceException {
		if (!ValidationUtils.isEmpty(name) && !ValidationUtils.isEmpty(pass)) {
			return EntityTransformer.transformAccount(userLogic.login(name, pass));
		} else {
			throw new IbsServiceException("Credentials can't be empty.");
		}
	}

	@Override
	public AccountDTO register(String name, String password, String passwordConfirm, String captchaText)
			throws IbsServiceException {

		if (ValidationUtils.isEmpty(name) || ValidationUtils.isEmpty(password)
				|| ValidationUtils.isEmpty(passwordConfirm)) {
			throw new IbsServiceException("Credentials can't be empty.");
		} else if (ValidationUtils.isEmpty(captchaText)) {
			//todo check captcha
//			HttpServletRequest request = getThreadLocalRequest();
//			HttpSession session = request.getSession();
//			Captcha captcha = (Captcha) session.getAttribute(Captcha.NAME);
//			return String.valueOf(captcha.isCorrect(captchaText));
			throw new IbsServiceException("Incorrect captcha's text.");
		}
		return EntityTransformer.transformAccount(register(name, password));
	}

	private Account register(String email, String passwd) throws PersistenceException {
		return adminLogic.create(AccountRole.USER, email, passwd);
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
