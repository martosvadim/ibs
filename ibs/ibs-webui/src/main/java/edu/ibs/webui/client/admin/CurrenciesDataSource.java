package edu.ibs.webui.client.admin;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import edu.ibs.common.dto.CurrencyDTO;
import edu.ibs.common.interfaces.IPaymentServiceAsync;
import edu.ibs.webui.client.ds.GwtRpcDataSource;
import edu.ibs.webui.client.utils.AppCallback;

import java.util.List;

/**
 * User: EgoshinME
 * Date: 10.01.13
 * Time: 14:23
 */
public class CurrenciesDataSource extends GwtRpcDataSource {
	private IPaymentServiceAsync service = IPaymentServiceAsync.Util.getInstance();

	private long lastUpdateTime;

	@Override
	public void executeFetch(final String requestId, final DSRequest request, final DSResponse response) {
		AsyncCallback<List<CurrencyDTO>> callback = new AppCallback<List<CurrencyDTO>>() {
			@Override
			public void onSuccess(List<CurrencyDTO> list) {
				int size = 0;
				if (list != null && list.size() > 0) {
					size = list.size();
					ListGridRecord[] listGridRecords = new ListGridRecord[size];
					for (int i = 0; i < size; i++) {
						CurrencyDTO dto = list.get(i);
						ListGridRecord record = new ListGridRecord();
						record.setAttribute("abbr", dto.getName());
						record.setAttribute("factor", dto.getFactor());
						if (dto.getLastUpdated() > getLastUpdateTime()) {
							setLastUpdateTime(dto.getLastUpdated());
						}
						listGridRecords[i] = record;
					}
					response.setData(listGridRecords);
				}
				response.setTotalRows(size);
				processResponse(requestId, response);
			}
		};

		service.getCurrencies(callback);
	}

	public long getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(long lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
}
