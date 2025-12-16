package com.picanha.chimichurri.entities;

import java.util.ArrayList;
import java.util.List;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public abstract class BaseServices<T> {
	
    private final Class<T> entityClass;

    public BaseServices(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public abstract T save(T obj);
    
	protected T save(T obj, PlatformTransactionManager txManager, EntityManager em) {
		TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());

        try {
            em.persist(obj);
            em.flush();

            // manually commit
            txManager.commit(status);
            return obj;

        } catch (Exception e) {
            // manually rollback
            txManager.rollback(status);
            throw new RuntimeException("Failed to save user", e);
        }
	}
	
	public abstract T getById(int id);
	
	protected T getById(int id, EntityManager em) {		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> query = cb.createQuery(entityClass);
		Root<T> root = query.from(entityClass);
		
		List<Predicate> predicates = new ArrayList<Predicate>();
		predicates.add(root.get("id").equalTo(id));
		
		query = query.select(root).distinct(true).where(predicates);
		TypedQuery<T> tq = em.createQuery(query);
		List<T> resultList = tq.getResultList();
		
		if(resultList.size()>1) {
			throw new RuntimeException(resultList.size() + " user has ID " + id);
		}else if(resultList.size()<1) {
			return null;
		}
		
		return resultList.get(0);
	}
	
	public abstract List<T> getAll();
	
	protected List<T> getAll(EntityManager em) {	
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> query = cb.createQuery(entityClass);
		Root<T> root = query.from(entityClass);
		
		query = query.select(root).distinct(true);
		TypedQuery<T> tq = em.createQuery(query);
		List<T> resultList = tq.getResultList();
		return resultList;
	}
	
	public abstract void deleteById(int id);
	
	protected void deleteById(int id, PlatformTransactionManager txManager, EntityManager em) {	
		T obj = getById(id, em);
		delete(obj, txManager, em);
	}

	public abstract void delete(T obj);
	
	protected void delete(T obj, PlatformTransactionManager txManager, EntityManager em) {
		TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());

        try {
            em.persist(obj);
            em.flush();

            // manually commit
            txManager.commit(status);

        } catch (Exception e) {
            // manually rollback
            txManager.rollback(status);
            throw new RuntimeException("Failed to save user", e);
        }
	}
}
