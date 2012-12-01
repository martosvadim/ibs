package edu.ibs.core.controller;

import edu.ibs.core.entity.Transaction.TransactionType;
import edu.ibs.core.entity.*;
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

	public Transaction pay(BankBook from, BankBook to, Money money, TransactionType type) {
		EntityManager em = null;
		try {
			Transaction tr = null;
			em = createEntityManager();
			em.getTransaction().begin();
			/*
			 * select for update lock
			 */
			from = em.find(BankBook.class, from.getId(), LockModeType.PESSIMISTIC_WRITE);
			to = em.find(BankBook.class, to.getId(), LockModeType.PESSIMISTIC_WRITE);
			//todo check for converting
			if (from.ge(money)) {
				from.subtract(money);
				to.add(money);
				tr = new Transaction(money, type, from, to);
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

	public User user(String email, String passwd) {
		EntityManager em = null;
		try {
			em = createEntityManager();
			CriteriaQuery<User> criteria = em.getCriteriaBuilder().createQuery(User.class);
			CriteriaBuilder cb = em.getCriteriaBuilder();
			Root<User> user = criteria.from(User.class);
			Expression<String> emailExpr = user.get("email");
			Expression<String> passwdExpr = user.get("password");
			criteria.select(user).where(cb.and(cb.equal(emailExpr, email), cb.equal(passwdExpr, passwd)));
			return em.createQuery(criteria).getSingleResult();
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

	public Transaction rollback(Transaction tr) {
		EntityManager em = null;
		try {
			Transaction rollback = null;
			em = createEntityManager();
			em.getTransaction().begin();
			tr = em.find(Transaction.class, tr.getId(), LockModeType.PESSIMISTIC_WRITE);
			BankBook from = tr.getFrom();
			BankBook to = tr.getTo();
			from = em.find(BankBook.class, from.getId(), LockModeType.PESSIMISTIC_WRITE);
			to = em.find(BankBook.class, to.getId(), LockModeType.PESSIMISTIC_WRITE);
			// todo check for convertation
			if (to.ge(tr.getMoney())) {
				from.add(tr.getMoney());
				to.subtract(tr.getMoney());
				rollback = new Transaction(tr.getMoney(), tr.getType(), to, from);
				em.persist(rollback);
			}
			em.getTransaction().commit();
			return rollback;
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

	public void addMoney(BankBook bankBook, Money money) {
		EntityManager em = null;
		try {
			em = createEntityManager();
			em.getTransaction().begin();
			em.refresh(bankBook, LockModeType.PESSIMISTIC_WRITE);
			bankBook.add(money);
			em.getTransaction().commit();
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}
}
