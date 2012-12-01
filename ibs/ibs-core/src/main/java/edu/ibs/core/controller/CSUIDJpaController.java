package edu.ibs.core.controller;

import edu.ibs.core.controller.exceptions.NonexistentEntityException;
import edu.ibs.core.entity.AbstractEntity;
import java.io.Serializable;
import java.util.Iterator;
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
			em.persist(entity);
			em.getTransaction().commit();
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

	public <T extends AbstractEntity> void batchUpdate(List<T> entities) {
		EntityManager em = null;
		try {
			em = createEntityManager();
			em.getTransaction().begin();
			int i = 0;
			for (Iterator<T> it = entities.iterator(); it.hasNext(); ++i) {
				T t = it.next();
				em.merge(t);
				if (i % BATCH_SIZE == 0) {
					em.flush();
					em.clear();
				}
			}
			em.getTransaction().commit();
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

	public <T extends AbstractEntity> void update(T entity) throws NonexistentEntityException, Exception {
		EntityManager em = null;
		try {
			em = createEntityManager();
			em.getTransaction().begin();
			em.merge(entity);
			em.getTransaction().commit();
		} catch (Exception ex) {
			String msg = ex.getLocalizedMessage();
			if (msg == null || msg.length() == 0) {
				long id = entity.getId();
				if (select(entity.getClass(), id) == null) {
					throw new NonexistentEntityException("The object with id " + id + " no longer exists.");
				}
			}
			throw ex;
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

	public <T extends AbstractEntity> void delete(Class<T> clazz, long id) throws NonexistentEntityException {
		EntityManager em = null;
		T entity;
		try {
			em = createEntityManager();
			em.getTransaction().begin();
			entity = em.getReference(clazz, id);
			entity.getId();
			em.remove(entity);
			em.getTransaction().commit();
		} catch (EntityNotFoundException enfe) {
			throw new NonexistentEntityException("The request with id " + id + " no longer exists.", enfe);
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

	public <T extends AbstractEntity> boolean exist(Class<T> clazz, long id) {
		return select(clazz, id) != null;
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
