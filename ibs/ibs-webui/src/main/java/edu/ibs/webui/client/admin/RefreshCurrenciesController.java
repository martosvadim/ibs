package edu.ibs.webui.client.admin;

import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import edu.ibs.common.interfaces.IPaymentService;
import edu.ibs.common.interfaces.IPaymentServiceAsync;
import edu.ibs.webui.client.controller.GenericWindowController;
import edu.ibs.webui.client.utils.AppCallback;

/**
 * User: Максим Date: 14.01.13 Time: 2:36
 */
public class RefreshCurrenciesController extends GenericWindowController {

	private final IButton refreshBtn = new IButton("Обновить");
	private final Label label = new Label("Валюты обновляются, ожидайте");
//    private final IButton closeBtn = new IButton("Закрыть");

	public RefreshCurrenciesController() {
		getWindow().setTitle("Обновление курсов валют.");
		getWindow().setShowCloseButton(false);
		label.setVisible(false);

//        closeBtn.addClickHandler(new ClickHandler() {
//            @Override
//            public void onClick(ClickEvent clickEvent) {
//                getWindow().hide();
//            }
//        });

		refreshBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent clickEvent) {
				refreshBtn.setDisabled(true);
				label.setVisible(true);
				IPaymentServiceAsync.Util.getInstance().refreshCurrencies(new AppCallback<Void>() {

					@Override
					public void onFailure(Throwable t) {
						super.onFailure(t);
						refreshBtn.setDisabled(false);
					}

					@Override
					public void onSuccess(Void aVoid) {
						refreshBtn.setDisabled(false);
						getWindow().hide();
						SC.say("Курсы валют успешно обновлены.");
					}
				});
			}
		});

		VLayout vl = new VLayout();
		vl.setMembersMargin(MARGIN);
		vl.setMargin(15);
		vl.addMember(refreshBtn);
		vl.addMember(label);
		vl.setWidth100();
		vl.setHeight100();
//		buttons.addMember(closeBtn);
		getWindow().addItem(vl);
	}
}
