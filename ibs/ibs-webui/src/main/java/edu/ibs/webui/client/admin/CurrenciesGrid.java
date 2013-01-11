package edu.ibs.webui.client.admin;

import com.google.gwt.i18n.client.NumberFormat;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.widgets.grid.CellFormatter;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import edu.ibs.webui.client.utils.Components;

/**
 * User: EgoshinME
 * Date: 10.01.13
 * Time: 14:22
 */
public class CurrenciesGrid extends ListGrid {
	public CurrenciesGrid() {
		super();
		Components.prepareGrid(this);
		this.setShowAllRecords(true);
		ListGridField abbrField = new ListGridField("abbr", "Валюта", 50);
		ListGridField factorField = new ListGridField("factor", "Значение", 60);
        factorField.setType(ListGridFieldType.FLOAT);
        factorField.setCellFormatter(new CellFormatter() {
            public String format(Object value, ListGridRecord record, int rowNum, int colNum) {
                if(value == null) return null;
                NumberFormat nf = NumberFormat.getFormat("#,##0.00");
                try {
                    return nf.format(((Number)value).floatValue());
                } catch (Exception e) {
                    return value.toString();
                }
            }
        });
		ListGridField emptyField = new ListGridField("emptyField", " ");
		this.setFields(new ListGridField[]{abbrField, factorField, emptyField});
		this.setDataSource(new CurrenciesDataSource());
	}
}
