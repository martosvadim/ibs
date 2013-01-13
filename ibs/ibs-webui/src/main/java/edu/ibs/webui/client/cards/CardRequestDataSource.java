package edu.ibs.webui.client.cards;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import edu.ibs.common.dto.CardRequestDTO;
import edu.ibs.common.dto.SearchCriteriaDTO;
import edu.ibs.common.interfaces.IPaymentServiceAsync;
import edu.ibs.webui.client.ds.GwtRpcDataSource;
import edu.ibs.webui.client.utils.AppCallback;
import edu.ibs.webui.client.utils.JS;

import java.util.List;

/**
 * User: EgoshinME
 * Date: 08.01.13
 * Time: 3:52
 */
public class CardRequestDataSource extends GwtRpcDataSource {

	private IPaymentServiceAsync service = IPaymentServiceAsync.Util.getInstance();
    private String passwordNumber;

    @Override
	public void executeFetch(final String requestId, final DSRequest request, final DSResponse response) {
		AsyncCallback<List<CardRequestDTO>> callback = new AppCallback<List<CardRequestDTO>>() {
			@Override
			public void onSuccess(List<CardRequestDTO> list) {
//                List<SearchCriteriaDTO> listS = JS.getSearchCriterias(request);
				int size = 0;
				if (list != null && list.size() > 0) {
					size = list.size();
					ListGridRecord[] listGridRecords = new ListGridRecord[size];
					for (int i = 0; i < size; i++) {
						CardRequestDTO dto = list.get(i);
						ListGridRecord record = new ListGridRecord();
						record.setAttribute("id", dto.getId());
						record.setAttribute("user", dto.getUser().getFirstName() + " " + dto.getUser().getLastName()
                                + " " + dto.getUser().getPassportNumber());
						record.setAttribute("cardbooktype", dto.getType());
						record.setAttribute("bankbookid", dto.getBankBook().getId());
						record.setAttribute("cardrequestdto", dto);
						listGridRecords[i] = record;
					}
					response.setData(listGridRecords);
				}
				response.setTotalRows(size);
				processResponse(requestId, response);
			}
		};

        if (passwordNumber == null || passwordNumber.length() == 0) {
		    service.getCardRequests(callback);
        } else {
            service.getCardRequests(passwordNumber, callback);
        }
	}

    public void setPasswordNumber(String passwordNumber) {
        this.passwordNumber = passwordNumber;
    }
}
