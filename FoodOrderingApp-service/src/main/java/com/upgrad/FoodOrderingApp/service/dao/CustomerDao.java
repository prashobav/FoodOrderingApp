package com.upgrad.FoodOrderingApp.service.dao;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;

@Repository
public class CustomerDao {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	public CustomerEntity createCustomer (CustomerEntity customerEntity) {
		  entityManager.persist(customerEntity);
		   return customerEntity;
		 
		
	}

}
