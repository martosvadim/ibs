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
import edu.ibs.common.dto.CurrencyDTO;
import edu.ibs.common.dto.UserDTO;
import edu.ibs.common.dto.VocDTO;
import edu.ibs.common.interfaces.IPaymentServiceAsync;
import edu.ibs.webui.client.ApplicationManager;
import edu.ibs.webui.client.controller.GenericController;
import edu.ibs.webui.client.controller.GenericWindowController;
import edu.ibs.webui.client.controller.IAction;
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

    private VLayout step1Layout, step2Layout;
	private final GenericController userEmailControl = Components.getTextItem();
	private GenericController currenciesControl = Components.getComboBoxControll();
    private final IButton nextBtn = new IButton("Дальше");
	private final IButton checkBtn = new IButton("Проверить");
    private final IButton createButton = new IButton("Создать");
    private boolean checked = false;
    private UserDTO userDTO;

	private List<CurrencyDTO> currencyDTOList = new ArrayList<CurrencyDTO>();

	public CreateBankBookController() {
		getWindow().setTitle("Создание банковского счёта");

        userEmailControl.addOnChange(new IAction() {
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
				String userEmailTxt = ((String) userEmailControl.unbind());
				if (userEmailTxt == null || "".equals(userEmailTxt) || userEmailTxt.length() == 0) {
					SC.warn("E-mail не заполнен.");
				} else {
					checkBtn.setDisabled(true);
					IPaymentServiceAsync.Util.getInstance().getUser(userEmailTxt, new AppCallback<UserDTO>() {
                        @Override
                        public void onFailure(Throwable t) {
                            super.onFailure(t);
                            checkBtn.setDisabled(false);
                        }

                        @Override
                        public void onSuccess(UserDTO uDTO) {
                            checkBtn.setDisabled(false);
                            if (uDTO != null && uDTO.getId() > 0) {
                                setUserDTO(uDTO);
                                nextBtn.setDisabled(false);
                                checked = true;
                            } else {
                                nextBtn.setDisabled(true);
                            }
                        }
                    });
				}
			}
		});

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


		createButton.setWidth(80);
		createButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent clickEvent) {

				VocDTO<String, String> currencyVoc = ((VocDTO<String, String>) currenciesControl.unbind());
				String currencyTxt = currencyVoc.getValue();
				if (currencyTxt == null || "".equals(currencyTxt) || currencyTxt.length() == 0 || currencyVoc.getId() == null) {
					SC.warn("Не выбрана валюта счёта.");
				} else {
					createButton.setDisabled(true);
					CurrencyDTO currencyDTO = null;
					for (CurrencyDTO currencyDTO1 : currencyDTOList) {
						if (currencyDTO1.getId() == Integer.parseInt(currencyVoc.getId())) {
							currencyDTO = currencyDTO1;
						}
					}
					IPaymentServiceAsync.Util.getInstance().createBankBook(getUserDTO(), currencyDTO, new AppCallback<BankBookDTO>() {
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
                                getWindow().hide();
							}
						}
					});
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

		getWindow().addItem(getStep1Layout());
	}

    private VLayout getStep1Layout() {
		if (step1Layout == null) {
            VLayout layoutForm = new VLayout();
            layoutForm.setWidth100();
            layoutForm.setHeight100();

            layoutForm.addMember(Components.addTitle("E-mail", userEmailControl.getView()));

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
					Components.addTitle("Имя", new Label(String.valueOf(getUserDTO().getFirstName()))));
			layoutForm.addMember(
					Components.addTitle("Фамилия", new Label(String.valueOf(getUserDTO().getLastName()))));
			layoutForm.addMember(
					Components.addTitle("Номер паспорта", new Label(getUserDTO().getPassportNumber())));

			layoutForm.addMember(Components.addTitle("Валюта", currenciesControl.getView()));

			HLayout buttons = new HLayout();
			buttons.addMember(createButton);

			step2Layout = new VLayout();
			step2Layout.setMembersMargin(MARGIN);
			step2Layout.addMember(layoutForm);
			step2Layout.addMember(buttons);
			step2Layout.setMargin(MARGIN);
			step2Layout.setShowResizeBar(false);
		}
		return step2Layout;
	}

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    @Override
	public void reload() {
		userEmailControl.bind("");
	}
}
