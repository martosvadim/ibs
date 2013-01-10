package edu.ibs.webui.client;

/**
 * User: Максим
 * Date: 29.10.12
 * Time: 23:00
 */

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.CloseClickEvent;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.LinkItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.layout.VStack;
import edu.ibs.common.dto.AccountDTO;
import edu.ibs.common.dto.UserDTO;
import edu.ibs.common.interfaces.IPaymentServiceAsync;
import edu.ibs.webui.client.controller.CardRequestController;
import edu.ibs.webui.client.controller.FillUserInfoController;
import edu.ibs.webui.client.controller.GenericController;
import edu.ibs.webui.client.controller.IAction;
import edu.ibs.webui.client.utils.AppCallback;
import edu.ibs.webui.client.utils.Components;


public class NavigationPane extends SectionStack {

    private static final int WEST_WIDTH = 200;
	private static final Integer MARGIN = 5;

	private VStack userInfoStack = new VStack();
	private SectionStackSection userInfoSection = new SectionStackSection("Настройки");

	public NavigationPane() {
        super();
        GWT.log("init NavigationPane()...", null);
        this.setWidth(WEST_WIDTH);
        this.setVisibilityMode(VisibilityMode.MUTEX);
        this.setShowExpandControls(false);
        this.setAnimateSections(true);

        SectionStackSection section1 = new SectionStackSection("Платежи");
        section1.setExpanded(true);
		VStack stack1 = new VStack();
		stack1.addMember(getAddPaymentLink());
		stack1.addMember(getFindPaymentLink());
		section1.setItems(stack1);

        SectionStackSection section2 = new SectionStackSection("Карты");
        section2.setExpanded(true);
		VStack stack2 = new VStack();
		stack2.addMember(getAddCardLink());
		stack2.addMember(getBankStatementLink());
		stack2.addMember(getAutoPaymentLink());
		section2.setItems(stack2);

        userInfoSection.setExpanded(true);
		defineUserInfoStack();
		userInfoSection.setItems(userInfoStack);

        this.addSection(section1);
        this.addSection(section2);
        this.addSection(userInfoSection);
    }

	private void defineUserInfoStack() {

		userInfoStack.removeMembers(userInfoStack.getMembers());

		boolean isUserFilled = false;
		AccountDTO account = ApplicationManager.getInstance().getAccount();
		UserDTO user = null;
		if (account != null) {
			user = account.getUser();
			if (user != null && user.getId() != 0l && user.getFirstName() != null) {
				isUserFilled = true;
			}
		}

		if (!isUserFilled) {
			// Если UserDTO пустой, предлагаем заполнить информацию о пользователе
			userInfoStack.addMember(getFillUserLink());
		} else {
			//Иначе выводим заполненную информацию о пользователе

			Label firstNameLbl = new Label(user.getFirstName());
			userInfoStack.addMember(Components.addTitle("Имя", firstNameLbl));

			Label lastNameLbl = new Label(user.getLastName());
			userInfoStack.addMember(Components.addTitle("Фамилия", lastNameLbl));

			Label passportNumberLbl = new Label(user.getPassportNumber());
			userInfoStack.addMember(Components.addTitle("№ пасспорта", passportNumberLbl));

		}
	}

	private Canvas getFillUserLink() {
		return getLink("Заполнить информацию о пользователе", new ClickHandler() {
			@Override
			public void onClick(ClickEvent clickEvent) {
				FillUserInfoController controller = new FillUserInfoController();
				controller.setCloseAction(new IAction<Object>() {
					@Override
					public void execute(Object data) {
						defineUserInfoStack();
//						userInfoStack.redraw();
						userInfoStack.getParentElement().redraw();
					}
				});
				controller.getWindow().draw();
			}
		});
	}

	private Canvas getAddCardLink() {
		return getLink("Заявка на карту", new ClickHandler() {
			@Override
			public void onClick(ClickEvent clickEvent) {
				CardRequestController controller = new CardRequestController();
				controller.getWindow().draw();
			}
		});
	}

	private Canvas getBankStatementLink() {
		return getLink("Выписка по карте", new ClickHandler() {
			@Override
			public void onClick(ClickEvent clickEvent) {

			}
		});
	}

	private Canvas getAutoPaymentLink() {
		return getLink("Автооплата", new ClickHandler() {
			@Override
			public void onClick(ClickEvent clickEvent) {

			}
		});
	}

	private Canvas getAddPaymentLink() {
		return getLink("Добавить", new ClickHandler() {
			@Override
			public void onClick(ClickEvent clickEvent) {
				Window wnd = Components.getWindow();
				wnd.setTitle("Добавление платежа");
				final GenericController reciepient = Components.getTextItem();
				final GenericController amount = Components.getTextItem();
				IButton payButton = new IButton("Оплатить");
				payButton.setWidth(80);
				payButton.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
					@Override
					public void onClick(com.smartgwt.client.widgets.events.ClickEvent clickEvent) {
						final String reciepientId = ((String) reciepient.unbind());
						final String amountTxt = ((String) amount.unbind());
						if (reciepientId == null || "".equals(reciepientId) || reciepientId.length() == 0) {
							SC.warn("Получатель не заполнен. Введите допустимый идентификатор.");
						} else if (amountTxt == null || "".equals(amountTxt) || amountTxt.length() == 0) {
							SC.warn("Введите корректную сумму платежа.");
						} else {
							//todo Передать соотв. параметры в метод
							IPaymentServiceAsync.Util.getInstance().pay(null, Long.parseLong(amountTxt), null,
									new AppCallback<Void>() {
								@Override
								public void onSuccess(Void aVoid) {
									//todo Платёж успешно проведён
									SC.say("Платёж успешно проведён");
								}
							});
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
				
				wnd.addItem(view);
				wnd.draw();
			}
		});
	}

	private Canvas getFindPaymentLink() {
		return getLink("Оплатить", new ClickHandler() {
			@Override
			public void onClick(ClickEvent clickEvent) {
				//todo Показать форму с платежами
//				Window wnd = Components.getWindow();
//				wnd.setTitle("Выберете платёж");
//				wnd.draw();
				SC.say("Нет сохранённых платежей.");
			}
		});
	}

	private Canvas getLink(final String title, final ClickHandler handler) {
		LinkItem linkItem = new LinkItem("link");
		linkItem.setShowTitle(false);
		linkItem.setLinkTitle(title);
		linkItem.addClickHandler(handler);

		DynamicForm form = new DynamicForm();
		form.setAutoHeight();
		form.setItems(linkItem);
		form.setColWidths("0,*");

		HStack stack = new HStack();
		Label label = new Label(" ");
		label.setHeight(18);
		label.setWidth(3);
		stack.setAutoHeight();
		stack.addMember(label);
		stack.addMember(form);
		return stack;
	}

}

