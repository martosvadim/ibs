package edu.ibs.webui.client.admin;

import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
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
		ListGridField emptyField = new ListGridField("emptyField", " ");
		this.setFields(new ListGridField[]{abbrField, factorField, emptyField});
		this.setDataSource(new CurrenciesDataSource());
	}
}
