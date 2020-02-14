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
	
	 public void updateCustomerAuth(final CustomerAuthEntity customerAuthEntity) {
	        entityManager.merge(customerAuthEntity);
	    }
	 public CustomerAuthEntity getCustomerAuthToken(final String accessToken) {
	        try {
	            return entityManager.createNamedQuery("customerAuthByAccessToken", CustomerAuthEntity.class)
	                    .setParameter("accessToken", accessToken).getSingleResult();
	        } catch(NoResultException nre) {
	            return null;
	        }
	    }	
	 
	 public CustomerEntity getCustomerByUuid(final String uuid) {
	        try {
	            return entityManager.createNamedQuery("customerByUuid", CustomerEntity.class).setParameter("uuid", uuid)
	                    .getSingleResult();
	        } catch(NoResultException nre) {
	            return null;
	        }
	    }
	 
	 public void updateCustomer(final CustomerEntity updatedCustomerEntity) {
	        entityManager.merge(updatedCustomerEntity);
	    }
}
