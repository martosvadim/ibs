package edu.ibs.webui.client;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuBar;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.MenuItemSeparator;

/**
 * Created by IntelliJ IDEA.
 * User: Максим
 * Date: 17.10.12
 * Time: 21:50
 * To change this template use File | Settings | File Templates.
 */
public class ApplicationMenu extends HLayout {
    private static final int APPLICATION_MENU_HEIGHT = 27;

    private static final int DEFAULT_SHADOW_DEPTH = 10;

    private static final String SEPARATOR = "separator";

    private static final String ICON_PREFIX = "icons/16/";

    private static final String ICON_SUFFIX = ".png";


    private MenuBar menuBar;

    private int menuPosition = 0;


    public ApplicationMenu() {
        super();
        GWT.log("init ApplicationMenu()...", null);
        this.setStyleName("crm-ApplicationMenu");
        this.setHeight(APPLICATION_MENU_HEIGHT);
        menuBar = new MenuBar();
        this.addMember(menuBar);
    }

    public Menu addMenu(String menuName, int width, String menuItemNames) {
        Menu menu = new Menu();
        menu.setTitle(menuName);
        menu.setShowShadow(true);
        menu.setShadowDepth(DEFAULT_SHADOW_DEPTH);
        menu.setWidth(width);
        String[] menuItems = process(menuItemNames);

        for (int i = 0; i < menuItems.length; i++) {
            String menuItemName = menuItems[i].replaceAll("\\W", "");
            if (menuItemName.contentEquals(SEPARATOR)) {
                MenuItemSeparator separator = new MenuItemSeparator();
                menu.addItem(separator);
                continue;
            }

            MenuItem menuItem = new MenuItem(menuItems[i], getIcon(menuItems[i]));
            menu.addItem(menuItem);
        }

        Menu[] menus = new Menu[1];
        menus[0] = menu;
        menuBar.addMenus(menus, menuPosition);
        menuPosition++;

        return menus[0];
    }

    public Menu addMenu(String menuName, int width) {
        Menu menu = new Menu();
        menu.setTitle(menuName);
        menu.setShowShadow(true);
        menu.setShadowDepth(DEFAULT_SHADOW_DEPTH);
        menu.setWidth(width);
        Menu[] menus = new Menu[1];
        menus[0] = menu;
        menuBar.addMenus(menus, menuPosition);
        menuPosition++;
        return menu;
    }


    public Menu addSubMenu(Menu parentMenu, String subMenuName, String menuItemNames) {
        Menu menu = new Menu();
        menu.setShowShadow(true);
        menu.setShadowDepth(DEFAULT_SHADOW_DEPTH);
        MenuItem menuItem = new MenuItem(subMenuName);
        String[] menuItems = process(menuItemNames);

        for (int i = 0; i < menuItems.length; i++) {
            String menuItemName = menuItems[i].replaceAll("\\W", "");
            if (menuItemName.contentEquals(SEPARATOR)) {
                MenuItemSeparator separator = new MenuItemSeparator();
                menu.addItem(separator);
                continue;
            }

            menuItem = new MenuItem(menuItems[i], getIcon(menuItems[i]));
            menu.addItem(menuItem);
        }

        menuItem = new MenuItem(subMenuName);
        parentMenu.addItem(menuItem);
        menuItem.setSubmenu(menu);

        return menu;
    }

    public final static String DELIMITER = ",";

    public static String[] process(String line) {
        return line.split(DELIMITER);
    }

    private String getIcon(String applicationName) {
        String name = applicationName.replaceAll("\\W", "");
        String icon = ICON_PREFIX + name.toLowerCase() + ICON_SUFFIX;
        return icon;
    }

}
