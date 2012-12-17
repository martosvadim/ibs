package edu.ibs.core.gwt;

import edu.ibs.common.dto.AccountDTO;
import edu.ibs.common.dto.UserDTO;
import edu.ibs.core.entity.Account;
import edu.ibs.core.entity.User;

/**
 * User: EgoshinME
 * Date: 17.12.12
 * Time: 6:19
 */
public final class EntityTransformer {

	public static Account transformAccount(final AccountDTO dto) {
		//todo Сделать все поля возможноми для set?
		Account account = new Account();
		account.setAvatar(dto.getAvatar());
		account.setSecurityAnswer(dto.getSecurityAnswer());
		account.setSecurityQuestion(dto.getSecurityQuestion());
		account.setUser(transformUser(dto.getUser()));
		return account;
	}

	public static AccountDTO transformAccount(final Account account) {
		AccountDTO dto = new AccountDTO();
		dto.setId(account.getId());
		dto.setAvatar(account.getAvatar());
		dto.setEmail(account.getEmail());
		dto.setPassword(account.getPassword());
		dto.setRole(account.getRole());
		dto.setSecurityAnswer(account.getSecurityAnswer());
		dto.setSecurityQuestion(account.getSecurityQuestion());
		dto.setUser(transformUser(account.getUser()));
		return dto;
	}

	public static User transformUser(final UserDTO dto) {
		//todo Сделать все поля возможноми для set?
		User user = new User();
		user.setAddress(dto.getAddress());
		user.setDescription(dto.getDescription());
		user.setPassportScan(dto.getPassportScan());
		user.setPhone1(dto.getPhone1());
		user.setPhone2(dto.getPhone2());
		user.setPhone3(dto.getPhone3());
		user.setZipCode(dto.getZipCode());
		return user;
	}

	public static UserDTO transformUser(final User user) {
		UserDTO dto = new UserDTO();
		dto.setAddress(user.getAddress());
		dto.setDescription(user.getDescription());
		dto.setEmail(user.getDescription());
		dto.setFirstName(user.getFirstName());
		dto.setFreezed(user.isFreezed());
		dto.setId(user.getId());
		dto.setLastName(user.getLastName());
		dto.setPassportNumber(user.getPassportNumber());
		dto.setPassportScan(user.getPassportScan());
		dto.setPhone1(user.getPhone1());
		dto.setPhone2(user.getPhone2());
		dto.setPhone3(user.getPhone3());
		dto.setZipCode(user.getZipCode());
		return dto;
	}
}
