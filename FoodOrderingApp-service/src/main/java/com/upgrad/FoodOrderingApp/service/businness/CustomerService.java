package com.upgrad.FoodOrderingApp.service.businness;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;

@Service
public class CustomerService {
   
	@Autowired
	private CustomerDao customerDao;
	 @Transactional(propagation = Propagation.REQUIRED)
	 public CustomerEntity SignupCustomer(CustomerEntity customerEntity) {
		return customerDao.createCustomer(customerEntity);
		}
}
