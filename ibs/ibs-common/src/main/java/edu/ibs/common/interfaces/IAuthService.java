package edu.ibs.common.interfaces;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import edu.ibs.common.dto.UserDTO;

/**
 * Created with IntelliJ IDEA.
 * User: EgoshinME
 * Date: 05.12.12
 * Time: 5:34
 * To change this template use File | Settings | File Templates.
 */
@RemoteServiceRelativePath("auth.rpc")
public interface IAuthService extends RemoteService {
    UserDTO login(String name, String pass);
    UserDTO register(String name, String password, String passwordConfirm, String captchaText);
}
