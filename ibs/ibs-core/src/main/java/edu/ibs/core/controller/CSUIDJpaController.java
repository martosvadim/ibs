package edu.ibs.core.controller;

import edu.ibs.core.controller.exceptions.NonexistentEntityException;
import edu.ibs.core.entity.AbstractEntity;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * Update, insert, delete
 *
 * @date Nov 2, 2012
 *
 * @author Vadim Martos
 */
public class CSUIDJpaController extends CoreJpa implements Serializable {

	public <T extends AbstractEntity> void insert(T entity) {
		EntityManager em = null;
		try {
			em = createEntityManager();
			em.getTransaction().begin();
			insert(em, entity);
			em.getTransaction().commit();
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

	public <T extends AbstractEntity> void insert(EntityManager em, T entity) {
		em.persist(entity);
	}

	public <T extends AbstractEntity> void update(T entity) throws NonexistentEntityException, Exception {
		EntityManager em = null;
		try {
			em = createEntityManager();
			em.getTransaction().begin();
			update(em, entity);
			em.getTransaction().commit();
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

	public <T extends AbstractEntity> void update(EntityManager em, T entity) throws NonexistentEntityException, Exception {
		try {
			entity = em.merge(entity);
		} catch (Exception ex) {
			String msg = ex.getLocalizedMessage();
			if (msg == null || msg.length() == 0) {
				long id = entity.getId();
				if (select(entity.getClass(), id) == null) {
					throw new NonexistentEntityException("The object with id " + id + " no longer exists.");
				}
			}
			throw ex;
		}
	}

	public <T extends AbstractEntity> void delete(Class<T> clazz, long id) throws NonexistentEntityException {
		EntityManager em = null;
		try {
			em = createEntityManager();
			em.getTransaction().begin();
			delete(em, clazz, id);
			em.getTransaction().commit();
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

	public <T extends AbstractEntity> void delete(EntityManager em, Class<T> clazz, long id) throws NonexistentEntityException {
		T entity;
		try {
			entity = em.getReference(clazz, id);
			entity.getId();
		} catch (EntityNotFoundException enfe) {
			throw new NonexistentEntityException("The request with id " + id + " no longer exists.", enfe);
		}
		em.remove(entity);
	}

	public <T extends AbstractEntity> List<T> selectAll(Class<T> clazz) {
		return selectAll(clazz, true, -1, -1);
	}

	public <T extends AbstractEntity> List<T> selectAll(Class<T> clazz, int maxResults, int firstResult) {
		return selectAll(clazz, false, maxResults, firstResult);
	}

	private <T extends AbstractEntity> List<T> selectAll(Class<T> clazz, boolean all, int maxResults, int firstResult) {
		EntityManager em = createEntityManager();
		try {
			CriteriaQuery<T> cq = em.getCriteriaBuilder().createQuery(clazz);
			cq.select(cq.from(clazz));
			TypedQuery<T> q = em.createQuery(cq);
			if (!all) {
				q.setMaxResults(maxResults);
				q.setFirstResult(firstResult);
			}
			return q.getResultList();
		} finally {
			em.close();
		}
	}

	public <T extends AbstractEntity> T select(Class<T> clazz, long id) {
		EntityManager em = createEntityManager();
		try {
			return em.find(clazz, id);
		} finally {
			em.close();
		}
	}

	public <T extends AbstractEntity> int count(Class<T> clazz) {
		EntityManager em = createEntityManager();
		try {
			CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
			Root<T> rt = cq.from(clazz);
			cq.select(em.getCriteriaBuilder().count(rt));
			Query q = em.createQuery(cq);
			return ((Long) q.getSingleResult()).intValue();
		} finally {
			em.close();
		}
	}
}
