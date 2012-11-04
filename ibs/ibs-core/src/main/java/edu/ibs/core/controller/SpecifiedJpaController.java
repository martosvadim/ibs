package edu.ibs.core.controller;

import edu.ibs.core.entity.*;
import edu.ibs.core.entity.Transaction.TransactionType;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.criteria.*;

/**
 *
 * @date Nov 3, 2012
 *
 * @author Vadim Martos
 */
public final class SpecifiedJpaController extends CSUIDJpaController {

	private static final SpecifiedJpaController instance = new SpecifiedJpaController();

	private SpecifiedJpaController() {
	}

	public static SpecifiedJpaController instance() {
		return instance;
	}

	public void update(List<Currency> currency) {
		EntityManager em = null;
		try {
			em = createEntityManager();
			em.getTransaction().begin();
			for (Currency c : currency) {
				em.refresh(c, LockModeType.PESSIMISTIC_READ);
			}
			int i = 0;
			for (Iterator<Currency> it = currency.iterator(); it.hasNext(); ++i) {
				Currency c = it.next();
				em.merge(c);
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

	public Transaction pay(BankBook from, BankBook to, long amount, TransactionType type) {
		EntityManager em = null;
		try {
			em = createEntityManager();
			em.getTransaction().begin();
			em.refresh(from, LockModeType.PESSIMISTIC_WRITE);
			em.refresh(to, LockModeType.PESSIMISTIC_WRITE);
			//todo fix rounding
			long money = (long) (amount * to.getCurrency().getFactor() / from.getCurrency().getFactor());
			long balance = from.getBalance();
			Transaction tr = null;
			if (balance < money) {
				return tr;
			} else {
				from.setBalance(balance - money);
				to.setBalance(to.getBalance() + amount);
				tr = new Transaction(amount, type, to.getCurrency(), to, from);
				em.persist(tr);
			}
			em.getTransaction().commit();
			return tr;
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

	public boolean rollback(Transaction tr) {
		EntityManager em = null;
		try {
			em = createEntityManager();
			em.getTransaction().begin();
			BankBook from = tr.getFrom();
			BankBook to = tr.getTo();
			em.refresh(from, LockModeType.PESSIMISTIC_WRITE);
			em.refresh(to, LockModeType.PESSIMISTIC_WRITE);
			if (to.getBalance() < tr.getAmount()) {
				return false;
			} else {
				//todo fix rounding
				long money = (long) (tr.getAmount() * to.getCurrency().getFactor() / from.getCurrency().getFactor());
				from.setBalance(from.getBalance() + money);
				to.setBalance(to.getBalance() - tr.getAmount());
			}
			em.getTransaction().commit();
			return false;
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

	public List<Request> requests(Date from, Date to) {
		EntityManager em = null;
		try {
			em = createEntityManager();
			CriteriaQuery<Request> criteria = em.getCriteriaBuilder().createQuery(Request.class);
			Root<Request> request = criteria.from(Request.class);
			Expression<Long> date = request.get("date");
			criteria.select(request).where(em.getCriteriaBuilder().between(date, from.getTime(), to.getTime()));
			return em.createQuery(criteria).getResultList();
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

	public List<SavedPayment> savedPayments(User user) {
		EntityManager em = null;
		try {
			em = createEntityManager();
			CriteriaQuery<SavedPayment> criteria = em.getCriteriaBuilder().createQuery(SavedPayment.class);
			Root<SavedPayment> payment = criteria.from(SavedPayment.class);
			Expression<User> owner = payment.get("user");
			criteria.select(payment).where(em.getCriteriaBuilder().equal(owner, user));
			return em.createQuery(criteria).getResultList();
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

	public List<Transaction> history(User owner, TransactionType type) {
		EntityManager em = null;
		try {
			em = createEntityManager();
			CriteriaBuilder builder = em.getCriteriaBuilder();
			CriteriaQuery<Transaction> criteria = builder.createQuery(Transaction.class);
			Root<Transaction> transaction = criteria.from(Transaction.class);
			Path<BankBook> from = transaction.get("from");
			Path<BankBook> to = transaction.get("to");
			criteria.multiselect(transaction, to, from).where(
					builder.and(
					builder.equal(transaction.get("type"), type),
					builder.or(
					builder.equal(to.get("owner"), owner),
					builder.equal(from.get("owner"), owner))));
			return em.createQuery(criteria).getResultList();
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

	public List<BankBook> bankBooks(User owner) {
		EntityManager em = null;
		try {
			em = createEntityManager();
			CriteriaBuilder builder = em.getCriteriaBuilder();
			CriteriaQuery<BankBook> criteria = builder.createQuery(BankBook.class);
			Root<BankBook> bankBook = criteria.from(BankBook.class);
			criteria.select(bankBook).where(builder.equal(bankBook.get("owner"), owner));
			return em.createQuery(criteria).getResultList();

		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

	public Currency currency(String name) {
		EntityManager em = null;
		try {
			em = createEntityManager();
			CriteriaBuilder builder = em.getCriteriaBuilder();
			CriteriaQuery<Currency> criteria = builder.createQuery(Currency.class);
			Root<Currency> currency = criteria.from(Currency.class);
			criteria.select(currency).where(builder.equal(currency.get("name"), name));
			return em.createQuery(criteria).getSingleResult();
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}
}
