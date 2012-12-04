package edu.ibs.webui.core;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface AuthorizationServiceAsync {
    void login(String login, String pass, String captchaText, AsyncCallback<String> callback);
    void isAuthenticated(String token, AsyncCallback<Boolean> callback);
}
