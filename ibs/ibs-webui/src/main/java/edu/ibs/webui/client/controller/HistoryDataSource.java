package edu.ibs.webui.client.controller;

import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import edu.ibs.common.dto.TransactionDTO;
import edu.ibs.common.interfaces.IPaymentServiceAsync;
import edu.ibs.webui.client.ApplicationManager;
import edu.ibs.webui.client.ds.GwtRpcDataSource;
import edu.ibs.webui.client.utils.AppCallback;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * User: Максим
 * Date: 11.01.13
 * Time: 6:18
 */
public class HistoryDataSource extends GwtRpcDataSource {

    private Date from, to;
    private IPaymentServiceAsync service = IPaymentServiceAsync.Util.getInstance();

    @Override
	public void executeFetch(final String requestId, final DSRequest request, final DSResponse response) {
        service.getHistory(ApplicationManager.getInstance().getAccount().getUser(), from, to, new AppCallback<List<TransactionDTO>>() {
            @Override
            public void onSuccess(List<TransactionDTO> list) {
                int size = 0;
				if (list != null && list.size() > 0) {
					size = list.size();
					ListGridRecord[] listGridRecords = new ListGridRecord[size];
					for (int i = 0; i < size; i++) {
						TransactionDTO dto = list.get(i);
						ListGridRecord record = new ListGridRecord();
						record.setAttribute("from", dto.getFrom().getBankBook().getId());
						record.setAttribute("to", dto.getTo().getBankBook().getId());
						record.setAttribute("amount",
                                dto.getMoney().getAmount().divide(new BigDecimal(dto.getMoney().getCurrency().getFraction().multiply()))
                                        + " " + dto.getMoney().getCurrency().getName());
						record.setAttribute("date", new Date(dto.getDate()));
                        record.setAttribute("desc", dto.getDesc());
						listGridRecords[i] = record;
					}
					response.setData(listGridRecords);
				}
				response.setTotalRows(size);
				processResponse(requestId, response);
			}
        });
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public void setTo(Date to) {
        this.to = to;
    }
}
