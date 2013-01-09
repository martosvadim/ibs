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

    public static final String PERSISTANCE_UTIN_NAME = "edu.ibs.core.jpa";
    private static final EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTANCE_UTIN_NAME);
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
