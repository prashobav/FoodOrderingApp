package com.upgrad.FoodOrderingApp.api.controller;
 
import com.upgrad.FoodOrderingApp.api.model.CategoryList;
import com.upgrad.FoodOrderingApp.api.model.ItemList;
import com.upgrad.FoodOrderingApp.api.model.RestaurantDetailsResponse;
import com.upgrad.FoodOrderingApp.api.model.RestaurantDetailsResponseAddress;
import com.upgrad.FoodOrderingApp.api.model.RestaurantDetailsResponseAddressState;
import com.upgrad.FoodOrderingApp.api.model.RestaurantList;
import com.upgrad.FoodOrderingApp.api.model.RestaurantListResponse;
import com.upgrad.FoodOrderingApp.api.model.RestaurantUpdatedResponse;
import com.upgrad.FoodOrderingApp.service.businness.AddressService;
import com.upgrad.FoodOrderingApp.service.businness.CategoryBusinessService;
import com.upgrad.FoodOrderingApp.service.businness.CustomerService;
import com.upgrad.FoodOrderingApp.service.businness.RestaurantBusinessService;
import com.upgrad.FoodOrderingApp.service.businness.StateBusinessService;
import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantCategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.InvalidRatingException;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
 
@ComponentScan
@RestController
@CrossOrigin
@RequestMapping("/") 
public class RestaurantController {


    @Autowired
    private RestaurantBusinessService restaurantBusinessService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private StateBusinessService stateBusinessService;

    @Autowired
    private CategoryBusinessService categoryBusinessService;

    @Autowired
    private CustomerService customerService;
 
   
    @RequestMapping(method = RequestMethod.GET, path = "/restaurant", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantListResponse> getAllRestaurants() {

        
        final List<RestaurantEntity> allRestaurants = restaurantBusinessService.getAllRestaurants();

        RestaurantListResponse restaurantListResponse = new RestaurantListResponse();

        
        List<RestaurantList> details = new ArrayList<RestaurantList>();
        for (RestaurantEntity n: allRestaurants) {
            RestaurantList detail = new RestaurantList();
            detail.setId(UUID.fromString(n.getUuid()));
            detail.setRestaurantName(n.getRestaurantName());
            detail.setPhotoURL(n.getPhotoUrl());
            detail.setCustomerRating(n.getCustomerRating());
            detail.setAveragePrice(n.getAvgPriceForTwo());
            detail.setNumberCustomersRated(n.getNumCustomersRated());

             
            AddressEntity addressEntity = addressService.getAddressById(n.getAddress().getId());
            RestaurantDetailsResponseAddress responseAddress = new RestaurantDetailsResponseAddress();

            responseAddress.setId(UUID.fromString(addressEntity.getUuid()));
            responseAddress.setFlatBuildingName(addressEntity.getFlatBuildingNumber());
            responseAddress.setLocality(addressEntity.getLocality());
            responseAddress.setCity(addressEntity.getCity());
            responseAddress.setPincode(addressEntity.getPincode());

          
            StateEntity stateEntity = stateBusinessService.getStateById(addressEntity.getState().getId());
            RestaurantDetailsResponseAddressState responseAddressState = new RestaurantDetailsResponseAddressState();

            responseAddressState.setId(UUID.fromString(stateEntity.getUuid()));
            responseAddressState.setStateName(stateEntity.getStateName());
            responseAddress.setState(responseAddressState);

           
            detail.setAddress(responseAddress);

             
            List<String> categoryLists = new ArrayList();
            for (CategoryEntity categoryEntity :n.getCategoryEntities()) {
                categoryLists.add(categoryEntity.getCategoryName());
            }

            
            Collections.sort(categoryLists);

          
            detail.setCategories(String.join(",", categoryLists));

           
            restaurantListResponse.addRestaurantsItem(detail);
        }

        
        return new ResponseEntity<RestaurantListResponse>(restaurantListResponse, HttpStatus.OK);
    }

    
    @RequestMapping(method = RequestMethod.GET, path = "/restaurant/name/{restaurant_name}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantListResponse> getRestaurantsByName(@PathVariable String restaurant_name)
            throws RestaurantNotFoundException {

      
        if(restaurant_name == null || restaurant_name.isEmpty() || restaurant_name.equalsIgnoreCase("\"\"")){
            throw new RestaurantNotFoundException("RNF-003", "Restaurant name field should not be empty");
        }

        
        final List<RestaurantEntity> allRestaurants = restaurantBusinessService.getRestaurantsByName(restaurant_name);

        RestaurantListResponse restaurantListResponse = new RestaurantListResponse();

         
        List<RestaurantList> details = new ArrayList<RestaurantList>();
        for (RestaurantEntity n: allRestaurants) {
            RestaurantList detail = new RestaurantList();
            detail.setId(UUID.fromString(n.getUuid()));
            detail.setRestaurantName(n.getRestaurantName());
            detail.setPhotoURL(n.getPhotoUrl());
            detail.setCustomerRating(n.getCustomerRating());
            detail.setAveragePrice(n.getAvgPriceForTwo());
            detail.setNumberCustomersRated(n.getNumCustomersRated());

             
            AddressEntity addressEntity = addressService.getAddressById(n.getAddress().getId());
            RestaurantDetailsResponseAddress responseAddress = new RestaurantDetailsResponseAddress();

            responseAddress.setId(UUID.fromString(addressEntity.getUuid()));
            responseAddress.setFlatBuildingName(addressEntity.getFlatBuildingNumber());
            responseAddress.setLocality(addressEntity.getLocality());
            responseAddress.setCity(addressEntity.getCity());
            responseAddress.setPincode(addressEntity.getPincode());

           
            StateEntity stateEntity = stateBusinessService.getStateById(addressEntity.getState().getId());
            RestaurantDetailsResponseAddressState responseAddressState = new RestaurantDetailsResponseAddressState();

            responseAddressState.setId(UUID.fromString(stateEntity.getUuid()));
            responseAddressState.setStateName(stateEntity.getStateName());
            responseAddress.setState(responseAddressState);

             
            detail.setAddress(responseAddress);

             
            List<String> categoryLists = new ArrayList();
            for (CategoryEntity categoryEntity :n.getCategoryEntities()) {
                categoryLists.add(categoryEntity.getCategoryName());
            }

             
            Collections.sort(categoryLists);

             
            detail.setCategories(String.join(",", categoryLists));

             

            restaurantListResponse.addRestaurantsItem(detail);

        }

      
        return new ResponseEntity<RestaurantListResponse>(restaurantListResponse, HttpStatus.OK);
    }

    
    @RequestMapping(method = RequestMethod.GET, path = "/restaurant/category/{category_id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity getRestaurantByCategoryId(@PathVariable String category_id) throws CategoryNotFoundException {

         
        if(category_id == null || category_id.isEmpty() || category_id.equalsIgnoreCase("\"\"")){
            throw new CategoryNotFoundException("CNF-001", "Category id field should not be empty");
        }

         
        CategoryEntity matchedCategory = categoryBusinessService.getCategoryEntityByUuid(category_id);

        
        if(matchedCategory == null){
            throw new CategoryNotFoundException("CNF-002", "No category by this id");
        }

         
        final List<RestaurantCategoryEntity> allRestaurantCategories = restaurantBusinessService.getRestaurantByCategoryId(matchedCategory.getId());

         
        List<RestaurantList> details = new ArrayList<RestaurantList>();
        for (RestaurantCategoryEntity restaurantCategoryEntity:allRestaurantCategories) {
            RestaurantEntity n = restaurantCategoryEntity.getRestaurant();
            RestaurantList detail = new RestaurantList();
            detail.setId(UUID.fromString(n.getUuid()));
            detail.setRestaurantName(n.getRestaurantName());
            detail.setPhotoURL(n.getPhotoUrl());
            detail.setCustomerRating(n.getCustomerRating());
            detail.setAveragePrice(n.getAvgPriceForTwo());
            detail.setNumberCustomersRated(n.getNumCustomersRated());

          
            AddressEntity addressEntity = addressService.getAddressById(n.getAddress().getId());
            RestaurantDetailsResponseAddress responseAddress = new RestaurantDetailsResponseAddress();

            responseAddress.setId(UUID.fromString(addressEntity.getUuid()));
            responseAddress.setFlatBuildingName(addressEntity.getFlatBuildingNumber());
            responseAddress.setLocality(addressEntity.getLocality());
            responseAddress.setCity(addressEntity.getCity());
            responseAddress.setPincode(addressEntity.getPincode());

             
            StateEntity stateEntity = stateBusinessService.getStateById(addressEntity.getState().getId());
            RestaurantDetailsResponseAddressState responseAddressState = new RestaurantDetailsResponseAddressState();

            responseAddressState.setId(UUID.fromString(stateEntity.getUuid()));
            responseAddressState.setStateName(stateEntity.getStateName());
            responseAddress.setState(responseAddressState);

             
            detail.setAddress(responseAddress);

             
            List<String> categoryLists = new ArrayList();
            for (CategoryEntity categoryEntity :n.getCategoryEntities()) {
                categoryLists.add(categoryEntity.getCategoryName());
            }

           
            Collections.sort(categoryLists);

             
            detail.setCategories(String.join(",", categoryLists));
 
            details.add(detail);
        }

         
        return new ResponseEntity<>(details, HttpStatus.OK);
    }

    
    
    @RequestMapping(method = RequestMethod.PUT, path = "/restaurant/{restaurant_id}",consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantUpdatedResponse> updateCustomerRating(@RequestHeader("authorization") final String authorization, @RequestParam Double customerRating, @PathVariable String restaurant_id )
            throws AuthorizationFailedException, InvalidRatingException, RestaurantNotFoundException {

         
        String[] bearerToken = authorization.split("Bearer ");

         
        RestaurantEntity restaurantEntity = restaurantBusinessService.updateCustomerRating(customerRating, restaurant_id, bearerToken[1]);

         
        RestaurantUpdatedResponse restaurantUpdatedResponse = new RestaurantUpdatedResponse()
                .id(UUID.fromString(restaurantEntity.getUuid())).status("RESTAURANT RATING UPDATED SUCCESSFULLY");

         
        return new ResponseEntity<RestaurantUpdatedResponse>(restaurantUpdatedResponse, HttpStatus.OK);
    }

}
