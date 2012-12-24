package edu.ibs.common.interfaces;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import edu.ibs.common.dto.AccountDTO;
import edu.ibs.common.dto.UserDTO;
import edu.ibs.common.exceptions.IbsServiceException;

/**
 * User: EgoshinME
 * Date: 05.12.12
 * Time: 5:34
 */
@RemoteServiceRelativePath("auth.rpc")
public interface IAuthService extends RemoteService {
	AccountDTO login(String name, String pass) throws IbsServiceException;
	void logout(String login) throws IbsServiceException;
	AccountDTO register(String name, String password, String passwordConfirm, String captchaText) throws IbsServiceException;
}
