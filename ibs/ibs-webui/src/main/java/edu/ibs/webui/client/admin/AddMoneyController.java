package edu.ibs.webui.client.admin;

import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import edu.ibs.common.dto.BankBookDTO;
import edu.ibs.common.interfaces.IPaymentServiceAsync;
import edu.ibs.webui.client.ApplicationManager;
import edu.ibs.webui.client.controller.GenericController;
import edu.ibs.webui.client.controller.GenericWindowController;
import edu.ibs.webui.client.controller.IAction;
import edu.ibs.webui.client.utils.AppCallback;
import edu.ibs.webui.client.utils.Components;

/**
 * User: EgoshinME
 * Date: 08.01.13
 * Time: 8:42
 */
public class AddMoneyController extends GenericWindowController {

	private VLayout step1Layout, step2Layout;
	private GenericController bankBookIdControl = Components.getTextItem();
	private GenericController amountControl = Components.getTextItem();
	final IButton nextBtn = new IButton("Дальше");
	final IButton checkBtn = new IButton("Проверить");
	final IButton addMoneyBtn = new IButton("Пополнить");
	private boolean checked = false;
	private BankBookDTO bankBookDTO;

	public AddMoneyController() {
		getWindow().setTitle("Пополнение счёта");

		bankBookIdControl.addOnChange(new IAction() {
			@Override
			public void execute(Object data) {
				nextBtn.setDisabled(true);
				checked = false;
			}
		});

		nextBtn.setDisabled(true);
		nextBtn.setWidth(80);
		checkBtn.setWidth(80);
		checkBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent clickEvent) {
				String bankBookIdText = ((String) bankBookIdControl.unbind());
				if (bankBookIdText == null || "".equals(bankBookIdText) || bankBookIdText.length() == 0) {
					SC.warn("Номер банковского счета не заполнен.");
				} else {
					checkBtn.setDisabled(true);
					try {
						long id = Long.valueOf(bankBookIdText);
						IPaymentServiceAsync.Util.getInstance().getBankBook(ApplicationManager.getInstance().getAccount(),
								id, new AppCallback<BankBookDTO>() {
							@Override
							public void onFailure(Throwable t) {
								super.onFailure(t);
								checkBtn.setDisabled(false);
							}

							@Override
							public void onSuccess(BankBookDTO bankBookDTO) {
								checkBtn.setDisabled(false);
								if (bankBookDTO != null && bankBookDTO.getId() > 0) {
									setBankBookDTO(bankBookDTO);
									nextBtn.setDisabled(false);
									checked = true;
								} else {
									nextBtn.setDisabled(true);
								}
							}
						});
					} catch (NumberFormatException e) {
						SC.say("Введите числовой номер банковского счёта.");
						checkBtn.setDisabled(false);
					}
				}
			}
		});
		nextBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent clickEvent) {
				getWindow().removeItem(getStep1Layout());
				getWindow().addItem(getStep2Layout());
				getWindow().redraw();
			}
		});

		addMoneyBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent clickEvent) {
				String amountTxt = ((String) amountControl.unbind());
				if (amountTxt == null || "".equals(amountTxt) || amountTxt.length() == 0) {
					SC.warn("Сумма не заполнена.");
				} else {
					try {
						Float amountFloat = Float.parseFloat(amountTxt);
						if (amountFloat <= 0f) {
							SC.warn("Введите положительное значение.");
						} else {
							addMoneyBtn.setDisabled(true);
							IPaymentServiceAsync.Util.getInstance().addMoney(getBankBookDTO(), amountFloat,
									new AppCallback<Boolean>() {

								@Override
								public void onFailure(Throwable t) {
									super.onFailure(t);
									addMoneyBtn.setDisabled(false);
								}

								@Override
								public void onSuccess(Boolean aBoolean) {
									addMoneyBtn.setDisabled(false);
									if (aBoolean) {
										SC.say("Счёт пополнен.");
                                        getWindow().hide();
									}
								}
							});
						}
					} catch (NumberFormatException e) {
						SC.warn("Введите корректную сумму.");
					}
				}
			}
		});


		getWindow().addItem(getStep1Layout());
	}

	private VLayout getStep1Layout() {
		if (step1Layout == null) {
			VLayout layoutForm = new VLayout();
			layoutForm.setWidth100();
			layoutForm.setHeight100();

			layoutForm.addMember(Components.addTitle("Номер банковского счета", bankBookIdControl.getView()));

			HLayout buttons = new HLayout();
			buttons.setMembersMargin(MARGIN);
			buttons.addMember(checkBtn);
			buttons.addMember(nextBtn);

			step1Layout = new VLayout();
			step1Layout.setMembersMargin(MARGIN);
			step1Layout.addMember(layoutForm);
			step1Layout.addMember(buttons);
			step1Layout.setMargin(MARGIN);
			step1Layout.setShowResizeBar(false);
		}
		return step1Layout;
	}

	private Canvas getStep2Layout() {
		if (step2Layout == null) {
			VLayout layoutForm = new VLayout();
			layoutForm.setMembersMargin(MARGIN);
			layoutForm.setWidth100();
			layoutForm.setHeight100();

			layoutForm.addMember(
					Components.addTitle("Идентификатор счета", new Label(String.valueOf(getBankBookDTO().getId()))));
			layoutForm.addMember(
					Components.addTitle("Баланс", new Label(String.valueOf(getBankBookDTO().getBalance()))));
			layoutForm.addMember(
					Components.addTitle("Валюта счета", new Label(getBankBookDTO().getCurrency().getName())));
			layoutForm.addMember(
					Components.addTitle("Владелец счета",
							new Label(getBankBookDTO().getOwner().getFirstName()
									+ " " + getBankBookDTO().getOwner().getLastName())));
			layoutForm.addMember(Components.addTitle("Сумма", amountControl.getView()));

			HLayout buttons = new HLayout();
			buttons.addMember(addMoneyBtn);

			step2Layout = new VLayout();
			step2Layout.setMembersMargin(MARGIN);
			step2Layout.addMember(layoutForm);
			step2Layout.addMember(buttons);
			step2Layout.setMargin(MARGIN);
			step2Layout.setShowResizeBar(false);
		}
		return step2Layout;
	}

	public void setBankBookDTO(BankBookDTO bankBookDTO) {
		this.bankBookDTO = bankBookDTO;
	}

	public BankBookDTO getBankBookDTO() {
		return bankBookDTO;
	}
}
