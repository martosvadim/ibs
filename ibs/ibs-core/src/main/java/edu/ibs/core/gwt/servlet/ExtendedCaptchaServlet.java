package edu.ibs.core.gwt.servlet;

import nl.captcha.Captcha;
import nl.captcha.backgrounds.GradiatedBackgroundProducer;
import nl.captcha.servlet.CaptchaServletUtil;
import nl.captcha.servlet.SimpleCaptchaServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static nl.captcha.Captcha.NAME;


/**
 * @author EgoshinME
 */
public class ExtendedCaptchaServlet extends SimpleCaptchaServlet {
    private static final long serialVersionUID = 6560171562324177699L;

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Captcha captcha = new Captcha.Builder(120, 35)
                .addText()
                .addBackground(new GradiatedBackgroundProducer())
                .gimp()
                .addNoise()
                .addBorder()
                .build();
        session.setAttribute(NAME, captcha);
        CaptchaServletUtil.writeImage(resp, captcha.getImage());
    }

}
