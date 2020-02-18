package com.upgrad.FoodOrderingApp.service.businness;
import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.dao.RestaurantDao;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantCategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.InvalidRatingException;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.UpdateCustomerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

import java.util.List;

@Service
public class RestaurantBusinessService {
	 @Autowired
	    private RestaurantDao restaurantDao;

	    @Autowired
	    private CustomerDao customerDao;

	    @Autowired
	    private CustomerService customerService;

	     
	    public List<RestaurantEntity> getAllRestaurants() {
	        return restaurantDao.getAllRestaurants();
	    }

	   public List<RestaurantEntity> getRestaurantsByName(String restaurantName) {
	        return restaurantDao.getRestaurantsByName(restaurantName);
	    }

	     public List<RestaurantCategoryEntity> getRestaurantByCategoryId(final Long categoryID) {
	        return restaurantDao.getRestaurantByCategoryId(categoryID);
	    }

	     public RestaurantEntity getRestaurantByUUId(String restaurantUUID) {
	        return restaurantDao.getRestaurantByUUId(restaurantUUID);
	    }

	    @Transactional
	    public RestaurantEntity updateCustomerRating (final Double customerRating, final String restaurant_id, final String authorizationToken)
	            throws AuthorizationFailedException, RestaurantNotFoundException, InvalidRatingException {

	        final ZonedDateTime now = ZonedDateTime.now();

	         customerService.validateAccessToken(authorizationToken);

	        if(restaurant_id == null || restaurant_id.isEmpty() || restaurant_id.equalsIgnoreCase("\"\"")){
	            throw new RestaurantNotFoundException("RNF-002", "Restaurant id field should not be empty");
	        }

	         RestaurantEntity restaurantEntity =  restaurantDao.getRestaurantByUUId(restaurant_id);

	        if (restaurantEntity == null) {
	            throw new RestaurantNotFoundException("RNF-001", "No restaurant by this id");
	        }

	         if(customerRating == null || customerRating.isNaN() || customerRating < 1 || customerRating > 5 ){
	            throw new InvalidRatingException("IRE-001", "Restaurant should be in the range of 1 to 5");
	        }

	         BigDecimal oldRatingCalculation = (restaurantEntity.getCustomerRating().multiply(new BigDecimal(restaurantEntity.getNumCustomersRated())));
	        BigDecimal calculatedRating = (oldRatingCalculation.add(new BigDecimal(customerRating))).divide(new BigDecimal(restaurantEntity.getNumCustomersRated() + 1));
	        restaurantEntity.setCustomerRating(calculatedRating);
	        restaurantEntity.setNumCustomersRated(restaurantEntity.getNumCustomersRated() + 1);

	         restaurantDao.updateRestaurant(restaurantEntity);
	        return restaurantEntity;
	    }

}
