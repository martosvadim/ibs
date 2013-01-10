package edu.ibs.webui.client.admin;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import edu.ibs.common.dto.BankBookDTO;
import edu.ibs.common.dto.CardBookDTO;
import edu.ibs.common.dto.CurrencyDTO;
import edu.ibs.common.interfaces.IPaymentServiceAsync;
import edu.ibs.webui.client.ds.GwtRpcDataSource;
import edu.ibs.webui.client.utils.AppCallback;

import java.util.List;

/**
 * User: Максим
 * Date: 11.01.13
 * Time: 0:50
 */
public class BankBooksDataSource extends GwtRpcDataSource {
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
						CardBookDTO cardBookDTO = list.get(i);
                        BankBookDTO dto = cardBookDTO.getBankBook();
                        if (dto != null) {
                            ListGridRecord record = new ListGridRecord();
                            record.setAttribute("id", dto.getId());
                            record.setAttribute("balance", dto.getBalance());
                            record.setAttribute("currency", dto.getCurrency().getName());
                            record.setAttribute("etc", dto.getDescription());
                            listGridRecords[i] = record;
                        }
					}
					response.setData(listGridRecords);
				}
				response.setTotalRows(size);
				processResponse(requestId, response);
			}
		};

		service.getContragentList(callback);
	}
}
