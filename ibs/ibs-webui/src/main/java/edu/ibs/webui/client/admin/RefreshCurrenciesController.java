package edu.ibs.webui.client.admin;

import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import edu.ibs.common.interfaces.IPaymentService;
import edu.ibs.common.interfaces.IPaymentServiceAsync;
import edu.ibs.webui.client.controller.GenericWindowController;
import edu.ibs.webui.client.utils.AppCallback;

/**
 * User: Максим
 * Date: 14.01.13
 * Time: 2:36
 */
public class RefreshCurrenciesController extends GenericWindowController {

    private final IButton refreshBtn = new IButton("Обновить");
    private final IButton closeBtn = new IButton("Закрыть");

    public RefreshCurrenciesController() {
        getWindow().setTitle("Обновление курсов валют.");
        getWindow().setShowCloseButton(false);

        closeBtn.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                getWindow().hide();
            }
        });

        refreshBtn.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                refreshBtn.setDisabled(true);
                IPaymentServiceAsync.Util.getInstance().refreshCurrencies(new AppCallback<Void>() {
                    @Override
                    public void onFailure(Throwable t) {
                        super.onFailure(t);
                        refreshBtn.setDisabled(false);
                    }
                    @Override
                    public void onSuccess(Void aVoid) {
                        refreshBtn.setDisabled(false);
                        SC.say("Курсы валют успешно обновлены.");
                    }
                });
            }
        });

        HLayout buttons = new HLayout();
        buttons.setMembersMargin(MARGIN);
        buttons.setMargin(45);
		buttons.addMember(refreshBtn);
        buttons.addMember(closeBtn);
        getWindow().addItem(buttons);
    }
}
