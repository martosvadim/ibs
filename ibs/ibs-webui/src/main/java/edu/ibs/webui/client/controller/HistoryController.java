package edu.ibs.webui.client.controller;

import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import edu.ibs.common.dto.CardBookDTO;
import edu.ibs.webui.client.utils.Components;

/**
 * User: Максим
 * Date: 11.01.13
 * Time: 5:26
 */
public class HistoryController extends GenericWindowController {
    private CardBookDTO cardBookDto;

    public HistoryController() {
        getWindow().setTitle("Выписка по карте");
        getWindow().setWidth100();
        getWindow().setHeight100();
        ListGrid lg = Components.getGrid();
        ListGridField fromF = new ListGridField("from", "Откуда");
        ListGridField toF = new ListGridField("to", "Куда");
        ListGridField amountF = new ListGridField("amount", "Сумма");
        ListGridField whenF = new ListGridField("date", "Дата");
        lg.setFields(new ListGridField[] {fromF, toF, amountF, whenF});
        lg.setDataSource(new HistoryDataSource());
        getWindow().addItem(lg);
    }

    public void setCardBookDTO(CardBookDTO cardBookDTO) {
        this.cardBookDto = cardBookDTO;
    }
}
