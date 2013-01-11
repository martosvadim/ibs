package edu.ibs.core.controller;

import edu.ibs.common.dto.TransactionType;
import edu.ibs.common.enums.CardBookType;
import edu.ibs.core.controller.exception.FreezedException;
import edu.ibs.core.controller.exception.NotEnoughMoneyException;
import edu.ibs.core.entity.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;

/**
 *
 * @date Nov 3, 2012
 *
 * @author Vadim Martos
 */
public final class SpecifiedJpaController extends CSUIDJpaController implements Serializable {

	public static final long[] CONTRAGENT_CARD_BOOK_IDS = {2L, 3L, 4L};
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
			if (from == null || to == null) {
				throw new IllegalArgumentException("Card book doesn't exist");
			} else if (to.getType().equals(CardBookType.CREDIT)) {
				em.getTransaction().rollback();
				throw new IllegalArgumentException("Can't transfer money to credit card book");
			} else if (from.isFreezed()) {
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
					throw new IllegalArgumentException(String.format("Bad card book %s: has no payment background", from));
				} else if (toEntity == null) {
					em.getTransaction().rollback();
					throw new IllegalArgumentException(String.format("Bad card book %s: has no payment background", to));
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
		} catch (RuntimeException e) {
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
				throw new IllegalArgumentException("Transaction doesn't exist");
			}
			Transaction rollback = pay(tr.getTo(), tr.getFrom(), tr.getMoney(), TransactionType.PAYMENT);
			em.getTransaction().commit();
			return rollback;
		} catch (RuntimeException e) {
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
			Iterator<Account> it = em.createQuery(criteria).setMaxResults(1).getResultList().iterator();
			return it.hasNext() ? it.next() : null;
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

	private Account getAccByEmail(String email) {
		EntityManager em = null;
		try {
			em = createEntityManager();
			CriteriaQuery<Account> criteria = em.getCriteriaBuilder().createQuery(Account.class);
			CriteriaBuilder cb = em.getCriteriaBuilder();
			Root<Account> acc = criteria.from(Account.class);
			Expression<String> emailExpr = acc.get("email");
			criteria.select(acc).where(cb.equal(emailExpr, email));
			Iterator<Account> it = em.createQuery(criteria).setMaxResults(1).getResultList().iterator();
			return it.hasNext() ? it.next() : null;
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

	public User getUserByEmail(String accountEmail) {
		Account acc = getAccByEmail(accountEmail);
		return acc == null ? null : acc.getUser();
	}

	public boolean userExists(String accountEmail) {
		return getUserByEmail(accountEmail) != null;
	}

	public boolean accountExists(String email) {
		return getAccByEmail(email) != null;
	}

	public List<CardRequest> getCardRequests(Date from, Date to) {
		EntityManager em = null;
		try {
			em = createEntityManager();
			CriteriaQuery<CardRequest> criteria = em.getCriteriaBuilder().createQuery(CardRequest.class);
			CriteriaBuilder cb = em.getCriteriaBuilder();
			Root<CardRequest> request = criteria.from(CardRequest.class);
			Expression<Long> date = request.get("dateCreated");
			Expression<Boolean> watched = request.get("watched");
			Predicate where;
			if (from == null && to == null) {
				where = cb.not(watched);
			} else {
				where = cb.and(cb.between(date, from.getTime(), to.getTime()), cb.not(watched));
			}
			criteria.select(request).where(where);
			return em.createQuery(criteria).getResultList();
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

	public List<SavedPayment> getSavedPayments(User user) {
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

	private List<Transaction> getTransactionHistory(User owner, TransactionType type, boolean from, boolean to, Date start, Date end) {
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

	public List<Transaction> getTrOutHistory(User owner, TransactionType type, Date from, Date to) {
		return getTransactionHistory(owner, type, true, false, from, to);
	}

	public List<Transaction> getTrInHistory(User owner, TransactionType type, Date from, Date to) {
		return getTransactionHistory(owner, type, false, true, from, to);
	}

	public List<Transaction> getTrAllHistory(User owner, TransactionType type, Date from, Date to) {
		return getTransactionHistory(owner, type, true, true, from, to);
	}

	public List<BankBook> getBankBooksOf(User owner) {
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

	public List<CardBook> getCardBooksOf(User user, BankBook bankBook, CardBookType type) {
		EntityManager em = null;
		try {
			em = createEntityManager();
			CriteriaBuilder builder = em.getCriteriaBuilder();
			CriteriaQuery<CardBook> criteria = builder.createQuery(CardBook.class);
			Root<CardBook> cardBook = criteria.from(CardBook.class);
			Path<User> u = cardBook.get("owner");
			Path<BankBook> bb = cardBook.get("bankBook");
			Path<CardBookType> t = cardBook.get("type");
			Predicate fake = builder.equal(builder.literal(Boolean.TRUE), Boolean.TRUE);
			//god mode -> on
			Predicate where = builder.and(
					type != null ? builder.equal(t, type) : fake,
					bankBook != null ? builder.equal(bb, bankBook) : fake,
					user != null ? builder.equal(u, user) : fake);
			//god mode -> off
			criteria.select(cardBook).where(where);
			return em.createQuery(criteria).getResultList();
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

	public List<CardBook> getCardBooksOf(User user) {
		return getCardBooksOf(user, null, null);
	}

	public List<CardBook> getCardBooksOf(User user, CardBookType type) {
		return getCardBooksOf(user, null, type);
	}

	public List<CardBook> getCardBooksOf(BankBook bankBook) {
		return getCardBooksOf(null, bankBook, null);
	}

	public List<CardBook> getCardBooksOf(BankBook bankBook, CardBookType type) {
		return getCardBooksOf(null, bankBook, type);
	}

	public List<CardBook> getCardBooksOf(User user, BankBook bankBook) {
		return getCardBooksOf(user, bankBook, null);
	}

	public Currency getCurrency(String name) {
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

	public void addMoney(BankBook bankBook, Money money) throws IllegalArgumentException, FreezedException {
		EntityManager em = null;
		try {
			em = createEntityManager();
			em.getTransaction().begin();
			bankBook = em.find(bankBook.getClass(), bankBook.getId(), LockModeType.PESSIMISTIC_WRITE);
			if (bankBook == null) {
				em.getTransaction().rollback();
				throw new IllegalArgumentException("BankBook with does not exists");
			} else if (em.find(bankBook.getCurrency().getClass(), bankBook.getCurrency().getId()) == null) {
				em.getTransaction().rollback();
				throw new IllegalArgumentException("Currency with does not exists");
			} else if (bankBook.isFreezed()) {
				em.getTransaction().rollback();
				throw new FreezedException(String.format("BankBook %s is freezed", bankBook));
			} else {
				bankBook.add(money);
				em.getTransaction().commit();
			}
		} catch (RuntimeException e) {
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

	public CreditPlan getCreditPlan(String name) {
		EntityManager em = null;
		try {
			em = createEntityManager();
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<CreditPlan> criteria = cb.createQuery(CreditPlan.class);
			Root<CreditPlan> plan = criteria.from(CreditPlan.class);
			Expression<String> nameExpr = plan.get("name");
			Expression<Boolean> freezedExpr = plan.get("freezed");
			criteria.select(plan).where(cb.and(cb.equal(nameExpr, name), cb.not(freezedExpr)));
			Iterator<CreditPlan> it = em.createQuery(criteria).setMaxResults(1).getResultList().iterator();
			return it.hasNext() ? it.next() : null;
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

	public List<CreditPlan> getCreditPlansFor(Currency curr) {
		EntityManager em = null;
		try {
			em = createEntityManager();
			curr = em.find(curr.getClass(), curr.getId());
			if (curr == null) {
				throw new IllegalArgumentException("Currency does not exist");
			}
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<CreditPlan> criteria = cb.createQuery(CreditPlan.class);
			Root<CreditPlan> plan = criteria.from(CreditPlan.class);
			Expression<Boolean> freezedExpr = plan.get("freezed");
			Expression<Currency> currExpr = plan.get("currency");
			criteria.select(plan).where(cb.and(cb.not(freezedExpr)), cb.equal(currExpr, curr));
			return em.createQuery(criteria).getResultList();
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

	public List<CreditPlan> getActualCreditPlans() {
		EntityManager em = null;
		try {
			em = createEntityManager();
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<CreditPlan> criteria = cb.createQuery(CreditPlan.class);
			Root<CreditPlan> plan = criteria.from(CreditPlan.class);
			Expression<Boolean> freezedExpr = plan.get("freezed");
			criteria.select(plan).where(cb.not(freezedExpr));
			return em.createQuery(criteria).getResultList();
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

	public List<CardRequest> getCardRequestsOf(User user, boolean watched) {
		EntityManager em = null;
		try {
			em = createEntityManager();
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<CardRequest> criteria = cb.createQuery(CardRequest.class);
			Root<CardRequest> request = criteria.from(CardRequest.class);
			Expression<Boolean> watchedExpr = request.get("watched");
			Expression<User> userExpr = request.get("user");
			Predicate where = cb.and(cb.equal(watchedExpr, watched), cb.equal(userExpr, user));
			criteria.select(request).where(where);
			return em.createQuery(criteria).getResultList();
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

	public CardRequest requestCard(User user, BankBook bankBook, CreditPlan plan) throws IllegalArgumentException {
		EntityManager em = null;
		try {
			em = createEntityManager();
			Currency curr = bankBook.getCurrency();
			em.getTransaction().begin();
			bankBook = em.find(bankBook.getClass(), bankBook.getId());
			user = em.find(user.getClass(), user.getId());
			if (bankBook == null) {
				bankBook = new BankBook(user, new Money(0L, curr));
				em.persist(bankBook);
				em.flush();
			} else if (user == null) {
				em.getTransaction().rollback();
				throw new IllegalArgumentException("User was not found");
			} else if (!bankBook.getOwner().equals(user)) {
				em.getTransaction().rollback();
				throw new IllegalArgumentException(String.format("User %s doesn't own bank book %s", user, bankBook));
			}
			CardRequest request;
			if (plan == null) {
				request = new CardRequest(user, bankBook);
			} else {
				request = new CardRequest(user, bankBook, plan);
			}
			em.persist(request);
			em.getTransaction().commit();
			return request;
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

	public CardBook processCardRequest(CardRequest request, boolean approved, String reason) {
		EntityManager em = null;
		try {
			em = createEntityManager();
			em.getTransaction().begin();
			request = em.find(request.getClass(), request.getId());
			CardBook cardBook = null;
			if (approved) {
				switch (request.getType()) {
					case CREDIT: {
						Credit credit = new Credit(request.getPlan());
						cardBook = new CardBook(request.getBankBook(), credit);
						break;
					}
					case DEBIT: {
						cardBook = new CardBook(request.getBankBook());
						break;
					}
					default: {
						em.getTransaction().rollback();
						throw new IllegalArgumentException(String.format("Unknown card book type %s", request.getType()));
					}
				}
				request.approve(cardBook);
				em.persist(cardBook);
			} else {
				request.decline(reason);
			}
			em.merge(request);
			em.getTransaction().commit();
			return cardBook;
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

	public CardBook createCardBook(User user, BankBook book, CreditPlan plan) {
		EntityManager em = null;
		try {
			em = createEntityManager();
			em.getTransaction().begin();
			user = em.find(user.getClass(), user.getId());
			if (user == null) {
				throw new IllegalArgumentException("User doesn't exist");
			}
			book = em.find(book.getClass(), book.getId());
			if (book == null) {
				throw new IllegalArgumentException("Book doesn't exist");
			}
			if (em.find(book.getCurrency().getClass(), book.getCurrency().getId()) == null) {
				throw new IllegalArgumentException("Currency doesn't exist");
			}
			CardBook cardBook;
			if (plan != null) {
				plan = em.find(plan.getClass(), plan.getId());
				if (plan == null) {
					throw new IllegalArgumentException("Credit plan doesn't exist");
				}
				if (!book.getCurrency().equals(plan.getCurrency())) {
					throw new IllegalArgumentException(String.format("Currency of bank book [%s] doesn't equals to currency of credit plan [%s]", book.getCurrency().getName(), plan.getCurrency().getName()));
				}
				Credit credit = new Credit(plan);
				em.persist(credit);
				em.flush();
				cardBook = new CardBook(book, credit);
			} else {
				cardBook = new CardBook(book);
			}
			cardBook.setOwner(user);
			em.persist(cardBook);
			em.getTransaction().commit();
			return cardBook;
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

	public void payForCredits() {
		EntityManager em = null;
		try {
			em = createEntityManager();
			em.getTransaction().begin();
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Credit> query = cb.createQuery(Credit.class);
			Root<Credit> credit = query.from(Credit.class);
			Expression<Long> lastPayed = credit.get("lastPayDate");
			//todo pay for credit here
			em.getTransaction().commit();
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

	public List<CardBook> getContragentList() {
		EntityManager em = null;
		try {
			em = createEntityManager();
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<CardBook> query = cb.createQuery(CardBook.class);
			Root<CardBook> book = query.from(CardBook.class);
			List<Predicate> predicates = new ArrayList<Predicate>(CONTRAGENT_CARD_BOOK_IDS.length);
			for (long id : CONTRAGENT_CARD_BOOK_IDS) {
				Predicate p = cb.equal(book.get("id"), id);
				predicates.add(p);
			}
			Predicate[] array = new Predicate[predicates.size()];
			query.select(book).where(cb.or(predicates.toArray(array)));
			return em.createQuery(query).getResultList();
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

	public List<Autopay> getAutopaysOf(User user) {
		//todo return autopays of user here
		return null;
	}

	public List<Autopay> getAutopaysToPay() {
		EntityManager em = null;
		try {
			em = createEntityManager();
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Autopay> query = cb.createQuery(Autopay.class);
			Root<Autopay> autopay = query.from(Autopay.class);
			Expression<Long> lastPayed = autopay.get("lastPayed");
			Expression<Long> period = autopay.get("period");
			query.select(autopay).where(cb.ge(cb.sum(lastPayed, period), System.currentTimeMillis()));
			return em.createQuery(query).getResultList();
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

	public List<Transaction> autopay() {
		List<Autopay> autopays = getAutopaysToPay();
		List<Transaction> transactions = new ArrayList<Transaction>(autopays.size());
		for (Autopay pay : autopays) {
			try {
				Transaction tr = pay(pay.getFrom(), pay.getTo(), pay.getMoney(), TransactionType.PAYMENT);
				pay.setLastPayed(System.currentTimeMillis());
				this.update(pay);
				transactions.add(tr);
			} catch (IllegalArgumentException ex) {
				Logger.getLogger(SpecifiedJpaController.class.getName()).log(Level.SEVERE, null, ex);
			} catch (FreezedException ex) {
				Logger.getLogger(SpecifiedJpaController.class.getName()).log(Level.SEVERE, null, ex);
			} catch (NotEnoughMoneyException ex) {
				Logger.getLogger(SpecifiedJpaController.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		return transactions;
	}
}
