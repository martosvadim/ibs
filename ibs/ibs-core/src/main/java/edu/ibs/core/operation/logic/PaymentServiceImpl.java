package edu.ibs.core.operation.logic;

import edu.ibs.common.dto.CardBookDTO;
import edu.ibs.common.dto.MoneyDTO;
import edu.ibs.common.exceptions.IbsServiceException;
import edu.ibs.common.interfaces.IPaymentService;

/**
 * User: EgoshinME
 * Date: 27.12.12
 * Time: 4:30
 */
public class PaymentServiceImpl implements IPaymentService {
	@Override
	public void pay(CardBookDTO from, long to, MoneyDTO money) throws IbsServiceException {

	}
}
