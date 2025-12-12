package com.picanha.chimichurri.entities.user;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class UserService {
	
	@Autowired
	private PlatformTransactionManager txManager;
	@Autowired
	private EntityManager em;

	public User saveUser(User user) {
		TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());

        try {
            em.persist(user);
            em.flush();

            // manually commit
            txManager.commit(status);
            return user;

        } catch (Exception e) {
            // manually rollback
            txManager.rollback(status);
            throw new RuntimeException("Failed to save user", e);
        }
	}
	
	public User getUser(int id) {		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<User> query = cb.createQuery(User.class);
		Root<User> root = query.from(User.class);
		
		List<Predicate> predicates = new ArrayList<Predicate>();
		predicates.add(root.get("id").equalTo(id));
		
		query = query.select(root).distinct(true).where(predicates);
		TypedQuery<User> tq = em.createQuery(query);
		List<User> uList = tq.getResultList();
		
		if(uList.size()>1) {
			throw new RuntimeException(uList.size() + " user has ID " + id);
		}else if(uList.size()<1) {
			return null;
		}
		
		return uList.get(0);
	}
}
