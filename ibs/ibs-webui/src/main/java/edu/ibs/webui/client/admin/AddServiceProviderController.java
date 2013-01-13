package edu.ibs.webui.client.admin;

import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import edu.ibs.common.dto.ProviderDTO;
import edu.ibs.common.dto.UserDTO;
import edu.ibs.common.dto.VocDTO;
import edu.ibs.common.enums.ProviderField;
import edu.ibs.common.interfaces.IAuthServiceAsync;
import edu.ibs.webui.client.controller.GenericController;
import edu.ibs.webui.client.controller.GenericWindowController;
import edu.ibs.webui.client.controller.MakeTransferController;
import edu.ibs.webui.client.utils.AppCallback;
import edu.ibs.webui.client.utils.Components;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Rimsky
 * Date: 13.01.13
 * Time: 18:40
 */
public class AddServiceProviderController extends GenericWindowController {
    private final GenericController providerName = Components.getTextItem();
    private final GenericController bookDescription = Components.getTextItem();

    private static final int MAX_FIELDS = 5;

    private final List<GenericController> controllers = new ArrayList<GenericController>();

    public AddServiceProviderController(){
        getWindow().setTitle("Регистрация поставщика услуг");
        getWindow().setHeight(500);
        getWindow().setWidth(250);

        for (int i = 0; i < MAX_FIELDS; i++) {
            GenericController controller = Components.getComboBoxControll();
            List<VocDTO<String, String>> bindList = new ArrayList<VocDTO<String, String>> ();
            for (ProviderField field : ProviderField.values()) {
                bindList.add(new VocDTO<String, String>(field.toString(),
                        MakeTransferController.getProviderFieldTitle(field)));
            }
            controller.bind(bindList);
            controllers.add(controller);
        }

        final IButton saveButton = new IButton("Сохранить");
        saveButton.setWidth(80);
        saveButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent clickEvent) {
                String providerNameText = ((String) providerName.unbind());
                String bookDescriptionText = ((String) bookDescription.unbind());

                List<ProviderField> providerFields = new ArrayList<ProviderField>();
                for (int i = 0; i < MAX_FIELDS; i++) {
                    VocDTO<String, String> vocDTO = (VocDTO<String, String>) controllers.get(i).unbind();
                    if (vocDTO != null && vocDTO.getId() != null && vocDTO.getId().length() > 0) {
                        providerFields.add(ProviderField.valueOf(vocDTO.getId().toUpperCase()));
                    }
                }

                if (providerNameText == null || "".equals(providerNameText) || providerNameText.length() == 0) {
                    SC.warn("Поле 'Имя поставщика услуг' не заполнено.");
                } else if (bookDescriptionText == null || "".equals(bookDescriptionText) || bookDescriptionText.length() == 0){
                    SC.warn("Поле 'Описание счёта поставщика услуг' не заполнено.");
                } else {
                    saveButton.setDisabled(true);
                    IAuthServiceAsync.Util.getInstance().createProvider(providerNameText, bookDescriptionText, providerFields,
                            new AppCallback<Void>() {
                                @Override
                                public void onFailure(Throwable t) {
                                    super.onFailure(t);
                                    saveButton.setDisabled(false);
                                }

                                @Override
                                public void onSuccess(final Void s) {
                                    saveButton.setDisabled(false);
                                    SC.say("Новый поставщик услуг успешно зарегистрирован.");
                                    getWindow().hide();
                                }
                            });
                }
            }
        });
        VLayout layoutForm = new VLayout();
        layoutForm.setWidth100();
        layoutForm.setHeight100();

        layoutForm.addMember(Components.addTitle("Имя поставщика услуг ", providerName.getView()));
        layoutForm.addMember(Components.addTitle("Описание счёта поставщика услуг ", bookDescription.getView()));
        layoutForm.addMember(Components.addTitle("Поле 1: ", controllers.get(0).getView()));
        layoutForm.addMember(Components.addTitle("Поле 2: ", controllers.get(1).getView()));
        layoutForm.addMember(Components.addTitle("Поле 3: ", controllers.get(2).getView()));
        layoutForm.addMember(Components.addTitle("Поле 4: ", controllers.get(3).getView()));
        layoutForm.addMember(Components.addTitle("Поле 5: ", controllers.get(4).getView()));

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
