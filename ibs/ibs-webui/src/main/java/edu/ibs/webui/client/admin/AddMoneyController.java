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
 * Date: 08.01.13
 * Time: 8:42
 */
public class AddMoneyController extends GenericWindowController {

	private GenericController userIdControl;

	public AddMoneyController() {
		getWindow().setTitle("Пополнение счёта");

		final IButton addMoneyButton = new IButton("Пополнить");
		addMoneyButton.setWidth(80);
		addMoneyButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent clickEvent) {
				String userIdText = ((String) userIdControl.unbind());
				if (userIdText == null || "".equals(userIdText) || userIdText.length() == 0) {
					SC.warn("Идентификатор пользователя не заполнен.");
				} else {
					addMoneyButton.setDisabled(true);
					IPaymentServiceAsync.Util.getInstance().createBankBook(userIdText, new AppCallback<BankBookDTO>() {
						@Override
						public void onFailure(Throwable t) {
							super.onFailure(t);
							addMoneyButton.setDisabled(false);
						}

						@Override
						public void onSuccess(BankBookDTO bankBookDTO) {
							addMoneyButton.setDisabled(false);
							if (bankBookDTO != null && bankBookDTO.getId() != 0) {
								long id = bankBookDTO.getId();
								String owner = "";
								if (bankBookDTO.getOwner() != null && bankBookDTO.getOwner().getId() != 0) {
									owner = bankBookDTO.getOwner().getFirstName() + " " + bankBookDTO.getOwner().getLastName();
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
		buttons.addMember(addMoneyButton);

		VLayout view = new VLayout();
		view.setMembersMargin(MARGIN);
		view.addMember(layoutForm);
		view.addMember(buttons);
		view.setMargin(MARGIN);
		view.setShowResizeBar(false);

		getWindow().addItem(view);
	}
}
