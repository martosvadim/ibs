package edu.ibs.webui.client.admin;

import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import edu.ibs.common.dto.ProviderDTO;
import edu.ibs.common.dto.UserDTO;
import edu.ibs.common.enums.ProviderField;
import edu.ibs.common.interfaces.IAuthServiceAsync;
import edu.ibs.webui.client.controller.GenericController;
import edu.ibs.webui.client.controller.GenericWindowController;
import edu.ibs.webui.client.utils.AppCallback;
import edu.ibs.webui.client.utils.Components;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Rimsky
 * Date: 13.01.13
 * Time: 18:40
 * To change this template use File | Settings | File Templates.
 */
public class AddServiceProviderController extends GenericWindowController {
    private final GenericController providerName = Components.getTextItem();
    private final GenericController bookDescription = Components.getTextItem();
    private final GenericController currencyList = Components.getComboBoxControll();
    private final GenericController field1 = Components.getComboBoxControll();
    private final GenericController field2 = Components.getComboBoxControll();
    private final GenericController field3 = Components.getComboBoxControll();
    private final GenericController field4 = Components.getComboBoxControll();
    private final GenericController field5 = Components.getComboBoxControll();

    public AddServiceProviderController(){
        getWindow().setTitle("Регистрация поставщика услуг");
        ProviderDTO dto = new ProviderDTO();
        ArrayList<ProviderField> fieldList = new ArrayList<ProviderField>();

        final IButton saveButton = new IButton("Сохранить");
        saveButton.setWidth(80);
        saveButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent clickEvent) {
                String providerNameText = ((String) providerName.unbind());
                String bookDescriptionText = ((String) bookDescription.unbind());
                ArrayList<ProviderField> providerFields = new ArrayList<ProviderField>();
                //добавить подтягивание из комбо боксов полей и добавление их в providerFields
                //добавить подтягивание валюты и приведение её к строке

                String currency = "BR";//   Это нужно убрать

                if (providerNameText == null || "".equals(providerNameText) || providerNameText.length() == 0) {
                    SC.warn("Поле 'Имя поставщика услуг' не заполнено.");
                } else if (bookDescriptionText == null || "".equals(bookDescriptionText) || bookDescriptionText.length() == 0){
                    SC.warn("Поле 'Описание счёта поставщика услуг' не заполнено.");
                } else if(providerFields.size() == 0){
                    SC.warn("Не выбрано ни одного поля.");
                } else {
                    saveButton.setDisabled(true);
                    IAuthServiceAsync.Util.getInstance().createProvider(providerNameText, bookDescriptionText, currency, providerFields,
                            new AppCallback<Void>() {
                                @Override
                                public void onFailure(Throwable t) {
                                    super.onFailure(t);
                                    saveButton.setDisabled(false);
                                }

                                @Override
                                public void onSuccess(final Void s) {
                                    saveButton.setDisabled(false);
                                    SC.say("Новый поставщик услуг успешно зарегистрирован");
                                    getWindow().hide();
                                }
                            });
                }
            }
        });
        VLayout layoutForm = new VLayout();
        layoutForm.setWidth100();
        layoutForm.setHeight100();

        layoutForm.addMember(Components.addTitle("Имя поставщика услуг: ", providerName.getView()));
        layoutForm.addMember(Components.addTitle("Описание счёта поставщика услуг: ", bookDescription.getView()));
        layoutForm.addMember(Components.addTitle("Валюта счёта: ", currencyList.getView()));
        layoutForm.addMember(Components.addTitle("Поле 1: ", field1.getView()));
        layoutForm.addMember(Components.addTitle("Поле 2: ", field2.getView()));
        layoutForm.addMember(Components.addTitle("Поле 3: ", field3.getView()));
        layoutForm.addMember(Components.addTitle("Поле 4: ", field4.getView()));
        layoutForm.addMember(Components.addTitle("Поле 5: ", field5.getView()));

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
}
