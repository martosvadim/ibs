package edu.ibs.webui.client.admin;

import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import edu.ibs.common.dto.AccountDTO;
import edu.ibs.common.dto.VocDTO;
import edu.ibs.common.enums.AccountRole;
import edu.ibs.common.interfaces.IAuthServiceAsync;
import edu.ibs.webui.client.controller.GenericController;
import edu.ibs.webui.client.controller.GenericWindowController;
import edu.ibs.webui.client.utils.AppCallback;
import edu.ibs.webui.client.utils.Components;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * User: EgoshinME
 * Date: 04.01.13
 * Time: 11:59
 */
public class CreateNewUserController extends GenericWindowController {

	private final GenericController email = Components.getTextItem();
	private final GenericController pass = Components.getPasswordItem();
	private final GenericController role = Components.getComboBoxControll();

	public CreateNewUserController() {
		getWindow().setTitle("Создание пользователя");

		List<VocDTO<String, String>> roles = new LinkedList<VocDTO<String, String>>();
		for (AccountRole accountRole : AccountRole.values()) {
			VocDTO<String, String> vocDTO = new VocDTO<String, String>();
			vocDTO.setId(accountRole.toString());
			String displayName = accountRole.toString();
			if (AccountRole.ADMIN.equals(accountRole)) {
				displayName = "Администратор";
			} else if (AccountRole.USER.equals(accountRole)) {
				displayName = "Пользователь";
			}
			vocDTO.setValue(displayName);
			roles.add(vocDTO);
		}
		role.bind(roles);

		IButton createButton = new IButton("Создать");
		createButton.setWidth(80);
		createButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent clickEvent) {
				String emailText = ((String) email.unbind());
				String passText = ((String) pass.unbind());
				String roleTxt = ((VocDTO<String, String>) role.unbind()).getId();
				final String roleDisplayName = ((VocDTO<String, String>) role.unbind()).getValue();
				if (emailText == null || "".equals(emailText) || emailText.length() == 0) {
					SC.warn("Логин не заполнен. Введите допустимый логин.");
				} else if (passText == null || "".equals(passText) || passText.length() == 0) {
					SC.warn("Пароль пуст. Введите допустимый пароль.");
				} else if (roleTxt == null || "".equals(roleTxt) || roleTxt.length() == 0
						|| AccountRole.forName(roleTxt) == null) {
					SC.warn("Неверная роль.");
				} else {
					AccountRole role1 = AccountRole.forName(roleTxt);
						IAuthServiceAsync.Util.getInstance().create(role1, emailText, passText,
								new AppCallback<AccountDTO>() {
									@Override
									public void onSuccess(final AccountDTO s) {
										SC.say("Создан пользователь " + s.getEmail() + " с ролью " + roleDisplayName);
										getWindow().hide();
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
		layoutForm.addMember(Components.addTitle("Роль", role.getView()));

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

	@Override
	public void reload() {
		email.bind("");
		pass.bind("");
	}
}
