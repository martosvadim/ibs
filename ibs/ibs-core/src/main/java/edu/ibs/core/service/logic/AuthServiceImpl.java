package edu.ibs.core.service.logic;

import edu.ibs.core.dto.UserDTO;
import edu.ibs.core.entity.User;
import edu.ibs.core.interfaces.IAuthService;
import edu.ibs.core.service.AdminServicable;
import edu.ibs.core.service.UserServicable;

import javax.persistence.PersistenceException;

public class AuthServiceImpl implements IAuthService {

    private static final UserServicable userLogic = (UserServicable) ApplicationContextProvider.provide().getBean("userLogic");
    private static final AdminServicable adminLogic = (AdminServicable) ApplicationContextProvider.provide().getBean("adminLogic");

    @Override
    public UserDTO login(String name, String pass) {
        return transform(userLogic.getUser(name, pass));
    }

    @Override
    public UserDTO register(String name, String password, String passwordConfirm, String captchaText) {
        //todo validation
        return transform(register(name, password));
    }

    private User register(String email, String passwd) throws PersistenceException {
        return adminLogic.create(User.Role.USER, email, passwd);
    }

    //todo сделать отдельный класс для конвертера?

    /**
     * Конвертация
     * @param user    пользователь
     * @return        объект для передачи данных
     */
    private UserDTO transform(final User user) {
        UserDTO dto = new UserDTO();
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setDescription(user.getDescription());
        return dto;
    }
}
