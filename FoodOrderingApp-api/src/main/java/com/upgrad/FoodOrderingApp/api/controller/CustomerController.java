package com.upgrad.FoodOrderingApp.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
 
import com.upgrad.FoodOrderingApp.api.model.SignupCustomerRequest;
import com.upgrad.FoodOrderingApp.api.model.SignupCustomerResponse;
 
@CrossOrigin	
@RestController
@RequestMapping("/")
public class CustomerController {
	  @RequestMapping(method = RequestMethod.POST,path="/customer/signup", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
      public ResponseEntity<SignupCustomerResponse> signup(final SignupCustomerRequest signupCustomerRequest) {
    	  return new ResponseEntity(HttpStatus.OK);
      }
      
}
