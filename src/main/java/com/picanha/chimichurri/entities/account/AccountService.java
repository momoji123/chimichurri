package com.picanha.chimichurri.entities.account;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

import com.picanha.chimichurri.entities.BaseServices;
import com.picanha.chimichurri.entities.user.User;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class AccountService extends BaseServices<Account>{
	
	

	public AccountService() {
		super(Account.class);
	}

	@Autowired
	private PlatformTransactionManager txManager;
	@Autowired
	private EntityManager em;

	public Account save(Account account) {
		return save(account, txManager, em);
	}
	
	public Account getById(int id) {		
		return getById(id, em);
	}

	@Override
	public List<Account> getAll() {
		return getAll(em);
	}

	@Override
	public void deleteById(int id) {
		deleteById(id, txManager, em);
	}

	@Override
	public void delete(Account obj) {
		delete(obj, txManager, em);
	}
	
	public List<Account> getByUser(User u){
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Account> query = cb.createQuery(Account.class);
		Root<Account> root = query.from(Account.class);
		
		List<Predicate> predicates = new ArrayList<Predicate>();
		predicates.add(root.get("user").get("id").equalTo(u.getId()));
		
		query = query.select(root).distinct(true).where(predicates);
		TypedQuery<Account> tq = em.createQuery(query);
		List<Account> resultList = tq.getResultList();
		
		return resultList;
	}
}
