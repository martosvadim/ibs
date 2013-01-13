package edu.ibs.webui.client.controller;

import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.DateItem;
import com.smartgwt.client.widgets.form.validator.CustomValidator;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import edu.ibs.common.dto.CardBookDTO;
import edu.ibs.webui.client.utils.Components;

import java.util.Date;

/**
 * User: Максим
 * Date: 12.01.13
 * Time: 21:08
 */
public class MainHistoryController extends GenericWindowController {

    private CardBookDTO cardBookDTO;

    final DateItem fromDate = new DateItem();
    final DateItem toDate = new DateItem();
    final DynamicForm form = new DynamicForm();

    public MainHistoryController() {
        getWindow().setTitle("Выписка по карте");
        IButton createButton = new IButton("Показать");
		createButton.setWidth(80);
		createButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent clickEvent) {
                Date from = fromDate.getValueAsDate();
                Date to = toDate.getValueAsDate();
                if (to.compareTo(from) >= 0) {
                    getWindow().hide();
                    HistoryController controller = new HistoryController();
                    controller.setCardBookDTO(getCardBookDTO());
                    controller.getDataSource().setFrom(from);
                    controller.getDataSource().setTo(to);
                    controller.getWindow().draw();
                } else {
                    SC.warn("Дата, до которой выполнять фильтрацию, должна быть меньше начальной даты.");
                }
            }
        });
        VLayout layoutForm = new VLayout();
		layoutForm.setWidth100();
		layoutForm.setHeight100();

        form.setWidth(280);
        fromDate.setName("fromDate");
        fromDate.setTitle("От");
        toDate.setName("toDate");
        toDate.setTitle("До");
        form.setFields(fromDate, toDate);

        layoutForm.addMember(form);

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

    public CardBookDTO getCardBookDTO() {
        return cardBookDTO;
    }

    public void setCardBookDTO(CardBookDTO cardBookDTO) {
        this.cardBookDTO = cardBookDTO;
    }
}
