package edu.ibs.webui.client.cards;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.grid.CellFormatter;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import edu.ibs.common.dto.CardBookDTO;
import edu.ibs.common.dto.CardRequestDTO;
import edu.ibs.common.interfaces.IPaymentServiceAsync;
import edu.ibs.webui.client.utils.AppCallback;
import edu.ibs.webui.client.utils.Components;

/**
 * User: Максим
 * Date: 14.01.13
 * Time: 0:50
 */
public class UsersCardsGrid extends ListGrid {
    public UsersCardsGrid() {
        super();
        this.setShowAllRecords(true);
		this.setAutoFetchData(true);
        this.setSortField(1);
		Components.localizeGrid(this);

        ListGridField bankBookId = new ListGridField("bankbook.id", "Номер банковского счёта", 150);
        ListGridField ownerField = new ListGridField("user", "Владелец");
        ListGridField cardNumberField = new ListGridField("cardbook.id", "Номер карт-счета");
        ListGridField currencyField = new ListGridField("currency.name", "Валюта", 50);
        ListGridField balanceField = new ListGridField("bankbook.balance", "Остаток");
        ListGridField infoField = new ListGridField("date.expire", "Срок действия карты", 180);
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

        ListGridField iconField = new ListGridField("icon", " ", 27);
        iconField.setImageSize(16);
        iconField.setAlign(Alignment.CENTER);
        iconField.setType(ListGridFieldType.IMAGE);
        iconField.setImageURLPrefix("toolbar/");
        iconField.setImageURLSuffix(".png");
        iconField.setPrompt("Заблокировать карт-счет");
        iconField.addRecordClickHandler(new RecordClickHandler() {
            @Override
            public void onRecordClick(RecordClickEvent event) {
                if (event.getRecord() != null) {
                    CardBookDTO cardBookDTO = (CardBookDTO) event.getRecord().getAttributeAsObject("dto");
                    if (cardBookDTO != null) {
                        if (event.getRecord().getAttributeAsBoolean("freezed")) {
                            IPaymentServiceAsync.Util.getInstance().unfreeze(cardBookDTO, new AppCallback<Void>() {
                                @Override
                                public void onSuccess(Void v) {

                                    invalidateCache();
                                    fetchData();
                                }
                            });
                        } else {
                            IPaymentServiceAsync.Util.getInstance().freeze(cardBookDTO, new AppCallback<Void>() {
                                @Override
                                public void onSuccess(Void v) {

                                    invalidateCache();
                                    fetchData();
                                }
                            });
                        }
                    }
                }
            }
        });

        this.setFields(new ListGridField[]{bankBookId, ownerField, cardNumberField, currencyField, balanceField,
            infoField, iconField});
		this.setDataSource(new UsersCardsDataSource());
    }
}
