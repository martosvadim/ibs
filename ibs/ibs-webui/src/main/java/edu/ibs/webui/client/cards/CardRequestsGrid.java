package edu.ibs.webui.client.cards;

import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import edu.ibs.common.dto.CardBookDTO;
import edu.ibs.common.dto.CardRequestDTO;
import edu.ibs.common.interfaces.IPaymentServiceAsync;
import edu.ibs.webui.client.utils.AppCallback;
import edu.ibs.webui.client.utils.Components;

/**
 * User: EgoshinME
 * Date: 08.01.13
 * Time: 8:37
 */
public class CardRequestsGrid extends ListGrid {
	public CardRequestsGrid() {
		super();
		Components.prepareGrid(this);
		this.setShowAllRecords(false);
//        this.setShowFilterEditor(true);

		ListGridField idField = new ListGridField("id", "ID", 50);
		ListGridField userField = new ListGridField("user", "Пользователь");
		ListGridField cardBookTypeField = new ListGridField("cardbooktype", "Тип", 50);
		ListGridField bankBookIdField = new ListGridField("bankbookid", "Банковский счет");
		ListGridField approveActionField = Components.getIconGridField("approve", "Создать карт-счет",
				"toolbar/assign.png", new RecordClickHandler() {
			@Override
			public void onRecordClick(RecordClickEvent event) {
				if (event.getRecord() != null) {
					CardRequestDTO requestDTO
							= (CardRequestDTO) event.getRecord().getAttributeAsObject("cardrequestdto");
					if (requestDTO != null) {
						IPaymentServiceAsync.Util.getInstance().approveCardRequest(requestDTO, new AppCallback<CardBookDTO>() {
							@Override
							public void onSuccess(CardBookDTO cardBookDTO) {
								if (cardBookDTO != null) {
                                    String owner = "";
                                    if (cardBookDTO.getBankBook().getOwner() != null && cardBookDTO.getBankBook().getOwner().getId() != 0) {
									    owner = cardBookDTO.getBankBook().getOwner().getFirstName() + " "
                                                + cardBookDTO.getBankBook().getOwner().getLastName();
								    }
									SC.say("Создан карт счёт " + cardBookDTO.getId()
											+ " для пользователя " + owner);
									invalidateCache();
									fetchData();
								}
							}
						});
					}
				}
			}
		});
        approveActionField.setCanFilter(false);
        approveActionField.setPrompt("Создать карт-счет");
		ListGridField declineActionField = Components.getIconGridField("decline", "Удалить заявку на карт-счет",
				"toolbar/delete.png", new RecordClickHandler() {
			@Override
			public void onRecordClick(RecordClickEvent event) {
				if (event.getRecord() != null) {
					final CardRequestDTO requestDTO
							= (CardRequestDTO) event.getRecord().getAttributeAsObject("cardrequestdto");
					if (requestDTO != null) {
						IPaymentServiceAsync.Util.getInstance().declineCardRequest(requestDTO, "", new AppCallback<Void>() {
							@Override
							public void onSuccess(Void v) {
								SC.say("Отклонена заявка номер " + requestDTO.getId());
								invalidateCache();
								fetchData();
							}
						});
					}
				}
			}
		});
        declineActionField.setCanFilter(false);
        declineActionField.setPrompt("Удалить заявку на карт-счет");
		this.setFields(new ListGridField[]{idField, userField, cardBookTypeField, bankBookIdField, approveActionField,
				declineActionField});
		this.setDataSource(new CardRequestDataSource());
	}

}
