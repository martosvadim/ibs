package edu.ibs.webui.client.cards;

import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import edu.ibs.common.dto.CardBookDTO;
import edu.ibs.common.interfaces.IPaymentServiceAsync;
import edu.ibs.webui.client.ds.GwtRpcDataSource;
import edu.ibs.webui.client.utils.AppCallback;

import java.util.Date;
import java.util.List;

/**
 * User: Максим
 * Date: 14.01.13
 * Time: 0:51
 */
public class UsersCardsDataSource extends GwtRpcDataSource {
    private IPaymentServiceAsync service = IPaymentServiceAsync.Util.getInstance();
    private String passportNumber;

    @Override
	public void executeFetch(final String requestId, final DSRequest request, final DSResponse response) {
        AppCallback<List<CardBookDTO>> callback = new AppCallback<List<CardBookDTO>>() {
            @Override
            public void onSuccess(List<CardBookDTO> cardBookDTOs) {
                int size = 0;
				if (cardBookDTOs != null && cardBookDTOs.size() > 0) {
					size = cardBookDTOs.size();
					ListGridRecord[] listGridRecords = new ListGridRecord[size];
					for (int i = 0; i < size; i++) {
						CardBookDTO dto = cardBookDTOs.get(i);
						ListGridRecord record = new ListGridRecord();
						record.setAttribute("cardbook.id", dto.getId());
						record.setAttribute("date.expire", dto.getDateExpire());
						record.setAttribute("card.type", dto.getType().toString());
						record.setAttribute("bankbook.id", dto.getBankBook().getId());
						record.setAttribute("bankbook.balance", dto.getBankBook().getBalance());
						record.setAttribute("currency.id", dto.getBankBook().getCurrency().getId());
						record.setAttribute("currency.name", dto.getBankBook().getCurrency().getName());
						record.setAttribute("dto", dto);
                        record.setAttribute("date.expire", new Date(dto.getDateExpire()));
                        record.setAttribute("user", dto.getBankBook().getOwner().getFirstName() + " "
                                + dto.getBankBook().getOwner().getLastName()
                                + " " + dto.getBankBook().getOwner().getPassportNumber());
                        record.setAttribute("isFreezed", dto.isFreezed());
						listGridRecords[i] = record;
					}
					response.setData(listGridRecords);
				}
				response.setTotalRows(size);
				processResponse(requestId, response);
            }
        };
        if (passportNumber == null || passportNumber.length() == 0) {
            service.getCards(callback);
        } else {
            service.getCards(passportNumber, callback);
        }
    }

    public void setPasswordNumber(String unbinded) {
        this.passportNumber = unbinded;
    }
}
