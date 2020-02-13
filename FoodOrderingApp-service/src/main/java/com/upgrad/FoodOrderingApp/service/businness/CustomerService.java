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
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;

@Service
public class CustomerService {
   
	@Autowired
	private CustomerDao customerDao;
	
	@Autowired
	private PasswordCryptographyProvider cryptographyProvider;
	
	 @Transactional(propagation = Propagation.REQUIRED)
	 public CustomerEntity SignupCustomer(CustomerEntity customerEntity) {
		 
		 String[] encryptedText = cryptographyProvider.encrypt(customerEntity.getPassword());
		 customerEntity.setSalt(encryptedText[0]);
		 customerEntity.setPassword(encryptedText[1]);
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
			 String cusEntpwd = customerEntity.getPassword();
			 String cusEntNumer =  customerEntity.getContactNumber();
			 String custLN = customerEntity.getLastName();
			 JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(encryptedPassword);
			 CustomerAuthEntity customerAuthToken = new CustomerAuthEntity();
			 customerAuthToken.setCustomer(customerEntity);
			 final ZonedDateTime now = ZonedDateTime.now();
			 final ZonedDateTime expiresAt = now.plusHours(8);
			 String UUID1 = customerEntity.getUuid();
			 customerAuthToken.setAccessToken(jwtTokenProvider.generateToken(customerEntity.getUuid(), now, expiresAt));
			 customerAuthToken.setLoginAt(now);
			 customerAuthToken.setExpiresAt(expiresAt);
			customerAuthToken.setUuid(customerEntity.getUuid());
			
			 customerDao.createAuthToken(customerAuthToken);
			 return customerAuthToken; 
		 }
		 else {
			 throw new AuthenticationFailedException("ATH-002","Invalid Credentials") ;
		 }  
		 
	 }
	 
	 @Transactional
	    public CustomerAuthEntity logout (final String authorizationToken) throws AuthorizationFailedException {

	        // Gets the CustomerAuthEntity with the provided authorizationToken
	        CustomerAuthEntity CustomerAuthEntity = customerDao.getCustomerAuthToken(authorizationToken);

	        // Gets the current time
	        final ZonedDateTime now = ZonedDateTime.now();

	        // Validates the provided access token
	        validateAccessToken(authorizationToken);

	        // Sets the logout time to now
	        CustomerAuthEntity.setLogoutAt(now);

	        // Calls customerDao to update the customer logout details in the database
	        customerDao.updateCustomerAuth(CustomerAuthEntity);
	        return CustomerAuthEntity;
	    }

	    @Transactional
	    public void validateAccessToken(final String authorizationToken) throws AuthorizationFailedException{

	        //get the customerAuthToken details from customerDao
	        CustomerAuthEntity CustomerAuthEntity = customerDao.getCustomerAuthToken(authorizationToken);

	        // Gets the current time
	        final ZonedDateTime now = ZonedDateTime.now();

	        // Throw AuthorizationFailedException if the customer is not authorized
	        if (CustomerAuthEntity == null) {
	            throw new AuthorizationFailedException("ATHR-001", "Customer is not Logged in.");
	        // Throw AuthorizationFailedException if the customer is logged out
	        } else if (CustomerAuthEntity.getLogoutAt() != null) {
	            throw new AuthorizationFailedException("ATHR-002", "Customer is logged out. Log in again to access this endpoint.");
	        // Throw AuthorizationFailedException if the customer session is expired
	        } else if (now.isAfter(CustomerAuthEntity.getExpiresAt()) ) {
	            throw new AuthorizationFailedException("ATHR-003", "Your session is expired. Log in again to access this endpoint.");
	        }

	    }

	    @Transactional
	    public CustomerAuthEntity getCustomerAuthToken(final String accessToken) {
	        // Calls customerDao to get the access token of the customer from the database
	        return customerDao.getCustomerAuthToken(accessToken);
	    }
}
 