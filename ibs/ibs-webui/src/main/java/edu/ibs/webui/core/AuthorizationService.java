package edu.ibs.webui.core;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("auth")
public interface AuthorizationService extends RemoteService {
    String login(String name, String pass, String captchaText);
    Boolean isAuthenticated(String token);
}
