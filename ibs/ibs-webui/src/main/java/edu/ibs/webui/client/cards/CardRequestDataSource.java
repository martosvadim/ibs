package edu.ibs.webui.client.cards;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import edu.ibs.common.dto.CardRequestDTO;
import edu.ibs.common.interfaces.IPaymentServiceAsync;
import edu.ibs.webui.client.ds.GwtRpcDataSource;
import edu.ibs.webui.client.utils.AppCallback;

import java.util.List;

/**
 * User: EgoshinME
 * Date: 08.01.13
 * Time: 3:52
 */
public class CardRequestDataSource extends GwtRpcDataSource {

	private IPaymentServiceAsync service = IPaymentServiceAsync.Util.getInstance();

	@Override
	public void executeFetch(final String requestId, final DSRequest request, final DSResponse response) {
		AsyncCallback<List<CardRequestDTO>> callback = new AppCallback<List<CardRequestDTO>>() {
			@Override
			public void onSuccess(List<CardRequestDTO> list) {
				int size = 0;
				if (list != null && list.size() > 0) {
					size = list.size();
					ListGridRecord[] listGridRecords = new ListGridRecord[size];
					for (int i = 0; i < size; i++) {
						CardRequestDTO dto = list.get(i);
						ListGridRecord record = new ListGridRecord();
						record.setAttribute("id", dto.getId());
						record.setAttribute("user", dto.getUser().getId());
						record.setAttribute("cardbooktype", dto.getType());
						record.setAttribute("bankbookid", dto.getBankBook().getId());
						listGridRecords[i] = record;
					}
					response.setData(listGridRecords);
				}
				response.setTotalRows(size);
				processResponse(requestId, response);
			}
		};

		service.getCardRequests(callback);
	}
}
