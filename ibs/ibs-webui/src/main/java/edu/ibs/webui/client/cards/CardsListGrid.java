package edu.ibs.webui.client.cards;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.grid.CellFormatter;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import edu.ibs.webui.client.utils.Components;

/**
 * User: Максим Date: 29.10.12 Time: 23:02
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
//        this.setShowFilterEditor(true);

		ListGridField iconField = new ListGridField("icon", "#", 27);
		iconField.setImageSize(16);
		iconField.setAlign(Alignment.CENTER);
		iconField.setType(ListGridFieldType.IMAGE);
		iconField.setImageURLPrefix("icons/16/");
		iconField.setImageURLSuffix(".png");

		ListGridField bankBookId = new ListGridField("bankbook.id", "Номер банковского счёта", 150);
		ListGridField cardTypeField = new ListGridField("card.type", "Тип");
		ListGridField cardNumberField = new ListGridField("cardbook.id", "Номер карт-счета");
		ListGridField currencyField = new ListGridField("currency.name", "Валюта", 50);
		ListGridField balanceField = new ListGridField("bankbook.balance", "Остаток");
		ListGridField infoField = new ListGridField("date.expire", "Срок действия карты", 180);
		ListGridField freezedField = new ListGridField("cardbook.freezed", "Заморожена");
		infoField.setType(ListGridFieldType.DATE);
		infoField.setCanFilter(false);
		infoField.setCellFormatter(new CellFormatter() {

			@Override
			public String format(Object arg0, ListGridRecord arg1, int arg2, int arg3) {
				DateTimeFormat fmt = DateTimeFormat.getFormat("dd.MM.yyyy");
				if (arg1.getAttributeAsDate("date.expire") != null) {
					return fmt.format(arg1.getAttributeAsDate("date.expire"));
				} else {
					return "";
				}
			}
		});
		this.setFields(new ListGridField[]{iconField, bankBookId, cardTypeField, cardNumberField, currencyField, infoField,
					balanceField, freezedField});
		this.setDataSource(new CardsDataSource());
	}
}
