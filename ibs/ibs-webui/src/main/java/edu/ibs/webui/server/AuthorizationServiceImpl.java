package edu.ibs.webui.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import edu.ibs.webui.core.AuthorizationService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class AuthorizationServiceImpl extends RemoteServiceServlet implements
        AuthorizationService {

    @Override
    public String login(String name, String pass, String captchaText) {
        HttpServletRequest request = getThreadLocalRequest();
        HttpSession session = request.getSession();
//        Captcha captcha = (Captcha) session.getAttribute(Captcha.NAME);
//        return String.valueOf(captcha.isCorrect(captchaText));
        return "";
    }

    @Override
    public Boolean isAuthenticated(String token) {
        return true;
    }
}
