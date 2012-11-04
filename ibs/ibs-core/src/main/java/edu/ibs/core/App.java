package edu.ibs.core;

import edu.ibs.core.controller.CSUIDJpaController;
import edu.ibs.core.entity.User;
import java.util.List;
import javax.persistence.EntityManager;
import org.apache.log4j.PropertyConfigurator;

/**
 * Hello world!
 *
 */
public class App {

	static {
		PropertyConfigurator.configure(App.class.getClassLoader().getResource("log4j.properties"));
	}

	public static void main(String[] args) throws Exception {
		CSUIDJpaController jpa = new CSUIDJpaController();
		User u = new User(User.Role.USER, "vadimk.martos@gmail.com");
		jpa.insert(u);
		EntityManager em = jpa.createEntityManager();
		em.getTransaction().begin();
		em.flush();
		User u1 = em.find(User.class, u.getId());
		u1.setFirstName("vadim");
		em.getTransaction().commit();
		List<User> list = jpa.selectAll(User.class);
		int count = jpa.count(User.class);
		u.setFirstName("vadim");
		jpa.update(u);
		u = jpa.select(User.class, u.getId());
		jpa.delete(User.class, u.getId());
		int i = 0;
	}
}
