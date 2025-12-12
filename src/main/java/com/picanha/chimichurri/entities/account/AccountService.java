package com.picanha.chimichurri.entities.account;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.picanha.chimichurri.entities.user.User;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class AccountService {

	@Autowired
	private PlatformTransactionManager txManager;
	@Autowired
	private EntityManager em;

	public Account saveAccount(Account account) {
		TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());

        try {
            em.persist(account);
            em.flush();

            // manually commit
            txManager.commit(status);
            return account;

        } catch (Exception e) {
            // manually rollback
            txManager.rollback(status);
            throw new RuntimeException("Failed to save user", e);
        }
	}
	
	public Account getAccount(int id) {		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Account> query = cb.createQuery(Account.class);
		Root<Account> root = query.from(Account.class);
		
		List<Predicate> predicates = new ArrayList<Predicate>();
		predicates.add(root.get("id").equalTo(id));
		
		query = query.select(root).distinct(true).where(predicates);
		TypedQuery<Account> tq = em.createQuery(query);
		List<Account> resultList = tq.getResultList();
		
		if(resultList.size()>1) {
			throw new RuntimeException(resultList.size() + " user has ID " + id);
		}else if(resultList.size()<1) {
			return null;
		}
		
		return resultList.get(0);
	}
}
