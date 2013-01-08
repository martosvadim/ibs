package edu.ibs.webui.client.cards;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import edu.ibs.common.dto.CardBookDTO;
import edu.ibs.common.dto.UserDTO;
import edu.ibs.common.interfaces.IPaymentServiceAsync;
import edu.ibs.webui.client.ApplicationManager;
import edu.ibs.webui.client.ds.GwtRpcDataSource;
import edu.ibs.webui.client.utils.AppCallback;

import java.util.List;

/**
 * User: EgoshinME
 * Date: 03.01.13
 * Time: 6:00
 */
public class CardsDataSource extends GwtRpcDataSource {

	private IPaymentServiceAsync service = IPaymentServiceAsync.Util.getInstance();

	@Override
	public void executeFetch(final String requestId, final DSRequest request, final DSResponse response) {
		AsyncCallback<List<CardBookDTO>> callback = new AppCallback<List<CardBookDTO>>() {
			@Override
			public void onSuccess(List<CardBookDTO> list) {
				int size = 0;
				if (list != null && list.size() > 0) {
					size = list.size();
					ListGridRecord[] listGridRecords = new ListGridRecord[size];
					for (int i = 0; i < size; i++) {
						CardBookDTO dto = list.get(i);
						ListGridRecord record = new ListGridRecord();
						record.setAttribute("cardbook.id", dto.getId());
						record.setAttribute("date.expire", dto.getDateExpire());
						record.setAttribute("card.type", dto.getType().toString());
						record.setAttribute("bankbook.id", dto.getBankBook().getId());
						record.setAttribute("bankbook.balance", dto.getBankBook().getBalance());
						record.setAttribute("currency.id", dto.getBankBook().getCurrency().getId());
						record.setAttribute("currency.name", dto.getBankBook().getCurrency().getName());
						listGridRecords[i] = record;
					}
					response.setData(listGridRecords);
				}
				response.setTotalRows(size);
				processResponse(requestId, response);
			}
		};

		UserDTO userDTO = ApplicationManager.getInstance().getAccount().getUser();
		if (userDTO != null && userDTO.getId() > 0) {
			service.getCardBooks(userDTO, callback);
		}
	}
}
