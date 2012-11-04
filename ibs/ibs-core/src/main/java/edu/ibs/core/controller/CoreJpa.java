package edu.ibs.core.controller;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @date Nov 4, 2012
 *
 * @author Vadim Martos
 */
class CoreJpa {

	private static final EntityManagerFactory factory = Persistence.createEntityManagerFactory("edu.ibs.core.jpa");
	public static final int BATCH_SIZE = 15;

	public EntityManager createEntityManager() {
		return factory.createEntityManager();
	}

	public synchronized void close() {
		if (factory.isOpen()) {
			factory.close();
		}

	}
}
