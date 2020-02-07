package com.upgrad.FoodOrderingApp.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.upgrad.FoodOrderingApp.api.model.SignupCustomerResponse;
@CrossOrigin
@RestController
@RequestMapping("/customer/signup")
public class CustomerController {
      public ResponseEntity<SignupCustomerResponse> signup(final SignupCustomerRequest signupCustomerRequest) {
    	  return new ResponseEntity(HttpStatus.OK);
      }
      
}
