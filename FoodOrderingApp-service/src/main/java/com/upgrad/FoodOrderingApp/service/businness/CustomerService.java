package com.upgrad.FoodOrderingApp.service.businness;

import java.time.ZonedDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;

@Service
public class CustomerService {
   
	@Autowired
	private CustomerDao customerDao;
	
	@Autowired
	private PasswordCryptographyProvider cryptographyProvider;
	
	 @Transactional(propagation = Propagation.REQUIRED)
	 public CustomerEntity SignupCustomer(CustomerEntity customerEntity) {
		return customerDao.createCustomer(customerEntity);
		}
	 
	 @Transactional(propagation = Propagation.REQUIRED)
	 public CustomerAuthEntity authenticate(final String username,final String password) throws AuthenticationFailedException {
		 CustomerEntity customerEntity = customerDao.getCustomerByContactNumber(username);
		 if (customerEntity == null) {
			throw new AuthenticationFailedException("ATH-001","This contact number has not been registered!") ; 
		 }
		 String encryptedPassword = cryptographyProvider.encrypt(password, customerEntity.getSalt());
		 if (encryptedPassword.equals(customerEntity.getPassword())) {
			 JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(encryptedPassword);
			 CustomerAuthEntity customerAuthToken = new CustomerAuthEntity();
			 customerAuthToken.setCustomer(customerEntity);
			 final ZonedDateTime now = ZonedDateTime.now();
			 final ZonedDateTime expiresAt = now.plusHours(8);
			 customerAuthToken.setAccessToken(jwtTokenProvider.generateToken(customerEntity.getUuid(), now, expiresAt));
			 customerAuthToken.setLoginAt(now);
			 customerAuthToken.setExpiresAt(expiresAt);
			 customerDao.createAuthToken(customerAuthToken);
			 return customerAuthToken; 
		 }
		 else {
			 throw new AuthenticationFailedException("ATH-002","Invalid Credentials") ;
		 } 
		
		  
		 
	 }
}
 