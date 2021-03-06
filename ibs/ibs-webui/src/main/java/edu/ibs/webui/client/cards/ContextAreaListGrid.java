package edu.ibs.webui.client.cards;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import edu.ibs.webui.client.utils.Components;

/**
 * User: Максим
 * Date: 29.10.12
 * Time: 23:02
 */
public class ContextAreaListGrid extends ListGrid {
    public ContextAreaListGrid() {
        super();
        GWT.log("init ContextAreaListGrid()...", null);
        this.setShowAllRecords(true);
		this.setAutoFetchData(true);
        this.setSortField(1);
		Components.localizeGrid(this);
		this.setSelectionType(SelectionStyle.SINGLE);

        ListGridField iconField = new ListGridField("icon", "#", 27);
        iconField.setImageSize(16);
        iconField.setAlign(Alignment.CENTER);
        iconField.setType(ListGridFieldType.IMAGE);
        iconField.setImageURLPrefix("icons/16/");
        iconField.setImageURLSuffix(".png");

		ListGridField bankBookId = new ListGridField("bankbook.id", "Номер банковского счёта", 150);
        ListGridField cardTypeField = new ListGridField("card.type", "Тип", 320);
        ListGridField cardNumberField = new ListGridField("cardbook.id", "Номер", 100);
        ListGridField currencyField = new ListGridField("currency.name", "Валюта", 100);
        ListGridField balanceField = new ListGridField("bankbook.balance", "Остаток", 140);
//        ListGridField infoField = new ListGridField("date.expire", "Дополнительно", 180);
        ListGridField emptyField = new ListGridField("emptyField", " ");
        this.setFields(new ListGridField[]{iconField, bankBookId, cardTypeField, cardNumberField, currencyField,
                balanceField, emptyField});
		this.setDataSource(new CardsDataSource());
    }

}
