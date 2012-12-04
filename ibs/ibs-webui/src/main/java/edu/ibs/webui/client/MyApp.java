package edu.ibs.webui.client;

import com.google.gwt.core.client.EntryPoint;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.CloseClickEvent;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.LinkItem;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.layout.VStack;
import com.smartgwt.client.widgets.menu.Menu;
import edu.ibs.webui.client.controller.GenericController;
import edu.ibs.webui.client.utils.Components;

public class MyApp implements EntryPoint {

    private static final int HEADER_HEIGHT = 85;
    private static final int DEFAULT_MENU_WIDTH = 70;

    private VLayout mainLayout;

    private boolean loggedIn = false;

    /**
     * Основное окно
     */
    private Canvas bg = new Canvas();

    public void onModuleLoad() {

        com.google.gwt.user.client.Window.enableScrolling(false);
        com.google.gwt.user.client.Window.setMargin("0px");

        bg.setWidth100();
        bg.setHeight100();

        if (!loggedIn) {
            getLoginCanvas().draw();
        } else {
            bg.addChild(getMainLayout());
        }

        bg.draw();
    }

    private Window getRegisterCanvas() {
        final Window window = Components.getWindow();
        window.setHeight(270);

        IButton regButton = new IButton("Зарегистрироваться");
        regButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent clickEvent) {
                window.hide();
                bg.addChild(getMainLayout());
            }
        });
        regButton.setWidth(150);

        final Img captchaImage = new Img("/SimpleCaptcha.jpg");
        captchaImage.setWidth(120);
        captchaImage.setHeight(35);

        GenericController login = Components.getTextItem();
        GenericController pass = Components.getPasswordItem();
        GenericController repPass = Components.getPasswordItem();

        VStack layout = new VStack();
        layout.setMembersMargin(5);
        layout.setMargin(5);
        layout.addMember(Components.addTitle("Логин", login.getView()));
        layout.addMember(Components.addTitle("Пароль", pass.getView()));
        layout.addMember(Components.addTitle("Подтверждение пароля", repPass.getView()));

        GenericController capthcaInputTextItemView = Components.getTextItem();
        layout.addMember(Components.addTitle("", captchaImage));
        layout.addMember(Components.addTitle("", capthcaInputTextItemView.getView()));
        layout.addMember(regButton);
        layout.setShowResizeBar(false);

        window.addItem(layout);
        window.addCloseClickHandler(new CloseClickHandler() {
            @Override
            public void onCloseClick(final CloseClickEvent closeClickEvent) {
                window.hide();
                bg.addChild(getMainLayout());
            }
        });

        return window;
    }

    private Window getLoginCanvas() {
        final Window window = Components.getWindow();

        IButton loginButton = new IButton("Войти");
        loginButton.setWidth(80);
        loginButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent clickEvent) {
                window.hide();
                bg.addChild(getMainLayout());
//                loggedIn = true;
//                User user = ServiceProxy.login("", "");
//                AbstractEntity user;
            }
        });
        GenericController login = Components.getTextItem();
        GenericController pass = Components.getPasswordItem();

        LinkItem linkItem = new LinkItem("link");
        linkItem.setShowTitle(false);
        linkItem.setLinkTitle("Регистрация");
        linkItem.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
            @Override
            public void onClick(final com.smartgwt.client.widgets.form.fields.events.ClickEvent clickEvent) {
                window.hide();
                getRegisterCanvas().draw();
            }
        });
        DynamicForm regForm = new DynamicForm();
        regForm.setItems(linkItem);
        regForm.setColWidths("0,*");

        VStack layout = new VStack();
        layout.setMembersMargin(5);
        layout.setMargin(5);
        layout.addMember(Components.addTitle("Логин", login.getView()));
        layout.addMember(Components.addTitle("Пароль", pass.getView()));

        HLayout buttons = new HLayout();
        buttons.setMembersMargin(5);
        buttons.addMember(loginButton);
        buttons.addMember(regForm);

        layout.addMember(buttons);
        layout.setShowResizeBar(false);

        window.addItem(layout);
        window.addCloseClickHandler(new CloseClickHandler() {
            @Override
            public void onCloseClick(CloseClickEvent closeClickEvent) {
                window.hide();
                bg.addChild(getMainLayout());
            }
        });

        return window;
    }

    private Canvas getMainLayout() {
        if (mainLayout == null) {

            mainLayout = new VLayout();
            mainLayout.setWidth100();
            mainLayout.setHeight100();

            HLayout northLayout = new HLayout();
            northLayout.setHeight(HEADER_HEIGHT);

            VLayout vLayout = new VLayout();
            vLayout.addMember(new Masthead());

            // intiialise the Application menu
            ApplicationMenu applicationMenu = new ApplicationMenu();

            applicationMenu.addMenu("<u>N</u>ew Activity", DEFAULT_MENU_WIDTH,
                                    "Task, Fax, Phone Call, Email, Letter, " +
                                    "Appointment");
            applicationMenu.addMenu("New Re<u>c</u>ord", DEFAULT_MENU_WIDTH,
                                    "Account, Contact, separator, Lead, Opportunity");
            Menu goToMenu = applicationMenu.addMenu("<u>G</u>o To", DEFAULT_MENU_WIDTH - 30);
            applicationMenu.addSubMenu(goToMenu, "Sales", "Leads, Opportunities, Accounts, Contacts");
            applicationMenu.addSubMenu(goToMenu, "Settings", "Administration, Templates, Data Management");
            applicationMenu.addSubMenu(goToMenu, "Resource Centre", "Highlights, Sales, Settings");
            applicationMenu.addMenu("<u>T</u>ools", DEFAULT_MENU_WIDTH - 30,
                                    "Import Data, Duplicate Detection, Advanced Find, Options");
            applicationMenu.addMenu("<u>H</u>elp", DEFAULT_MENU_WIDTH - 30,
                                    "Help on this Page, Contents, myCRM Online, About myCRM");

            vLayout.addMember(applicationMenu);
            northLayout.addMember(vLayout);

            VLayout westLayout = new NavigationPane();
            westLayout.setWidth("15%");

            VLayout eastLayout = new AccountView();
            eastLayout.setWidth("85%");

            HLayout southLayout = new HLayout();
            southLayout.setMembers(westLayout, eastLayout);

            mainLayout.addMember(northLayout);
            mainLayout.addMember(southLayout);
        }

        return mainLayout;
    }
}
