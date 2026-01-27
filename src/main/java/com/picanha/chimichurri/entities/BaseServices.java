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
import jakarta.servlet.http.HttpServletRequest;

public abstract class BaseServices<T> {

	private final String DB_TX_STAT = "DB_TX_STAT";

	protected PlatformTransactionManager txManager;
	protected EntityManager em;

	private final Class<T> entityClass;

	public BaseServices(Class<T> entityClass, PlatformTransactionManager txManager, EntityManager em) {
		this.entityClass = entityClass;
		this.txManager = txManager;
		this.em = em;
	}

	public T save(T obj, HttpServletRequest request) {
		startTransaction(request);

		try {
			em.persist(obj);
			em.flush();
			return obj;

		} catch (Exception e) {
			rollback(request);
			throw new RuntimeException("Failed to save", e);
		}
	}

	public void rollback(HttpServletRequest request) {
		TransactionStatus status;
		// manually rollback
		status = (TransactionStatus) request.getAttribute(DB_TX_STAT);
		if (status == null) {
			return;
		}
		txManager.rollback(status);
		request.removeAttribute(DB_TX_STAT);
	}

	public void commit(HttpServletRequest request) {
		// manually commit
		TransactionStatus status = (TransactionStatus) request.getAttribute(DB_TX_STAT);
		if (status == null) {
			return;
		}
		txManager.commit(status);
		request.removeAttribute(DB_TX_STAT);
	}

	private void startTransaction(HttpServletRequest request) {
		TransactionStatus status = (TransactionStatus) request.getAttribute(DB_TX_STAT);

		if (status == null) {
			status = txManager.getTransaction(new DefaultTransactionDefinition());
			request.setAttribute(DB_TX_STAT, status);
		}
	}

	public T getById(int id) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> query = cb.createQuery(entityClass);
		Root<T> root = query.from(entityClass);

		List<Predicate> predicates = new ArrayList<Predicate>();
		predicates.add(root.get("id").equalTo(id));

		query = query.select(root).distinct(true).where(predicates);
		TypedQuery<T> tq = em.createQuery(query);
		List<T> resultList = tq.getResultList();

		if (resultList.size() > 1) {
			throw new RuntimeException(resultList.size() + " has ID " + id);
		} else if (resultList.size() < 1) {
			return null;
		}

		return resultList.get(0);
	}

	public List<T> getAll() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> query = cb.createQuery(entityClass);
		Root<T> root = query.from(entityClass);

		query = query.select(root).distinct(true);
		TypedQuery<T> tq = em.createQuery(query);
		List<T> resultList = tq.getResultList();
		return resultList;
	}

	public void deleteById(int id, HttpServletRequest req) {
		T obj = getById(id);
		delete(obj, req);
	}

	public void delete(T obj, HttpServletRequest req) {
		startTransaction(req);

		try {
			em.remove(obj);
		} catch (Exception e) {
			rollback(req);
			throw new RuntimeException("Failed to delete", e);
		}
	}
}
