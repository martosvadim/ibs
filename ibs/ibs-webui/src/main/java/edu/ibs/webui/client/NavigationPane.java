package edu.ibs.webui.client;

/**
 * Created by IntelliJ IDEA.
 * User: Максим
 * Date: 29.10.12
 * Time: 23:00
 * To change this template use File | Settings | File Templates.
 */

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.HTMLFlow;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;


public class NavigationPane extends SectionStack {

    private static final int WEST_WIDTH = 200;

    public NavigationPane() {
        super();
        GWT.log("init NavigationPane()...", null);
        this.setWidth(WEST_WIDTH);
        this.setVisibilityMode(VisibilityMode.MUTEX);
        this.setShowExpandControls(false);
        this.setAnimateSections(true);

        String payments = "Ссылки<br />По разделу<br />Платежи<br />";

        String cards = "Ссылки<br />По разделу<br />Карты<br />";

        String settings = "Данные<br />Сменить пароль<br />Сменить e-mail<br />";

        SectionStackSection section1 = new SectionStackSection("Платежи");
        section1.setExpanded(true);

        HTMLFlow htmlFlow1 = new HTMLFlow();
        htmlFlow1.setOverflow(Overflow.AUTO);
        htmlFlow1.setPadding(10);
        htmlFlow1.setContents(payments);
        section1.addItem(htmlFlow1);

        SectionStackSection section2 = new SectionStackSection("Карты");
        section2.setExpanded(true);

        HTMLFlow htmlFlow2 = new HTMLFlow();
        htmlFlow2.setOverflow(Overflow.AUTO);
        htmlFlow2.setPadding(10);
        htmlFlow2.setContents(cards);
        section2.addItem(htmlFlow2);

        SectionStackSection section3 = new SectionStackSection("Настройки");
        section3.setExpanded(true);

        HTMLFlow htmlFlow3 = new HTMLFlow();
        htmlFlow3.setOverflow(Overflow.AUTO);
        htmlFlow3.setPadding(10);
        htmlFlow3.setContents(settings);
        section3.addItem(htmlFlow3);

        this.addSection(section1);
        this.addSection(section2);
        this.addSection(section3);
    }

}

