package edu.ibs.webui.client;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.HLayout;
import edu.ibs.webui.client.utils.JS;

/**
 * User: Максим
 * Date: 29.10.12
 * Time: 23:18
 */
public class Masthead extends HLayout {

    private static final int MASTHEAD_HEIGHT = 58;

    public Masthead() {
        super();
        GWT.log("init Masthead()...", null);
        this.setStyleName("crm-Masthead");
        this.setHeight(MASTHEAD_HEIGHT);
        Img logo = new Img("logo.png", 48, 48);
        logo.setStyleName("crm-Masthead-Logo");

        Label name = new Label();
        name.setStyleName("crm-MastHead-Name");
        name.setContents("IBS");

        HLayout westLayout = new HLayout();
        westLayout.setHeight(MASTHEAD_HEIGHT);
        westLayout.setWidth("50%");
        westLayout.addMember(logo);
        westLayout.addMember(name);

        Label signedInUser = new Label();
        signedInUser.setStyleName("crm-MastHead-SignedInUser");
        signedInUser.setContents(JS.getCookie("ibs.login"));

        HLayout eastLayout = new HLayout();
        eastLayout.setAlign(Alignment.RIGHT);
        eastLayout.setHeight(MASTHEAD_HEIGHT);
        eastLayout.setWidth("50%");
        eastLayout.addMember(signedInUser);

        this.addMember(westLayout);
        this.addMember(eastLayout);

    }

}
