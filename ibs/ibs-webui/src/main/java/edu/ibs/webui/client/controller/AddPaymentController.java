package edu.ibs.webui.client.controller;

import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import edu.ibs.common.dto.CardBookDTO;
import edu.ibs.common.dto.TransactionType;
import edu.ibs.common.interfaces.IPaymentServiceAsync;
import edu.ibs.webui.client.utils.AppCallback;
import edu.ibs.webui.client.utils.Components;

/**
 * User: EgoshinME
 * Date: 09.01.13
 * Time: 13:12
 */
public class AddPaymentController extends GenericWindowController {
	private final GenericController reciepient = Components.getTextItem();
	private final GenericController amount = Components.getTextItem();

	private CardBookDTO cardBookDTO = null;
    private IAction<Object> closeAction= null;

	public AddPaymentController() {
		getWindow().setTitle("Перевод средств");
		final IButton payButton = new IButton("Перевести");
		payButton.setWidth(80);
		payButton.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
			@Override
			public void onClick(com.smartgwt.client.widgets.events.ClickEvent clickEvent) {
				final String reciepientId = ((String) reciepient.unbind());
				final String amountTxt = ((String) amount.unbind());
				if (reciepientId == null || "".equals(reciepientId) || reciepientId.length() == 0) {
					SC.warn("Получатель не заполнен. ");
				} else if (amountTxt == null || "".equals(amountTxt) || amountTxt.length() == 0) {
					SC.warn("Сумма не заполнена.");
				} else {
					try {
						Float amountFloat = Float.parseFloat(amountTxt);
						if (amountFloat <= 0f) {
							SC.warn("Введите положительное значение.");
						} else {
							payButton.setDisabled(true);

							IPaymentServiceAsync.Util.getInstance().pay(getCardBookDTO(), reciepientId, amountFloat,
									TransactionType.TRANSFER, new AppCallback<Void>() {
										@Override
										public void onFailure(Throwable t) {
											super.onFailure(t);
											payButton.setDisabled(false);
										}

										@Override
										public void onSuccess(Void aVoid) {
											payButton.setDisabled(false);
											SC.say("Перевод успешно проведён");
                                            if (closeAction != null) {
                                                closeAction.execute(null);
                                            }
                                            getWindow().hide();
										}
									});
						}
					} catch (NumberFormatException e) {
						SC.warn("Введите корректную сумму.");
					}
				}
			}
		});
		VLayout layoutForm = new VLayout();
		layoutForm.setWidth100();
		layoutForm.setHeight100();

		layoutForm.addMember(Components.addTitle("Получатель", reciepient.getView()));
		layoutForm.addMember(Components.addTitle("Сумма", amount.getView()));

		HLayout buttons = new HLayout();
		buttons.addMember(payButton);

		VLayout view = new VLayout();
		view.setMembersMargin(MARGIN);
		view.addMember(layoutForm);
		view.addMember(buttons);
		view.setMargin(MARGIN);
		view.setShowResizeBar(false);

		getWindow().addItem(view);
	}

	public CardBookDTO getCardBookDTO() {
		return cardBookDTO;
	}

	public void setCardBookDTO(CardBookDTO cardBookDTO) {
		this.cardBookDTO = cardBookDTO;
        DynamicForm form = ((DynamicForm)amount.getView());
        ((TextItem) form.getFields()[0]).setShowHintInField(true);
        form.getFields()[0].setHint(Components.getCurrencyHint(cardBookDTO.getBankBook().getCurrency()));
	}

    public IAction<Object> getCloseAction() {
        return closeAction;
    }

    public void setCloseAction(IAction<Object> closeAction) {
        this.closeAction = closeAction;
    }
}
