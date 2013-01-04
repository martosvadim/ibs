package edu.ibs.webui.client.admin;

import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import edu.ibs.common.dto.AccountDTO;
import edu.ibs.common.enums.AccountRole;
import edu.ibs.common.interfaces.IAuthServiceAsync;
import edu.ibs.webui.client.controller.GenericController;
import edu.ibs.webui.client.controller.GenericWindowController;
import edu.ibs.webui.client.utils.AppCallback;
import edu.ibs.webui.client.utils.Components;

/**
 * User: EgoshinME
 * Date: 04.01.13
 * Time: 11:59
 */
public class CreateNewUserController extends GenericWindowController {

	private static final Integer MARGIN = 5;

	public CreateNewUserController() {
		getWindow().setTitle("Создание пользователя");
		final GenericController email = Components.getTextItem();
		final GenericController pass = Components.getPasswordItem();
		IButton createButton = new IButton("Создать");
		createButton.setWidth(80);
		createButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent clickEvent) {
				final String emailText = ((String) email.unbind());
				String passText = ((String) pass.unbind());
				if (emailText == null || "".equals(emailText) || emailText.length() == 0) {
					SC.warn("Логин не заполнен. Введите допустимый логин.");
				} else if (passText == null || "".equals(passText) || passText.length() == 0) {
					SC.warn("Пароль пуст. Введите допустимый пароль.");
				} else {
					IAuthServiceAsync.Util.getInstance().create(AccountRole.ADMIN, emailText, passText,
							new AppCallback<AccountDTO>() {
								@Override
								public void onSuccess(final AccountDTO s) {
									SC.say("Создан пользователь " + s.getEmail() + " с ролью " + s.getRole().toString());
								}
							});
				}
			}
		});
		VLayout layoutForm = new VLayout();
		layoutForm.setWidth100();
		layoutForm.setHeight100();

		layoutForm.addMember(Components.addTitle("E-mail", email.getView()));
		layoutForm.addMember(Components.addTitle("Пароль", pass.getView()));

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
