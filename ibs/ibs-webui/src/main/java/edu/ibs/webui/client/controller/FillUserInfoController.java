package edu.ibs.webui.client.controller;

import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import edu.ibs.common.dto.UserDTO;
import edu.ibs.common.interfaces.IAuthServiceAsync;
import edu.ibs.webui.client.ApplicationManager;
import edu.ibs.webui.client.utils.AppCallback;
import edu.ibs.webui.client.utils.Components;

/**
 * User: EgoshinME
 * Date: 05.01.13
 * Time: 3:13
 */
public class FillUserInfoController extends GenericWindowController {

	public FillUserInfoController() {
		getWindow().setTitle("Информация о пользователе");
		final GenericController firstName = Components.getTextItem();
		final GenericController lastName = Components.getTextItem();
		final GenericController passportNumber = Components.getTextItem();
		IButton createButton = new IButton("Сохранить");
		createButton.setWidth(80);
		createButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent clickEvent) {
				String fNameTxt = ((String) firstName.unbind());
				String lNameTxt = ((String) lastName.unbind());
				String pNumTxt = ((String) passportNumber.unbind());
				if (fNameTxt == null || "".equals(fNameTxt) || fNameTxt.length() == 0) {
					SC.warn("Имя не заполнено.");
				} else if (lNameTxt == null || "".equals(lNameTxt) || lNameTxt.length() == 0) {
					SC.warn("Фамилия не заполнена.");
				} else if (pNumTxt == null || "".equals(pNumTxt) || pNumTxt.length() == 0) {
					SC.warn("Номер пасспорта не заполнен.");
				} else {
					IAuthServiceAsync.Util.getInstance().setUser(ApplicationManager.getInstance().getAccount(),
							fNameTxt, lNameTxt, pNumTxt, new AppCallback<UserDTO>() {
						@Override
						public void onSuccess(UserDTO userDTO) {
							if (userDTO != null && userDTO.getFirstName() != null) {
								ApplicationManager.getInstance().getAccount().setUser(userDTO);
							}
						}
					});
				}
			}
		});
		VLayout layoutForm = new VLayout();
		layoutForm.setWidth100();
		layoutForm.setHeight100();

		layoutForm.addMember(Components.addTitle("Имя", firstName.getView()));
		layoutForm.addMember(Components.addTitle("Фамилия", lastName.getView()));
		layoutForm.addMember(Components.addTitle("№ Паспорта", passportNumber.getView()));

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
