package edu.ibs.webui.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import edu.ibs.common.dto.CardBookDTO;
import edu.ibs.common.interfaces.IPaymentServiceAsync;

import java.util.List;

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
        this.setSortField(1);

        ListGridField iconField = new ListGridField("icon", "#", 27);
        iconField.setImageSize(16);
        iconField.setAlign(Alignment.CENTER);
        iconField.setType(ListGridFieldType.IMAGE);
        iconField.setImageURLPrefix("icons/16/");
        iconField.setImageURLSuffix(".png");

        ListGridField cardTypeField = new ListGridField("cardType", "Тип", 320);
        ListGridField cardNumberField = new ListGridField("cardNumber", "Номер", 100);
        ListGridField currencyField = new ListGridField("currency", "Валюта", 100);
        ListGridField balanceField = new ListGridField("balance", "Остаток", 140);
        ListGridField infoField = new ListGridField("info", "Дополнительно", 180);
        ListGridField emptyField = new ListGridField("emptyField", " ");
        this.setFields(new ListGridField[]{iconField, cardTypeField, cardNumberField, currencyField,
                balanceField, infoField, emptyField});
        this.setData(AccountData.getRecords());
    }

    private void getCardBooks() {
        IPaymentServiceAsync.Util.getInstance().getCardBooks(ApplicationManager.getInstance().getAccount().getUser(),
                new AsyncCallback<List<CardBookDTO>>() {
            @Override
            public void onFailure(final Throwable throwable) {
                SC.warn(throwable.getMessage());
            }

            @Override
            public void onSuccess(final List<CardBookDTO> cardBookDTOs) {
                if (cardBookDTOs != null && cardBookDTOs.size() > 0) {
                    //todo добавить в таблицу
                }
            }
        });
    }

}
