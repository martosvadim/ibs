package edu.ibs.core.data;

import edu.ibs.core.beans.User;
import edu.ibs.core.beans.User.Role;

/**
 *
 * @author Vadim Martos
 */
public interface UserDAO {

	public User create(String email, Role role);

	public boolean delete(User user);

	public boolean update(User user);
}
