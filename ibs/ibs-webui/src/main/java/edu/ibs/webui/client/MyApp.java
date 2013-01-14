package edu.ibs.webui.client;

import com.google.gwt.core.client.EntryPoint;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.CloseClickEvent;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.LinkItem;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.layout.VStack;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import edu.ibs.common.dto.AccountDTO;
import edu.ibs.common.dto.BankBookDTO;
import edu.ibs.common.enums.AccountRole;
import edu.ibs.common.enums.ProviderField;
import edu.ibs.common.interfaces.IAuthServiceAsync;
import edu.ibs.common.interfaces.IPaymentService;
import edu.ibs.common.interfaces.IPaymentServiceAsync;
import edu.ibs.webui.client.admin.*;
import edu.ibs.webui.client.cards.CardRequestDataSource;
import edu.ibs.webui.client.cards.CardRequestsGrid;
import edu.ibs.webui.client.cards.UsersCardsDataSource;
import edu.ibs.webui.client.cards.UsersCardsGrid;
import edu.ibs.webui.client.controller.FillUserInfoController;
import edu.ibs.webui.client.controller.GenericController;
import edu.ibs.webui.client.controller.IAction;
import edu.ibs.webui.client.utils.AppCallback;
import edu.ibs.webui.client.utils.Components;
import edu.ibs.webui.client.utils.JS;

import java.util.List;

public class MyApp implements EntryPoint {

	private static final int HEADER_HEIGHT = 85;
	public static final String LOGIN_COOKIE_NAME = "ibs.login";
	public static final String IS_ADMIN_COOKIE = "ibs.admin";
	private VLayout mainLayout;
	private VLayout adminLayout;
	private Window loginWindow;
	private Window registerWindow;
	/**
	 * Основное окно
	 */
	private Canvas bg = new Canvas();
	private CurrenciesGrid currenciesGrid = new CurrenciesGrid();
    private HLayout searchByPasswordLayout1, searchByPasswordLayout2;
    private CardRequestsGrid cardRequestGrid = new CardRequestsGrid();
    private UsersCardsGrid usersCardsGrid = new UsersCardsGrid();

    public void onModuleLoad() {

		com.google.gwt.user.client.Window.enableScrolling(false);
		com.google.gwt.user.client.Window.setMargin("0px");

		bg.setWidth100();
		bg.setHeight100();

		bg.draw();
		loadUser();
	}

	private void loadUser() {
		String login = JS.getCookie(LOGIN_COOKIE_NAME);
		if (login == null || login.length() == 0) {
			login();
		} else {
			IAuthServiceAsync.Util.getInstance().login(login, "", new AppCallback<AccountDTO>() {

				@Override
				public void onFailure(final Throwable throwable) {
					super.onFailure(throwable);
					login();
				}

				@Override
				public void onSuccess(final AccountDTO dto) {
					if (dto == null) {
						login();
					} else {
						JS.setCookie(LOGIN_COOKIE_NAME, dto.getEmail());
						if (AccountRole.ADMIN.equals(dto.getRole())) {
							JS.setCookie(IS_ADMIN_COOKIE, Boolean.TRUE.toString());
						} else {
							JS.setCookie(IS_ADMIN_COOKIE, Boolean.FALSE.toString());
						}
						ApplicationManager.getInstance().setAccount(dto);
						bg.addChild(getMainLayoutForRole());
					}
				}
			});
		}
	}

	private void login() {
		getLoginWindow().show();
	}

	private Window getRegisterCanvas() {
		if (registerWindow == null) {
			registerWindow = Components.getWindow();
			registerWindow.setHeight(200);

			final GenericController login = Components.getTextItem();
			final GenericController pass = Components.getPasswordItem();
			final GenericController repPass = Components.getPasswordItem();
//			final GenericController capthcaInputTextItemView = Components.getTextItem();

			final IButton regButton = new IButton("Зарегистрироваться");
			regButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(final ClickEvent clickEvent) {
					String name = (String) login.unbind();
					String password = (String) pass.unbind();
					String passwordAgain = (String) repPass.unbind();
//					String captchaText = (String) capthcaInputTextItemView.unbind();
					if (name == null || "".equals(name) || name.length() == 0) {
						SC.warn("Почтовый адрес не заполнен.");
					} else if (password == null || "".equals(password) || password.length() == 0) {
						SC.warn("Пароль не заполнен.");
					} else if (passwordAgain == null || "".equals(passwordAgain) || passwordAgain.length() == 0) {
						SC.warn("Поле подтверждения пароля не заполнено.");
//					} else if (captchaText == null || "".equals(captchaText) || captchaText.length() == 0) {
//						SC.warn("Введите текст с картинки.");
					} else {
						regButton.setDisabled(true);
						IAuthServiceAsync.Util.getInstance().register(name, password, passwordAgain, null,
								new AppCallback<AccountDTO>() {

									@Override
									public void onFailure(Throwable t) {
										super.onFailure(t);
										regButton.setDisabled(false);
									}

									@Override
									public void onSuccess(AccountDTO s) {
										regButton.setDisabled(false);
										if (s != null) {
											JS.setCookie(LOGIN_COOKIE_NAME, s.getEmail());
											if (AccountRole.ADMIN.equals(s.getRole())) {
												JS.setCookie(IS_ADMIN_COOKIE, Boolean.TRUE.toString());
											} else {
												JS.setCookie(IS_ADMIN_COOKIE, Boolean.FALSE.toString());
											}
											ApplicationManager.getInstance().setAccount(s);
											SC.say("Вы зарегестрировались, " + s.getEmail() + "!");
											registerWindow.hide();
											bg.addChild(getMainLayoutForRole());
										}
									}
								});
					}
				}
			});
			regButton.setWidth(150);

//			final Img captchaImage = new Img("/SimpleCaptcha.jpg");
//			captchaImage.setWidth(120);
//			captchaImage.setHeight(35);

			VStack layout = new VStack();
			layout.setWidth100();
			layout.setHeight100();
			layout.addMember(Components.addTitle("E-mail", login.getView()));
			layout.addMember(Components.addTitle("Пароль", pass.getView()));
			layout.addMember(Components.addTitle("Подтверждение пароля", repPass.getView()));

//			layout.addMember(Components.addTitle("", captchaImage));
//			layout.addMember(Components.addTitle("", capthcaInputTextItemView.getView()));
			layout.setShowResizeBar(false);

			HLayout buttons = new HLayout();
			buttons.addMember(regButton);

			VLayout view = new VLayout();
			view.setMembersMargin(5);
			view.addMember(layout);
			view.addMember(buttons);
			view.setMargin(5);
			view.setShowResizeBar(false);

			registerWindow.addItem(view);
			registerWindow.addCloseClickHandler(new CloseClickHandler() {

				@Override
				public void onCloseClick(final CloseClickEvent closeClickEvent) {
					registerWindow.hide();
					getLoginWindow().show();
				}
			});

		}

		return registerWindow;
	}

	private Window getLoginWindow() {
		if (loginWindow == null) {
			loginWindow = Components.getWindow();
			loginWindow.setShowCloseButton(false);
			final GenericController login = Components.getTextItem();
			final GenericController pass = Components.getPasswordItem();

			IButton loginButton = new IButton("Войти");
			loginButton.setWidth(80);
			loginButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(final ClickEvent clickEvent) {
					final String loginText = ((String) login.unbind());
					String passText = ((String) pass.unbind());
					if (loginText == null || "".equals(loginText) || loginText.length() == 0) {
						SC.warn("Логин не заполнен. Введите допустимый логин.");
					} else if (passText == null || "".equals(passText) || passText.length() == 0) {
						SC.warn("Пароль пуст. Введите допустимый пароль.");
					} else {
						IAuthServiceAsync.Util.getInstance().login(loginText, passText,
								new AppCallback<AccountDTO>() {

									@Override
									public void onSuccess(final AccountDTO s) {
										if (s != null) {
											JS.setCookie(LOGIN_COOKIE_NAME, loginText);
											if (AccountRole.ADMIN.equals(s.getRole())) {
												JS.setCookie(IS_ADMIN_COOKIE, Boolean.TRUE.toString());
											} else {
												JS.setCookie(IS_ADMIN_COOKIE, Boolean.FALSE.toString());
											}
											ApplicationManager.getInstance().setAccount(s);
											loginWindow.hide();
											bg.addChild(getMainLayoutForRole());
										} else {
											SC.say("Доступ запрещён. Попробуйте ещё раз.");
										}
									}
								});
					}
				}
			});

			LinkItem linkItem = new LinkItem("link");
			linkItem.setShowTitle(false);
			linkItem.setLinkTitle("Регистрация");
			linkItem.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

				@Override
				public void onClick(final com.smartgwt.client.widgets.form.fields.events.ClickEvent clickEvent) {
					loginWindow.hide();
					getRegisterCanvas().show();
				}
			});
			DynamicForm regForm = new DynamicForm();
			regForm.setItems(linkItem);
			regForm.setColWidths("0,*");

			VStack layout = new VStack();
			layout.setMembersMargin(5);
			layout.setMargin(5);
			layout.addMember(Components.addTitle("E-mail", login.getView()));
			layout.addMember(Components.addTitle("Пароль", pass.getView()));

			HLayout buttons = new HLayout();
			buttons.setMembersMargin(5);
			buttons.addMember(loginButton);
			buttons.addMember(regForm);

			layout.addMember(buttons);
			layout.setShowResizeBar(false);

			loginWindow.addItem(layout);
			loginWindow.addCloseClickHandler(new CloseClickHandler() {

				@Override
				public void onCloseClick(CloseClickEvent closeClickEvent) {
					//
				}
			});

		}

		return loginWindow;
	}

	private Canvas getMainLayoutForRole() {
		boolean admin = Boolean.TRUE.toString().equals(JS.getCookie(IS_ADMIN_COOKIE));
		if (admin) {
			return getAdminLayout();
		} else {
			AccountDTO acc = ApplicationManager.getInstance().getAccount();
			if (acc != null && acc.getUser() != null && acc.getUser().getId() > 0) {
                IPaymentServiceAsync.Util.getInstance().getBankBooks(acc.getUser(), new AppCallback<List<BankBookDTO>>() {
                    @Override
                    public void onSuccess(List<BankBookDTO> bankBookDTOs) {
                        ApplicationManager.getInstance().setBankBookDTOList(bankBookDTOs);
                    }
                });
				return getMainLayout();
			} else {
				final FillUserInfoController controller = new FillUserInfoController();
				final String loginStr = ApplicationManager.getInstance().getAccount().getEmail();
				// Действие по закрытию окна на кнопку "Х"
				controller.getWindow().addCloseClickHandler(new CloseClickHandler() {

					@Override
					public void onCloseClick(CloseClickEvent closeClickEvent) {
						IAuthServiceAsync.Util.getInstance().logout(loginStr, new AppCallback<Void>() {

							@Override
							public void onSuccess(Void aVoid) {
								JS.goToLoginPage();
							}
						});
					}
				});
				// Действие при успешном закрытии окна и завершении работы с формой
				controller.setCloseAction(new IAction<Object>() {

					@Override
					public void execute(Object data) {
						bg.removeChild(controller.getWindow());
						bg.addChild(getMainLayout());
					}
				});
				return controller.getWindow();
			}
		}
	}

	private Canvas getAdminLayout() {
		if (adminLayout == null) {
			adminLayout = new VLayout();
			adminLayout.setWidth100();
			adminLayout.setHeight100();

			VLayout vLayout = new VLayout();
			vLayout.setHeight(HEADER_HEIGHT);
			vLayout.addMember(new Masthead());

			VLayout links = new VLayout();
			links.setWidth("20%");
			final String adminLinkStyleName = "label-link-admin";

			Label addUser = new Label("Добавить пользователя");
			addUser.setStyleName(adminLinkStyleName);
            addUser.setHeight(60);
			final ClickHandler addUserClickHandler = new ClickHandler() {

				@Override
				public void onClick(ClickEvent clickEvent) {
					CreateNewUserController controller = new CreateNewUserController();
					controller.getWindow().draw();
				}
			};
			addUser.addClickHandler(addUserClickHandler);
			links.addMember(addUser);

			Label editUser = new Label("Редактировать данные пользователя");
			editUser.setStyleName(adminLinkStyleName);
            editUser.setHeight(60);
			final ClickHandler editUserClickHandler = new ClickHandler() {

				@Override
				public void onClick(ClickEvent clickEvent) {
					SearchUserController controller = new SearchUserController();
					controller.getWindow().draw();
				}
			};
			editUser.addClickHandler(editUserClickHandler);
			links.addMember(editUser);

			Label createBankBook = new Label("Создать банковский счёт");
			createBankBook.setStyleName(adminLinkStyleName);
            createBankBook.setHeight(60);
			final ClickHandler createBankBookClickHandler = new ClickHandler() {

				@Override
				public void onClick(ClickEvent clickEvent) {
					CreateBankBookController controller = new CreateBankBookController();
					controller.getWindow().draw();
				}
			};
			createBankBook.addClickHandler(createBankBookClickHandler);
			links.addMember(createBankBook);

			Label addMoney = new Label("Пополнить счёт");
			addMoney.setStyleName(adminLinkStyleName);
            addMoney.setHeight(60);
			final ClickHandler addMoneyClickHandler = new ClickHandler() {

				@Override
				public void onClick(ClickEvent clickEvent) {
					AddMoneyController controller = new AddMoneyController();
					controller.getWindow().draw();
				}
			};
			addMoney.addClickHandler(addMoneyClickHandler);
			links.addMember(addMoney);


            Label addServiceProvider = new Label("Зарегистрировать нового поставщика услуг");
            addServiceProvider.setStyleName(adminLinkStyleName);
            addServiceProvider.setHeight(60);
            final ClickHandler addServiceProviderClickHandler = new ClickHandler() {

                @Override
                public void onClick(ClickEvent clickEvent) {
                    AddServiceProviderController controller = new AddServiceProviderController();
                    controller.getWindow().draw();
                }
            };
            addServiceProvider.addClickHandler(addServiceProviderClickHandler);
            links.addMember(addServiceProvider);


			Label refreshCurrencies = new Label("Обновить курсы валют");
			refreshCurrencies.setStyleName(adminLinkStyleName);
            refreshCurrencies.setHeight(60);
			final ClickHandler rcCH = new ClickHandler() {

				@Override
				public void onClick(ClickEvent clickEvent) {
					RefreshCurrenciesController controller = new RefreshCurrenciesController();
                    controller.getWindow().draw();
				}
			};
			refreshCurrencies.addClickHandler(rcCH);
			links.addMember(refreshCurrencies);

			VLayout adminContentLayout = new VLayout();
			adminContentLayout.setWidth100();

			VLayout currenciesLayout = new VLayout();
			currenciesLayout.setWidth(180);
			currenciesLayout.setHeight("30%");
			currenciesLayout.setMargin(20);
			currenciesLayout.addMember(currenciesGrid);

			final TabSet topTabSet = new TabSet();
			topTabSet.setTabBarPosition(Side.BOTTOM);
			topTabSet.setWidth100();
			topTabSet.setHeight100();

			Tab tTab1 = new Tab("Заявки", "icons/16/contacts.png");
            VLayout pane1Layout = new VLayout();
            pane1Layout.setMembersMargin(5);
            pane1Layout.addMember(getSearchByPasswordLayout1());
            pane1Layout.addMember(cardRequestGrid);
			tTab1.setPane(pane1Layout);

			Tab tTab2 = new Tab("Счета", "icons/16/datamanagement.png");
			tTab2.setPane(new BankBooksGrid());

            Tab tTab3 = new Tab("Карты", "icons/16/sales.png");
            VLayout pane2Layout = new VLayout();
            pane2Layout.setMembersMargin(5);
            pane2Layout.addMember(getSearchByPasswordLayout2());
            usersCardsGrid.setHeight100();
            pane2Layout.addMember(usersCardsGrid);
			tTab3.setPane(pane2Layout);

			topTabSet.addTab(tTab1);
			topTabSet.addTab(tTab2);
            topTabSet.addTab(tTab3);

			adminContentLayout.addMember(topTabSet);

			HLayout view = new HLayout();
			view.addMember(links);
			view.addMember(adminContentLayout);
			view.addMember(currenciesLayout);

			adminLayout.addMember(vLayout);
			adminLayout.addMember(view);
			adminLayout.setStyleName("crm-ContextArea");
		}

		return adminLayout;
	}

    private Canvas getSearchByPasswordLayout1() {
        if (searchByPasswordLayout1 == null) {
            searchByPasswordLayout1 = new HLayout();
            searchByPasswordLayout1.setWidth("50%");
            searchByPasswordLayout1.setMembersMargin(5);
            IButton searchBtn = new IButton("Поиск");
            final GenericController searchText = Components.getTextItem();
            searchBtn.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent clickEvent) {
                    String unbinded = (String) searchText.unbind();
                    if (ProviderField.PASSPORT.validate(unbinded)) {
                        ((CardRequestDataSource) cardRequestGrid.getDataSource()).setPasswordNumber(unbinded);
                        cardRequestGrid.invalidateCache();
                        cardRequestGrid.fetchData();
                    } else {
                        SC.say(ProviderField.PASSPORT.validPattern());
                    }
                }
            });
            searchByPasswordLayout1.addMember(Components.addTitle("№ пасспорта", searchText.getView()));
            searchByPasswordLayout1.addMember(searchBtn);
        }
        return searchByPasswordLayout1;
    }

    private Canvas getSearchByPasswordLayout2() {
        if (searchByPasswordLayout2 == null) {
            searchByPasswordLayout2 = new HLayout();
            searchByPasswordLayout2.setWidth("50%");
            searchByPasswordLayout2.setMembersMargin(5);
            IButton searchBtn = new IButton("Поиск");
            final GenericController searchText = Components.getTextItem();
            searchBtn.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent clickEvent) {
                    String unbinded = (String) searchText.unbind();
                    if (ProviderField.PASSPORT.validate(unbinded)) {
                        ((UsersCardsDataSource) usersCardsGrid.getDataSource()).setPasswordNumber(unbinded);
                        usersCardsGrid.invalidateCache();
                        usersCardsGrid.fetchData();
                    } else {
                        SC.say(ProviderField.PASSPORT.validPattern());
                    }
                }
            });
            searchByPasswordLayout2.addMember(Components.addTitle("№ пасспорта", searchText.getView()));
            searchByPasswordLayout2.addMember(searchBtn);
        }
        return searchByPasswordLayout2;
    }

	private Canvas getMainLayout() {
		if (mainLayout == null) {

			mainLayout = new VLayout();
			mainLayout.setWidth100();
			mainLayout.setHeight100();

			HLayout northLayout = new HLayout();

			VLayout vLayout = new VLayout();
			vLayout.addMember(new Masthead());

			northLayout.addMember(vLayout);

			VLayout westLayout = new NavigationPane();
			westLayout.setWidth("15%");
			westLayout.setHeight("40%");

			VLayout eastLayout = new AccountView();
			eastLayout.setWidth("85%");

			((NavigationPane) westLayout).setAccountView((AccountView) eastLayout);

			HLayout southLayout = new HLayout();
			southLayout.setMembers(westLayout, eastLayout);
			southLayout.setHeight100();

			mainLayout.addMember(northLayout);
			mainLayout.addMember(southLayout);
		}

		return mainLayout;
	}
}