package edu.ibs.webui.client.controller;

import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import edu.ibs.common.dto.CardBookDTO;
import edu.ibs.common.dto.ProviderDTO;
import edu.ibs.common.dto.TransactionType;
import edu.ibs.common.dto.VocDTO;
import edu.ibs.common.enums.ProviderField;
import edu.ibs.common.interfaces.IPaymentServiceAsync;
import edu.ibs.webui.client.utils.AppCallback;
import edu.ibs.webui.client.utils.Components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * User: Максим Date: 11.01.13 Time: 2:48
 */
public class MakeTransferController extends GenericWindowController {

	private IAction<Object> closeAction = null;
	private final GenericController amount = Components.getTextItem();
	private GenericController reciepient = Components.getComboBoxControll();
	private CardBookDTO cardBookDTO;
	private List<ProviderDTO> contragents = new ArrayList<ProviderDTO>();
	private VLayout providerFieldsLayout = new VLayout();
	private List<Canvas> providerFieldViews = new ArrayList<Canvas>();
	private Map<ProviderField, GenericController> fieldsMap = new HashMap<ProviderField, GenericController>();

	public MakeTransferController() {
		getWindow().setTitle("Оплата");
		final IButton payButton = new IButton("Оплатить");
		payButton.setWidth(80);

		IPaymentServiceAsync.Util.getInstance().getContragentList(new AppCallback<List<ProviderDTO>>() {

			@Override
			public void onSuccess(List<ProviderDTO> providerDTOs) {
				if (providerDTOs != null && providerDTOs.size() > 0) {
					contragents = providerDTOs;
					List<VocDTO<String, String>> bindList = new LinkedList<VocDTO<String, String>>();
					for (ProviderDTO dto : providerDTOs) {
						VocDTO<String, String> vocDTO = new VocDTO<String, String>(
								String.valueOf(dto.getId()), String.valueOf(dto.getCard().getBankBook().getDescription()));
						bindList.add(vocDTO);
					}
					reciepient.bind(bindList);
				}
			}
		});

		reciepient.addOnChange(new IAction() {

			@Override
			public void execute(Object data) {
				providerFieldsLayout.setVisible(true);
				for (Canvas c : providerFieldViews) {
					providerFieldsLayout.removeMember(c);
				}
				providerFieldViews.clear();
				fieldsMap.clear();
				String id = ((VocDTO<String, String>) reciepient.unbind()).getId();
				ProviderDTO dto = null;
				if (id != null && id.length() > 0) {
					for (ProviderDTO pdto : contragents) {
						if (pdto.getId() == Long.parseLong(id)) {
							dto = pdto;
							break;
						}
					}
					for (ProviderField field : dto.getFieldList()) {
						GenericController controller = Components.getTextItem();
						Canvas providerFieldView = Components.addTitle(getProviderFieldTitle(field), controller.getView());
						providerFieldsLayout.addMember(providerFieldView);
						fieldsMap.put(field, controller);
						providerFieldViews.add(providerFieldView);
					}

					providerFieldsLayout.redraw();
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
						Float amountFloat = Float.parseFloat(amountTxt);
						if (amountFloat <= 0f) {
							SC.warn("Введите положительное значение.");
						} else {
							if (validateFields()) {
								payButton.setDisabled(true);

								String description = "";
								for (ProviderField field : fieldsMap.keySet()) {
									String str = field.toString() + ": " + fieldsMap.get(field).unbind() + "; ";
									description += str;
								}

								IPaymentServiceAsync.Util.getInstance().pay(getCardBookDTO(), reciepientId, amountFloat,
										TransactionType.PAYMENT, description, new AppCallback<Void>() {

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
		layoutForm.addMember(providerFieldsLayout);
		providerFieldsLayout.setVisible(false);
		VLayout amountLayout = new VLayout();
		amountLayout.setHeight100();
		amountLayout.addMember(Components.addTitle("Сумма", amount.getView()));
		layoutForm.addMember(amountLayout);

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

	private String getProviderFieldTitle(ProviderField field) {
		switch (field) {
			case BOOK:
				return "Счёт";
			case PHONE:
				return "№ телефона";
			case ADDRESS:
				return "Адрес";
			case NAME:
				return "Имя";
			case PASSPORT:
				return "№ пасспорта";
			default:
				return "";
		}
	}

	private boolean validateFields() {
		for (ProviderField field : fieldsMap.keySet()) {
			GenericController controller = fieldsMap.get(field);
			if (!field.validate((String) controller.unbind())) {
				SC.say(field.validPattern());
				return false;
			}
		}
		return true;
	}

	public CardBookDTO getCardBookDTO() {
		return cardBookDTO;
	}

	public void setCardBookDTO(CardBookDTO cardBookDTO) {
		this.cardBookDTO = cardBookDTO;
		DynamicForm form = ((DynamicForm) amount.getView());
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
