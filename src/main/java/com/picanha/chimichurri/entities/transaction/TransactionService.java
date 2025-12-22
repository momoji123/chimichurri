package com.picanha.chimichurri.entities.transaction;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

import com.picanha.chimichurri.entities.BaseServices;
import com.picanha.chimichurri.entities.account.Account;
import com.picanha.chimichurri.entities.account.AccountService;
import com.picanha.chimichurri.entities.account.AccountType;
import com.picanha.chimichurri.entities.user.User;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class TransactionService extends BaseServices<Transaction> {
	public TransactionService() {
		super(Transaction.class);
	}

	@Autowired
	private PlatformTransactionManager txManager;
	@Autowired
	private EntityManager em;

	@Autowired
	private AccountService accountService;

	public Transaction save(Transaction obj) {
		return save(obj, txManager, em);
	}

	public Transaction getById(int id) {
		return getById(id, em);
	}

	public List<Transaction> getAll() {
		return getAll(em);
	}

	@Override
	public void deleteById(int id) {
		deleteById(id, txManager, em);
	}

	@Override
	public void delete(Transaction obj) {
		delete(obj, txManager, em);
	}

	public List<Transaction> getBy(User u, AccountType type, String asset, ZonedDateTime fromTransactionDate,
			ZonedDateTime toTransactionDate) {
		Account account = null;

		for (Account acc : accountService.getByUser(u)) {
			if (acc.getType().equals(type)) {
				account = acc;
				break;
			}
		}

		if (account == null) {
			return new ArrayList<Transaction>();
		}

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Transaction> query = cb.createQuery(Transaction.class);
		Root<Transaction> root = query.from(Transaction.class);

		List<Predicate> predicates = new ArrayList<Predicate>();
		predicates.add(root.get("account").get("id").equalTo(account.getId()));

		if (StringUtils.isNotBlank(asset)) {
			predicates.add(root.get("asset").equalTo(asset));
		}

		if (fromTransactionDate != null) {
			predicates.add(cb.greaterThanOrEqualTo(root.get("transactionDate"), fromTransactionDate));
		}

		if (toTransactionDate != null) {
			predicates.add(cb.lessThanOrEqualTo(root.get("transactionDate"), toTransactionDate));
		}

		query = query.select(root).distinct(true).where(predicates);
		TypedQuery<Transaction> tq = em.createQuery(query);
		List<Transaction> resultList = tq.getResultList();

		if (resultList.size() < 1) {
			return new ArrayList<Transaction>();
		}

		return resultList;
	}
}
