package edu.ibs.core.operation.logic;

import edu.ibs.common.dto.CardBookDTO;
import edu.ibs.common.dto.MoneyDTO;
import edu.ibs.common.dto.UserDTO;
import edu.ibs.common.exceptions.IbsServiceException;
import edu.ibs.common.interfaces.IPaymentService;
import edu.ibs.core.entity.CardBook;
import edu.ibs.core.entity.User;
import edu.ibs.core.gwt.EntityTransformer;
import edu.ibs.core.operation.AdminOperations;
import edu.ibs.core.operation.UserOperations;

import java.util.LinkedList;
import java.util.List;

/**
 * User: EgoshinME
 * Date: 27.12.12
 * Time: 4:30
 */
public class PaymentServiceImpl implements IPaymentService {

    private UserOperations userLogic;
	private AdminOperations adminLogic;

	@Override
	public void pay(CardBookDTO from, long to, MoneyDTO money) throws IbsServiceException {

	}

    @Override
    public List<CardBookDTO> getCardBooks(final UserDTO userDto) throws IbsServiceException {
        User user = new User(userDto);
        List<CardBookDTO> list = new LinkedList<CardBookDTO>();
        try {
            for (CardBook cardBook : getUserLogic().getCardBooks(user)) {
                list.add(EntityTransformer.transformCardBook(cardBook));
            }
        } catch (Throwable t) {
            throw new IbsServiceException("Ошибка при получении списка карт.");
        }
        return list;
    }

    public UserOperations getUserLogic() {
        return userLogic;
    }

    public void setUserLogic(final UserOperations userLogic) {
        this.userLogic = userLogic;
    }

    public AdminOperations getAdminLogic() {
        return adminLogic;
    }

    public void setAdminLogic(final AdminOperations adminLogic) {
        this.adminLogic = adminLogic;
    }
}
