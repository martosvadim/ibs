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
public class CardsListGrid extends ListGrid {
    public CardsListGrid() {
        super();
        GWT.log("init CardsListGrid()...", null);
        this.setShowAllRecords(true);
		this.setAutoFetchData(true);
        this.setSortField(1);
		Components.localizeGrid(this);
		this.setSelectionType(SelectionStyle.SINGLE);
        this.setShowFilterEditor(true);

        ListGridField iconField = new ListGridField("icon", "#", 27);
        iconField.setImageSize(16);
        iconField.setAlign(Alignment.CENTER);
        iconField.setType(ListGridFieldType.IMAGE);
        iconField.setImageURLPrefix("icons/16/");
        iconField.setImageURLSuffix(".png");

		ListGridField bankBookId = new ListGridField("bankbook.id", "Номер банковского счёта", 150);
        ListGridField cardTypeField = new ListGridField("card.type", "Тип");
        ListGridField cardNumberField = new ListGridField("cardbook.id", "Номер");
        ListGridField currencyField = new ListGridField("currency.name", "Валюта", 50);
        ListGridField balanceField = new ListGridField("bankbook.balance", "Остаток");
//        ListGridField infoField = new ListGridField("date.expire", "Дополнительно", 180);
        this.setFields(new ListGridField[]{iconField, bankBookId, cardTypeField, cardNumberField, currencyField,
                balanceField});
		this.setDataSource(new CardsDataSource());
    }

}
