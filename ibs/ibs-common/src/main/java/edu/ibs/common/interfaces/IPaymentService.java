package edu.ibs.common.interfaces;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import edu.ibs.common.dto.CardBookDTO;
import edu.ibs.common.dto.MoneyDTO;
import edu.ibs.common.exceptions.IbsServiceException;

/**
 * User: EgoshinME
 * Date: 27.12.12
 * Time: 4:27
 */
@RemoteServiceRelativePath("pay.rpc")
public interface IPaymentService extends RemoteService {
	void pay(CardBookDTO from, long to, MoneyDTO money) throws IbsServiceException;
}
