package edu.ibs.webui.client.admin;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import edu.ibs.common.dto.CurrencyDTO;
import edu.ibs.common.dto.UserDTO;
import edu.ibs.webui.client.ds.GwtRpcDataSource;
import edu.ibs.webui.client.utils.AppCallback;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Rimsky
 * Date: 12.01.13
 * Time: 21:07
 * To change this template use File | Settings | File Templates.
 */
public class UsersDataSource extends GwtRpcDataSource {
    @Override
    public void executeFetch(final String requestId, final DSRequest request, final DSResponse response) {
        AsyncCallback<List<UserDTO>> callback = new AppCallback<List<UserDTO>>() {
            @Override
            public void onSuccess(List<UserDTO> list) {
                int size = 0;
                if (list != null && list.size() > 0) {
                    size = list.size();
                    ListGridRecord[] listGridRecords = new ListGridRecord[size];
                    for (int i = 0; i < size; i++) {
                        UserDTO dto = list.get(i);
                        ListGridRecord record = new ListGridRecord();
                        record.setAttribute("firstname", dto.getFirstName());
                        record.setAttribute("surname", dto.getLastName());
                        record.setAttribute("email", dto.getEmail());
                        record.setAttribute("passport", dto.getPassportNumber());
                        listGridRecords[i] = record;
                    }
                    response.setData(listGridRecords);
                }
                response.setTotalRows(size);
                processResponse(requestId, response);
            }
        };
    }
}
