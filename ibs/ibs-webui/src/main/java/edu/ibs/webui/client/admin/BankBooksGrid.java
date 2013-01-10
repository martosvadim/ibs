package edu.ibs.webui.client.admin;

import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import edu.ibs.webui.client.utils.Components;

/**
 * User: Максим
 * Date: 11.01.13
 * Time: 0:49
 */
public class BankBooksGrid extends ListGrid {
    public BankBooksGrid() {
        super();
		Components.prepareGrid(this);
		this.setShowAllRecords(true);
		ListGridField idField = new ListGridField("id", "ID", 100);
		ListGridField balanceField = new ListGridField("balance", "Баланс", 100);
        ListGridField currencyField = new ListGridField("currency", "Валюта", 100);
        ListGridField ownerField = new ListGridField("etc", "Дополнительно", 100);
		ListGridField emptyField = new ListGridField("emptyField", " ");
		this.setFields(new ListGridField[]{idField, balanceField, currencyField, ownerField, emptyField});
		this.setDataSource(new BankBooksDataSource());
    }
}
