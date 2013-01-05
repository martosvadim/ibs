package edu.ibs.webui.client.admin;

import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import edu.ibs.common.dto.BankBookDTO;
import edu.ibs.common.interfaces.IPaymentServiceAsync;
import edu.ibs.webui.client.controller.GenericController;
import edu.ibs.webui.client.controller.GenericWindowController;
import edu.ibs.webui.client.utils.AppCallback;
import edu.ibs.webui.client.utils.Components;

/**
 * User: EgoshinME
 * Date: 05.01.13
 * Time: 5:48
 */
public class CreateBankBookController extends GenericWindowController {

	private final GenericController userIdControl = Components.getTextItem();

	public CreateBankBookController() {
		getWindow().setTitle("Создание банковского счёта");

		IButton createButton = new IButton("Создать");
		createButton.setWidth(80);
		createButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent clickEvent) {
				String userIdText = ((String) userIdControl.unbind());
				if (userIdText == null || "".equals(userIdText) || userIdText.length() == 0) {
					SC.warn("Идентификатор пользователя не заполнен.");
				} else {
					IPaymentServiceAsync.Util.getInstance().createBankBook(userIdText, new AppCallback<BankBookDTO>() {
						@Override
						public void onSuccess(BankBookDTO bankBookDTO) {
							if (bankBookDTO != null && bankBookDTO.getId() != 0) {
								long id = bankBookDTO.getId();
								String owner = "";
								if (bankBookDTO.getOwner() != null && bankBookDTO.getOwner().getId() != 0) {
									owner = bankBookDTO.getOwner().getEmail();
								}
								SC.say("Создан банковский счёт " + id + " для пользователя " + owner + ".");
							}
						}
					});
				}
			}
		});
		VLayout layoutForm = new VLayout();
		layoutForm.setWidth100();
		layoutForm.setHeight100();

		layoutForm.addMember(Components.addTitle("Идентификатор пользователя", userIdControl.getView()));

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
		userIdControl.bind("");
	}
}
