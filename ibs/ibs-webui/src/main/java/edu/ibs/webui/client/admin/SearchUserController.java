package edu.ibs.webui.client.admin;

import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import edu.ibs.common.dto.UserDTO;
import edu.ibs.common.interfaces.IAuthServiceAsync;
import edu.ibs.webui.client.controller.GenericController;
import edu.ibs.webui.client.controller.GenericWindowController;
import edu.ibs.webui.client.utils.AppCallback;
import edu.ibs.webui.client.utils.Components;

/**
 * Created with IntelliJ IDEA.
 * User: Rimsky
 * Date: 13.01.13
 * Time: 15:21
 * To change this template use File | Settings | File Templates.
 */
public class SearchUserController extends GenericWindowController {
    private final GenericController passport = Components.getTextItem();

    public SearchUserController(){
        getWindow().setTitle("Поиск пользователя");
        final IButton searchButton = new IButton("Найти");
        searchButton.setWidth(80);
        searchButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent clickEvent) {
                String passportText = ((String) passport.unbind());
                if (passportText == null || "".equals(passportText) || passportText.length() == 0) {
                    SC.warn("Поле '№ паспорта' не заполнено.");
                } else {
                    searchButton.setDisabled(true);
                    IAuthServiceAsync.Util.getInstance().getUserByPassport(passportText, new AppCallback<UserDTO>() {
                        @Override
                        public void onFailure(Throwable t) {
                            super.onFailure(t);
                            searchButton.setDisabled(false);
                        }

                        @Override
                        public void onSuccess(final UserDTO s) {
                            searchButton.setDisabled(false);
                            EditUserController controller = new EditUserController(s);
                            controller.getWindow().draw();
                            getWindow().hide();
                        }
                    });
                }
            }
        });
        VLayout layoutForm = new VLayout();
        layoutForm.setWidth100();
        layoutForm.setHeight100();

        layoutForm.addMember(Components.addTitle("№ паспорта", passport.getView()));

        HLayout buttons = new HLayout();
        buttons.addMember(searchButton);

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
        passport.bind("");
    }
}
