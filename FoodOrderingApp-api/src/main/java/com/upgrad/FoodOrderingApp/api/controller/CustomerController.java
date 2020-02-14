package com.upgrad.FoodOrderingApp.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.upgrad.FoodOrderingApp.api.model.LoginResponse;
import com.upgrad.FoodOrderingApp.api.model.LogoutResponse;
import com.upgrad.FoodOrderingApp.api.model.SignupCustomerRequest;
import com.upgrad.FoodOrderingApp.api.model.SignupCustomerResponse;
import com.upgrad.FoodOrderingApp.api.model.UpdateCustomerRequest;
import com.upgrad.FoodOrderingApp.api.model.UpdateCustomerResponse;
import com.upgrad.FoodOrderingApp.api.model.UpdatePasswordRequest;
import com.upgrad.FoodOrderingApp.api.model.UpdatePasswordResponse;
import com.upgrad.FoodOrderingApp.service.businness.CustomerService;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.UpdateCustomerException;
 
@CrossOrigin	
@RestController
@RequestMapping("/")
public class CustomerController {
	
	@Autowired
	private CustomerService customerService;
	
	  @RequestMapping(method = RequestMethod.POST,path="/customer/signup", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
      public ResponseEntity<SignupCustomerResponse> signup(final SignupCustomerRequest signupCustomerRequest) {
		  
		  final CustomerEntity customerEntity = new CustomerEntity();
		  customerEntity.setUuid(UUID.randomUUID().toString());
		  customerEntity.setFirstName(signupCustomerRequest.getFirstName());
		  customerEntity.setLastName(signupCustomerRequest.getLastName());
		  customerEntity.setEmail(signupCustomerRequest.getEmailAddress());
		  customerEntity.setPassword(signupCustomerRequest.getPassword());
		  customerEntity.setContactNumber(signupCustomerRequest.getContactNumber());
		  customerEntity.setSalt("1234abc");
		  
		  
		  final CustomerEntity createdCustomerEntity =  customerService.SignupCustomer(customerEntity);
		  SignupCustomerResponse customerResponse = new SignupCustomerResponse().id(createdCustomerEntity.getUuid()).status("CUSTOMER SUCCESSFULLY REGISTERED");
		  
		  return new ResponseEntity<SignupCustomerResponse>(customerResponse,HttpStatus.CREATED);
      }
      
	  
	  
	 @RequestMapping(method = RequestMethod.POST,path="/customer/login", produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
      public ResponseEntity<LoginResponse> login(@RequestHeader("Authorization") final String Authorization) throws AuthenticationFailedException {
		 if (Authorization == null || !Authorization.startsWith("Basic ")) {
	            throw new AuthenticationFailedException("ATH-003", "Incorrect format of decoded customer name and password");
	        } 
		 
		 byte[] decode = Base64.getDecoder().decode(Authorization.split("Basic ")[1]);
		  String decodedText = new String(decode);
		  
		  if (!decodedText.matches("([0-9]+):(.+?)")) {
	            throw new AuthenticationFailedException("ATH-003", "Incorrect format of decoded customer name and password");
	        }
		  String [] decodedArray = decodedText.split(":");
		  CustomerAuthEntity customerAuthToken = customerService.authenticate(decodedArray[0], decodedArray[1]);
		  CustomerEntity customer = customerAuthToken.getCustomer();
		  LoginResponse loginResponse = new LoginResponse().id(customer.getUuid()).
		   firstName(customer.getFirstName()).
		   lastName(customer.getLastName()).
		   emailAddress(customer.getEmail()).
		   contactNumber(customer.getContactNumber()).message("LOGGED IN SUCCESSFULLY");
		 
		  HttpHeaders headers = new HttpHeaders();
		  headers.add("access-token", customerAuthToken.getAccessToken());
		  return new ResponseEntity<LoginResponse>(loginResponse,headers,HttpStatus.OK);
      }
	  
	 @RequestMapping(method= RequestMethod.POST, path="/customer/logout", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	    public ResponseEntity<LogoutResponse>logout(@RequestHeader("authorization") final String authorization)
	            throws AuthorizationFailedException { 
	        String[] basicToken = authorization.split("Basic "); 
	        final CustomerAuthEntity customerAuthEntity = customerService.logout(basicToken[1]); 
	        CustomerEntity customerEntity = customerAuthEntity.getCustomer(); 
	        LogoutResponse logoutResponse = new LogoutResponse().id(customerEntity.getUuid()).message("LOGGED OUT SUCCESSFULLY"); 
	        return new ResponseEntity<LogoutResponse>(logoutResponse, HttpStatus.OK);

	    }
	  
	    @RequestMapping(method = RequestMethod.PUT, path = "/customer",consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	    public ResponseEntity<UpdateCustomerResponse> updateCustomer(final UpdateCustomerRequest customerUpdateRequest,
	                                                               @RequestHeader("authorization") final String authorizaton)
	            throws AuthorizationFailedException, UpdateCustomerException {

	      
	        String[] bearerToken = authorizaton.split("Bearer ");

	        
	        final CustomerEntity updatedCustomerEntity = new CustomerEntity();

	       
	        updatedCustomerEntity.setFirstName(customerUpdateRequest.getFirstName());
	        updatedCustomerEntity.setLastName(customerUpdateRequest.getLastName());

	    
	        CustomerEntity customerEntity = customerService.updateCustomer(updatedCustomerEntity, bearerToken[1]);

	        
	        UpdateCustomerResponse updateCustomerResponse = new UpdateCustomerResponse().id(customerEntity.getUuid())
	                .firstName(customerEntity.getFirstName()).lastName(customerEntity.getLastName())
	                .status("CUSTOMER DETAILS UPDATED SUCCESSFULLY");

	      
	        return new ResponseEntity<UpdateCustomerResponse>(updateCustomerResponse, HttpStatus.OK);

	    }
	  
	    @RequestMapping(method = RequestMethod.PUT, path = "/customer/password",consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	    public ResponseEntity<UpdatePasswordResponse> updateCustomerPassword(final UpdatePasswordRequest customerUpdatePasswordRequest,
	                                                                 @RequestHeader("authorization") final String authorizaton)
	            throws AuthorizationFailedException, UpdateCustomerException {

	        
	        String[] bearerToken = authorizaton.split("Bearer ");

	         
	        String oldPassword = customerUpdatePasswordRequest.getOldPassword();
	        String newPassword = customerUpdatePasswordRequest.getNewPassword();

	        
	        CustomerEntity customerEntity = customerService.updateCustomerPassword(oldPassword, newPassword, bearerToken[1]);

	   
	        UpdatePasswordResponse updatePasswordResponse = new UpdatePasswordResponse().id(customerEntity.getUuid())
	                .status("CUSTOMER PASSWORD UPDATED SUCCESSFULLY");

	 
	        return new ResponseEntity<UpdatePasswordResponse>(updatePasswordResponse, HttpStatus.OK);

	    }
}
