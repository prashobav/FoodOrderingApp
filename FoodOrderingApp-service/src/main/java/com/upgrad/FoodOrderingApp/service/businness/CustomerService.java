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
import com.upgrad.FoodOrderingApp.service.exception.UpdateCustomerException;

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

	         
	        CustomerAuthEntity CustomerAuthEntity = customerDao.getCustomerAuthToken(authorizationToken);

	        
	        final ZonedDateTime now = ZonedDateTime.now();

	        
	        validateAccessToken(authorizationToken);

	        
	        CustomerAuthEntity.setLogoutAt(now);

	        
	        customerDao.updateCustomerAuth(CustomerAuthEntity);
	        return CustomerAuthEntity;
	    }

	    @Transactional
	    public void validateAccessToken(final String authorizationToken) throws AuthorizationFailedException{

	        
	        CustomerAuthEntity CustomerAuthEntity = customerDao.getCustomerAuthToken(authorizationToken);

	        
	        final ZonedDateTime now = ZonedDateTime.now();

	         
	        if (CustomerAuthEntity == null) {
	            throw new AuthorizationFailedException("ATHR-001", "Customer is not Logged in.");
	         
	        } else if (CustomerAuthEntity.getLogoutAt() != null) {
	            throw new AuthorizationFailedException("ATHR-002", "Customer is logged out. Log in again to access this endpoint.");
	         
	        } else if (now.isAfter(CustomerAuthEntity.getExpiresAt()) ) {
	            throw new AuthorizationFailedException("ATHR-003", "Your session is expired. Log in again to access this endpoint.");
	        }	

	    }

	    @Transactional
	    public CustomerAuthEntity getCustomerAuthToken(final String accessToken) {
	        // Calls customerDao to get the access token of the customer from the database
	        return customerDao.getCustomerAuthToken(accessToken);
	    }
	    
	    @Transactional
	    public CustomerEntity updateCustomer (CustomerEntity updatedCustomerEntity, final String authorizationToken)
	            throws AuthorizationFailedException, UpdateCustomerException {

	      
	        CustomerAuthEntity customerAuthEntity = customerDao.getCustomerAuthToken(authorizationToken);

	       
	        validateAccessToken(authorizationToken);

	        
	        CustomerEntity customerEntity =  customerDao.getCustomerByUuid(customerAuthEntity.getUuid());

	         
	        if (updatedCustomerEntity.getFirstName() == null) {
	            throw new UpdateCustomerException("UCR-002", "First name field should not be empty");
	        }

	        
	        customerEntity.setFirstName(updatedCustomerEntity.getFirstName());
	        customerEntity.setLastName(updatedCustomerEntity.getLastName());

	         
	        customerDao.updateCustomer(customerEntity);
	        return customerEntity;
	    }
	    
	    @Transactional
	    public CustomerEntity updateCustomerPassword (final String oldPassword, final String newPassword, final String authorizationToken)
	            throws AuthorizationFailedException, UpdateCustomerException {

	        
	        final ZonedDateTime now = ZonedDateTime.now();

	         
	        CustomerAuthEntity customerAuthTokenEntity = customerDao.getCustomerAuthToken(authorizationToken);

	        
	        validateAccessToken(authorizationToken);

	        
	        CustomerEntity customerEntity =  customerDao.getCustomerByUuid(customerAuthTokenEntity.getUuid());

	       
	        if (oldPassword == null || newPassword ==  null) {
	            throw new UpdateCustomerException("UCR-003", "No field should be empty");
	        }

	         final String encryptedPassword = cryptographyProvider.encrypt(oldPassword, customerEntity.getSalt());

	         if(!encryptedPassword.equals(customerEntity.getPassword())) {
	            throw new UpdateCustomerException("UCR-004", "Incorrect old password!");
	         } else if (newPassword.length() < 8
	                || !newPassword.matches(".*[0-9]{1,}.*")
	                || !newPassword.matches(".*[A-Z]{1,}.*")
	                || !newPassword.matches(".*[#@$%&*!^]{1,}.*")) {
	            throw new UpdateCustomerException("UCR-001", "Weak password!");
	        }

	         customerEntity.setPassword(newPassword);
 
	        String[] encryptedText = cryptographyProvider.encrypt(customerEntity.getPassword());
	        customerEntity.setSalt(encryptedText[0]);
	        customerEntity.setPassword(encryptedText[1]);

	       
	        customerDao.updateCustomer(customerEntity);
	        return customerEntity;
	    }
}
 