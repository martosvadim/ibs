package edu.ibs.webui.client.admin;

import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import edu.ibs.common.dto.UserDTO;
import edu.ibs.common.dto.VocDTO;
import edu.ibs.common.enums.AccountRole;
import edu.ibs.common.interfaces.IAuthServiceAsync;
import edu.ibs.core.operation.logic.AuthServiceImpl;
import edu.ibs.webui.client.controller.GenericController;
import edu.ibs.webui.client.controller.GenericWindowController;
import edu.ibs.webui.client.utils.AppCallback;
import edu.ibs.webui.client.utils.Components;

import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Rimsky
 * Date: 13.01.13
 * Time: 14:35
 * To change this template use File | Settings | File Templates.
 */
public class EditUserController extends GenericWindowController{
    private final GenericController firstname = Components.getTextItem();
    private final GenericController lastname = Components.getTextItem();
    private final GenericController passport = Components.getTextItem();
 //   private final GenericController email = Components.getTextItem();

    public EditUserController(final UserDTO dto) {
        getWindow().setTitle("Редактирование данных пользователя");
        firstname.bind(dto.getFirstName());
        lastname.bind(dto.getLastName());
        passport.bind(dto.getPassportNumber());
   //     email.bind(dto.getEmail());
        final IButton saveButton = new IButton("Сохранить");
        saveButton.setWidth(80);
        saveButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent clickEvent) {
                String firstnameText = ((String) firstname.unbind());
                String lastnameText = ((String) lastname.unbind());
         //       String emailText = ((String) email.unbind());
                String passportText = ((String) passport.unbind());
               // String roleTxt = ((VocDTO<String, String>) role.unbind()).getId();
               // final String roleDisplayName = ((VocDTO<String, String>) role.unbind()).getValue();
                if (firstnameText == null || "".equals(firstnameText) || firstnameText.length() == 0) {
                    SC.warn("Поле 'Имя' не заполнено.");
                } else if (lastnameText == null || "".equals(lastnameText) || lastnameText.length() == 0){
                    SC.warn("Поле 'Фамилия' не заполнено.");
                } else if (passportText == null || "".equals(passportText) || passportText.length() == 0) {
                    SC.warn("Поле '№ паспорта' не заполнено.");
             //   } else if (emailText == null || "".equals(emailText) || emailText.length() == 0) {
             //       SC.warn("Поле 'Email' не заполнено.");
                } else {
             //       dto.setEmail(emailText);
                    dto.setFirstName(firstnameText);
                    dto.setLastName(lastnameText);
                    dto.setPassportNumber(passportText);
                    saveButton.setDisabled(true);
                    IAuthServiceAsync.Util.getInstance().updateUser(dto,
                            new AppCallback<UserDTO>() {
                                @Override
                                public void onFailure(Throwable t) {
                                    super.onFailure(t);
                                    saveButton.setDisabled(false);
                                }

                                @Override
                                public void onSuccess(final UserDTO s) {
                                    saveButton.setDisabled(false);
                                    SC.say("Данные о пользователе сохранены. Имя: " + s.getFirstName() + ", фамилия: " + s.getLastName() + ", № паспорта: "
                                            + s.getPassportNumber());
                                    getWindow().hide();
                                }
                            });
                }
            }
        });
        VLayout layoutForm = new VLayout();
        layoutForm.setWidth100();
        layoutForm.setHeight100();

        layoutForm.addMember(Components.addTitle("Имя", firstname.getView()));
        layoutForm.addMember(Components.addTitle("Фамилия", lastname.getView()));
        layoutForm.addMember(Components.addTitle("№ паспорта", passport.getView()));
       // layoutForm.addMember(Components.addTitle("E-mail", email.getView()));

        HLayout buttons = new HLayout();
        buttons.addMember(saveButton);

        VLayout view = new VLayout();
        view.setMembersMargin(MARGIN);
        view.addMember(layoutForm);
        view.addMember(buttons);
        view.setMargin(MARGIN);
        view.setShowResizeBar(false);

        getWindow().addItem(view);
    }

    @Override
      public void reload() {          //Если будет использоваться, нужно добавить инициализацию значениями
        firstname.bind("");
        lastname.bind("");
     //   email.bind("");
        passport.bind("");
    }
}
