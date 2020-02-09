package com.upgrad.FoodOrderingApp.service.dao;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;

@Repository
public class CustomerDao {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	public CustomerEntity createCustomer (CustomerEntity customerEntity) {
		  entityManager.persist(customerEntity);
		   return customerEntity;
	}
	
	public CustomerEntity getCustomerByContactNumber(final String contactNumber) {
		try {
			return entityManager.createNamedQuery("customerByContactNumber",CustomerEntity.class).setParameter("contactNumber", contactNumber).getSingleResult();
		} catch (NoResultException  nre) {
			 return null;
		}
		
	}
	
	public CustomerAuthEntity createAuthToken(final CustomerAuthEntity customerAuthEntity) {
		entityManager.persist(customerAuthEntity);
		return customerAuthEntity;
	}

}
