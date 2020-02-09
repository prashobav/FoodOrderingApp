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
import com.upgrad.FoodOrderingApp.api.model.SignupCustomerRequest;
import com.upgrad.FoodOrderingApp.api.model.SignupCustomerResponse;
import com.upgrad.FoodOrderingApp.service.businness.CustomerService;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
 
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
		  SignupCustomerResponse customerResponse = new SignupCustomerResponse().id(createdCustomerEntity.getUuid()).status("REGISTERED");
		  
		  return new ResponseEntity<SignupCustomerResponse>(customerResponse,HttpStatus.CREATED);
      }
      
	  
	  
	  @RequestMapping(method = RequestMethod.POST,path="/customer/login", produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
      public ResponseEntity<LoginResponse> login(@RequestHeader("Authorization") final String Authorization) throws AuthenticationFailedException {
		  byte[] decode = Base64.getDecoder().decode(Authorization.split("Basic ")[1]);
		  String decodedText = new String(decode);
		  String [] decodedArray = decodedText.split(":");
		  CustomerAuthEntity customerAuthToken = customerService.authenticate(decodedArray[0], decodedArray[1]);
		  CustomerEntity customer = customerAuthToken.getCustomer();
		  LoginResponse loginResponse = new LoginResponse().id(UUID.fromString(customer.getUuid())).firstName(customer.getFirstName()).lastName(customer.getLastName())
		  .email(customer.getEmail()).contactNumber(customer.getContactNumber());
		  HttpHeaders headers = new HttpHeaders();
		  headers.add("access-token", customerAuthToken.getAccessToken());
		  return new ResponseEntity<LoginResponse>(loginResponse,headers,HttpStatus.OK);
      }
	  
	  /*@RequestMapping(method = RequestMethod.POST,path="/customer/logout", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
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
		  SignupCustomerResponse customerResponse = new SignupCustomerResponse().id(createdCustomerEntity.getUuid()).status("REGISTERED");
		  
		  return new ResponseEntity<SignupCustomerResponse>(customerResponse,HttpStatus.CREATED);
      }
	  
	  @RequestMapping(method = RequestMethod.POST,path="/customer", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
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
		  SignupCustomerResponse customerResponse = new SignupCustomerResponse().id(createdCustomerEntity.getUuid()).status("REGISTERED");
		  
		  return new ResponseEntity<SignupCustomerResponse>(customerResponse,HttpStatus.CREATED);
      }
	  
	  @RequestMapping(method = RequestMethod.POST,path="/customer/password", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
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
		  SignupCustomerResponse customerResponse = new SignupCustomerResponse().id(createdCustomerEntity.getUuid()).status("REGISTERED");
		  
		  return new ResponseEntity<SignupCustomerResponse>(customerResponse,HttpStatus.CREATED);
      }*/
}
