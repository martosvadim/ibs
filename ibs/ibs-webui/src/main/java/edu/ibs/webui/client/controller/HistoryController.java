package edu.ibs.webui.client.controller;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.widgets.grid.CellFormatter;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import edu.ibs.common.dto.CardBookDTO;
import edu.ibs.webui.client.utils.Components;

/**
 * User: Максим
 * Date: 11.01.13
 * Time: 5:26
 */
public class HistoryController extends GenericWindowController {
    private CardBookDTO cardBookDto;
    private HistoryDataSource dataSource = new HistoryDataSource();

    public HistoryController() {
        getWindow().setCanDrag(true);
        getWindow().setTitle("Выписка по карте");
        getWindow().setWidth100();
        getWindow().setHeight100();
        ListGrid lg = Components.getGrid();
        ListGridField fromF = new ListGridField("from", "Откуда");
        ListGridField toF = new ListGridField("to", "Куда");
        ListGridField amountF = new ListGridField("amount", "Сумма");
        ListGridField descF = new ListGridField("desc", "Данные");
        ListGridField whenF = new ListGridField("date", "Дата");
        whenF.setType(ListGridFieldType.DATE);
        whenF.setCellFormatter(new CellFormatter() {
            @Override
            public String format(Object arg0, ListGridRecord arg1, int arg2, int arg3) {
                DateTimeFormat fmt = DateTimeFormat.getFormat("dd.MM.yyyy HH:mm");
                return fmt.format(arg1.getAttributeAsDate("date"));
            }
        });
        lg.setFields(new ListGridField[] {fromF, toF, amountF, whenF, descF});
        lg.setDataSource(dataSource);
        getWindow().addItem(lg);
    }

    public void setCardBookDTO(CardBookDTO cardBookDTO) {
        this.cardBookDto = cardBookDTO;
    }

    public HistoryDataSource getDataSource() {
        return dataSource;
    }
}
