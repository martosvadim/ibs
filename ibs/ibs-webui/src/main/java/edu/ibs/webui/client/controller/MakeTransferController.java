package edu.ibs.webui.client.controller;

import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import edu.ibs.common.dto.CardBookDTO;
import edu.ibs.common.dto.TransactionType;
import edu.ibs.common.dto.VocDTO;
import edu.ibs.common.interfaces.IPaymentServiceAsync;
import edu.ibs.webui.client.utils.AppCallback;
import edu.ibs.webui.client.utils.Components;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * User: Максим
 * Date: 11.01.13
 * Time: 2:48
 */
public class MakeTransferController extends GenericWindowController {

    private IAction<Object> closeAction = null;

	private final GenericController amount = Components.getTextItem();
    private GenericController reciepient = Components.getComboBoxControll();

	private CardBookDTO cardBookDTO;
    private List<CardBookDTO> contragents = new ArrayList<CardBookDTO>();

	public MakeTransferController() {
		getWindow().setTitle("Оплата");
		final IButton payButton = new IButton("Оплатить");
		payButton.setWidth(80);

        IPaymentServiceAsync.Util.getInstance().getContragentList(new AppCallback<List<CardBookDTO>>() {
            @Override
            public void onSuccess(List<CardBookDTO> cardBookDTOs) {
                if (cardBookDTOs != null && cardBookDTOs.size() > 0) {
                    contragents = cardBookDTOs;
                    List<VocDTO<String, String>> bindList = new LinkedList<VocDTO<String, String>>();
                    for (CardBookDTO dto : cardBookDTOs) {
                        VocDTO<String, String> vocDTO = new VocDTO<String, String>(
                                    String.valueOf(dto.getId()), String.valueOf(dto.getBankBook().getDescription()));
                        bindList.add(vocDTO);
                    }
                    reciepient.bind(bindList);
                }
            }
        });

		payButton.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
			@Override
			public void onClick(com.smartgwt.client.widgets.events.ClickEvent clickEvent) {
                VocDTO<String, String> recVoc = ((VocDTO<String, String>) reciepient.unbind());
				final String reciepientId = recVoc.getId();
				final String amountTxt = ((String) amount.unbind());
				if (reciepientId == null || "".equals(reciepientId) || reciepientId.length() == 0) {
					SC.warn("Получатель не заполнен. ");
				} else if (amountTxt == null || "".equals(amountTxt) || amountTxt.length() == 0) {
					SC.warn("Сумма не заполнена.");
				} else {
					try {
						Double amountDouble = Double.parseDouble(amountTxt);
						if (amountDouble <= 0d) {
							SC.warn("Введите положительное значение.");
						} else {
							payButton.setDisabled(true);

							IPaymentServiceAsync.Util.getInstance().pay(getCardBookDTO(), reciepientId, amountDouble,
                                    TransactionType.PAYMENT, new AppCallback<Void>() {
										@Override
										public void onFailure(Throwable t) {
											super.onFailure(t);
											payButton.setDisabled(false);
										}

										@Override
										public void onSuccess(Void aVoid) {
											payButton.setDisabled(false);
											SC.say("Платёж успешно проведён");
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
	}

    public IAction<Object> getCloseAction() {
        return closeAction;
    }

    public void setCloseAction(IAction<Object> closeAction) {
        this.closeAction = closeAction;
    }
}
