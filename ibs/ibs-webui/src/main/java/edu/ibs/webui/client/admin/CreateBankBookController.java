package edu.ibs.webui.client.admin;

import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import edu.ibs.common.dto.BankBookDTO;
import edu.ibs.common.dto.CurrencyDTO;
import edu.ibs.common.dto.VocDTO;
import edu.ibs.common.interfaces.IPaymentServiceAsync;
import edu.ibs.webui.client.controller.GenericController;
import edu.ibs.webui.client.controller.GenericWindowController;
import edu.ibs.webui.client.utils.AppCallback;
import edu.ibs.webui.client.utils.Components;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * User: EgoshinME
 * Date: 05.01.13
 * Time: 5:48
 */
public class CreateBankBookController extends GenericWindowController {

	private final GenericController userIdControl = Components.getTextItem();
	private GenericController currenciesControl = Components.getComboBoxControll();

	private List<CurrencyDTO> currencyDTOList = new ArrayList<CurrencyDTO>();

	public CreateBankBookController() {
		getWindow().setTitle("Создание банковского счёта");

		IPaymentServiceAsync.Util.getInstance().getCurrencies(new AppCallback<List<CurrencyDTO>>() {
			@Override
			public void onSuccess(List<CurrencyDTO> currencyDTOs) {
				if (currencyDTOs != null && currencyDTOs.size() > 0) {
					List<VocDTO<String, String>> bindList = new LinkedList<VocDTO<String, String>>();

					for (CurrencyDTO dto : currencyDTOs) {
						currencyDTOList.add(dto);
						VocDTO<String, String> vocDTO = new VocDTO<String, String>();
						vocDTO.setId(String.valueOf(dto.getId()));
						vocDTO.setValue(dto.getName());
						bindList.add(vocDTO);
					}
					currenciesControl.bind(bindList);
				}
			}
		});

		final IButton createButton = new IButton("Создать");
		createButton.setWidth(80);
		createButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent clickEvent) {
				String userIdText = ((String) userIdControl.unbind());
				VocDTO<String, String> currencyVoc = ((VocDTO<String, String>) currenciesControl.unbind());
				String currencyTxt = currencyVoc.getValue();
				if (userIdText == null || "".equals(userIdText) || userIdText.length() == 0) {
					SC.warn("Идентификатор пользователя не заполнен.");
				} else if (currencyTxt == null || "".equals(currencyTxt) || currencyTxt.length() == 0 || currencyVoc.getId() == null) {
					SC.warn("Не выбрана валюта счёта.");
				} else {
					createButton.setDisabled(true);
					CurrencyDTO currencyDTO = null;
					for (CurrencyDTO currencyDTO1 : currencyDTOList) {
						if (currencyDTO1.getId() == Integer.parseInt(currencyVoc.getId())) {
							currencyDTO = currencyDTO1;
						}
					}
					IPaymentServiceAsync.Util.getInstance().createBankBook(userIdText, currencyDTO, new AppCallback<BankBookDTO>() {
						@Override
						public void onFailure(Throwable t) {
							super.onFailure(t);
							createButton.setDisabled(false);
						}
						@Override
						public void onSuccess(BankBookDTO bankBookDTO) {
							createButton.setDisabled(false);
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
		layoutForm.addMember(Components.addTitle("Валюта", currenciesControl.getView()));

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
