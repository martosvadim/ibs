package edu.ibs.webui.client.controller;

import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import edu.ibs.webui.client.utils.Components;

/**
 * User: Максим
 * Date: 12.01.13
 * Time: 21:08
 */
public class MainHistoryController extends GenericWindowController {
    public MainHistoryController() {
        getWindow().setTitle("Выписка по карте");
        IButton createButton = new IButton("Сохранить");
		createButton.setWidth(80);
		createButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent clickEvent) {

            }
        });
        VLayout layoutForm = new VLayout();
		layoutForm.setWidth100();
		layoutForm.setHeight100();

//		layoutForm.addMember(Components.addTitle("Имя", firstName.getView()));

		HLayout buttons = new HLayout();
		buttons.addMember(createButton);

		VLayout view = new VLayout();
		view.setMembersMargin(MARGIN);
		view.addMember(layoutForm);
		view.addMember(buttons);
		view.setMargin(MARGIN);
		view.setShowResizeBar(false);

		getWindow().addItem(view);
    }
}
