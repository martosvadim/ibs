package edu.ibs.core.controller;

import edu.ibs.common.dto.TransactionType;
import edu.ibs.core.controller.exception.FreezedException;
import edu.ibs.core.controller.exception.NotEnoughMoneyException;
import edu.ibs.core.entity.*;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceException;
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

	private MoneyEntity forTypeOf(CardBook card, EntityManager em) {
		switch (card.getType()) {
			case CREDIT: {
				Credit fromCredit = card.getCredit();
				return em.find(Credit.class, fromCredit.getId(), LockModeType.PESSIMISTIC_WRITE);
			}
			case DEBIT: {
				BankBook fromBankBook = card.getBankBook();
				return em.find(BankBook.class, fromBankBook.getId(), LockModeType.PESSIMISTIC_WRITE);
			}
			default: {
				throw new IllegalArgumentException(String.format("Unknown card type: %s", card.getType()));
			}
		}
	}

	public Transaction pay(CardBook from, CardBook to, Money money, TransactionType type) throws IllegalArgumentException, FreezedException, NotEnoughMoneyException {
		EntityManager em = null;
		try {
			Transaction tr = null;
			em = createEntityManager();
			em.getTransaction().begin();
			from = em.find(from.getClass(), from.getId());
			to = em.find(to.getClass(), to.getId());
			if (from.isFreezed()) {
				em.getTransaction().rollback();
				throw new FreezedException(String.format("CardBook %s is freezed", from));
			} else if (to.isFreezed()) {
				em.getTransaction().rollback();
				throw new FreezedException(String.format("CardBook %s is freezed", to));
			} else {
				MoneyEntity fromEntity = forTypeOf(from, em);
				MoneyEntity toEntity = forTypeOf(to, em);
				if (fromEntity == null) {
					em.getTransaction().rollback();
					throw new IllegalArgumentException(String.format("Bad card book %s: has no payment entity", from));
				} else if (toEntity == null) {
					em.getTransaction().rollback();
					throw new IllegalArgumentException(String.format("Bad card book %s: has no payment entity", to));
				} else {
					if (fromEntity.ge(money)) {
						fromEntity.subtract(money);
						toEntity.add(money);
						tr = new Transaction(money, type, from, to);
						em.persist(tr);
					} else {
						em.getTransaction().rollback();
						throw new NotEnoughMoneyException(String.format("Not enough money at card book %s, should has at least %s", from, money));
					}
				}
			}
			em.getTransaction().commit();
			return tr;
		} catch (PersistenceException e) {
			if (em != null) {
				em.getTransaction().rollback();
			}
			throw e;
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

	public Transaction rollback(Transaction tr) throws IllegalArgumentException, FreezedException, NotEnoughMoneyException {
		EntityManager em = null;
		try {
			em = createEntityManager();
			em.getTransaction().begin();
			tr = em.find(Transaction.class, tr.getId(), LockModeType.PESSIMISTIC_WRITE);
			if (tr == null) {
				em.getTransaction().rollback();
				throw new IllegalArgumentException(String.format("Transaction with id %s doesn't exist", tr.getId()));
			}
			Transaction rollback = pay(tr.getTo(), tr.getFrom(), tr.getMoney(), TransactionType.PAYMENT);
			em.getTransaction().commit();
			return rollback;
		} catch (PersistenceException e) {
			if (em != null) {
				em.getTransaction().rollback();
			}
			throw e;
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

	public Account getUserAccount(String email, String password) {
		EntityManager em = null;
		try {
			em = createEntityManager();
			CriteriaQuery<Account> criteria = em.getCriteriaBuilder().createQuery(Account.class);
			CriteriaBuilder cb = em.getCriteriaBuilder();
			Root<Account> acc = criteria.from(Account.class);
			Expression<String> emailExpr = acc.get("email");
			Expression<String> passwdExpr = acc.get("password");
			criteria.select(acc).where(cb.and(cb.equal(emailExpr, email), cb.equal(passwdExpr, password)));
			return em.createQuery(criteria).getSingleResult();
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

	public List<CardRequest> requests(Date from, Date to) {
		EntityManager em = null;
		try {
			em = createEntityManager();
			CriteriaQuery<CardRequest> criteria = em.getCriteriaBuilder().createQuery(CardRequest.class);
			CriteriaBuilder cb = em.getCriteriaBuilder();
			Root<CardRequest> request = criteria.from(CardRequest.class);
			Expression<Long> date = request.get("dateCreated");
			Expression<Boolean> approved = request.get("approved");
			criteria.select(request).where(cb.and(cb.between(date, from.getTime(), to.getTime()), cb.not(approved)));
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

	private List<Transaction> history(User owner, TransactionType type, boolean from, boolean to, Date start, Date end) {
		EntityManager em = null;
		try {
			em = createEntityManager();
			CriteriaBuilder builder = em.getCriteriaBuilder();
			CriteriaQuery<Transaction> criteria = builder.createQuery(Transaction.class);
			Root<Transaction> transaction = criteria.from(Transaction.class);
			Path<CardBook> fromCB = transaction.get("from");
			Path<CardBook> toCB = transaction.get("to");
			Path<Date> date = transaction.get("date");
			Predicate fake = builder.equal(builder.literal(Boolean.TRUE), Boolean.TRUE);
			//god mode -> on
			Predicate where = builder.and(
					builder.equal(transaction.get("type"), type),
					builder.or(
					to ? builder.equal(toCB.get("owner"), owner) : fake,
					from ? builder.equal(fromCB.get("owner"), owner) : fake),
					start != null && end != null ? builder.between(date, start, end)
					: start == null && end != null ? builder.lessThanOrEqualTo(date, end)
					: start != null && end == null ? builder.greaterThanOrEqualTo(date, start) : fake);
			//god mode -> off
			criteria.multiselect(transaction, toCB, fromCB).where(where);
			return em.createQuery(criteria).getResultList();
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

	public List<Transaction> historyOutcome(User owner, TransactionType type, Date from, Date to) {
		return history(owner, type, true, false, from, to);
	}

	public List<Transaction> historyIncome(User owner, TransactionType type, Date from, Date to) {
		return history(owner, type, false, true, from, to);
	}

	public List<Transaction> historyAll(User owner, TransactionType type, Date from, Date to) {
		return history(owner, type, true, true, from, to);
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
