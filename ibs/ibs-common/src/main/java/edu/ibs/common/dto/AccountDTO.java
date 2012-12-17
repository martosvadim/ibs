package edu.ibs.common.dto;

import edu.ibs.common.enums.AccountRole;

/**
 * User: EgoshinME
 * Date: 17.12.12
 * Time: 4:22
 */
public class AccountDTO implements IBaseDTO {

	private long id;
	private String email;
	private AccountRole role;
	private String password;
	private String securityQuestion;
	private String securityAnswer;
	private String avatar;
	private UserDTO user;

	public long getId() {
		return id;
	}

	public void setId(final long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public AccountRole getRole() {
		return role;
	}

	public void setRole(final AccountRole role) {
		this.role = role;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	public String getSecurityQuestion() {
		return securityQuestion;
	}

	public void setSecurityQuestion(final String securityQuestion) {
		this.securityQuestion = securityQuestion;
	}

	public String getSecurityAnswer() {
		return securityAnswer;
	}

	public void setSecurityAnswer(final String securityAnswer) {
		this.securityAnswer = securityAnswer;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(final String avatar) {
		this.avatar = avatar;
	}

	public UserDTO getUser() {
		return user;
	}

	public void setUser(final UserDTO user) {
		this.user = user;
	}
}
