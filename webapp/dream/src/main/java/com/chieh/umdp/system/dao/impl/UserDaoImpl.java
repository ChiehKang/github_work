package com.chieh.umdp.system.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.chieh.umdp.generic.entity.CurUser;
import com.chieh.umdp.system.dao.UserDao;

@Repository
public class UserDaoImpl implements UserDao {

//	@PersistenceContext
//	private EntityManager entityManager;

	EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("persistenceUnit");
	EntityManager entityManager = entityManagerFactory.createEntityManager();
	
	@Override
	public CurUser GetSingleAccount(String account, String password) throws Exception {
		// TODO Auto-generated method stub
		CurUser result = null;
		
		try {
			CriteriaBuilder builder = entityManager.getCriteriaBuilder();
			CriteriaQuery<CurUser> criteria = builder.createQuery(CurUser.class);
			Root<CurUser> root = criteria.from(CurUser.class);
			criteria.select(root);

			// 搜尋
			Predicate accountEqual = builder.equal(root.get("account"), account);
			Predicate pccountEqual = builder.equal(root.get("password"), password);
			
			criteria.where(accountEqual, pccountEqual);
			
			result = entityManager.createQuery(criteria).getSingleResult();
		} catch(javax.persistence.NoResultException e) {
			System.out.println("javax.persistence.NoResultException: No entity found for query.");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@Override
	public CurUser getUserInfo(String account) throws Exception {
		// TODO Auto-generated method stub
		CurUser result = null;
		try {
			CriteriaBuilder builder = entityManager.getCriteriaBuilder();
			CriteriaQuery<CurUser> criteria = builder.createQuery(CurUser.class);
			Root<CurUser> root = criteria.from(CurUser.class);
			criteria.select(root);

			// 搜尋
			Predicate UserAccountEqual = builder.equal(root.get("account"), account);
			criteria.where(UserAccountEqual);
			result = entityManager.createQuery(criteria).getSingleResult();
		} catch(javax.persistence.NoResultException e) {
			System.out.println("javax.persistence.NoResultException: No entity found for query.");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public List<Long> getAllUserId() {
		System.out.println("userDao.getUserInfo");
		String query = "Select user.userId from CurUser as user";
		Query criteriaQuery = entityManager.createQuery(query);
		return criteriaQuery.getResultList();
		
	}
}
