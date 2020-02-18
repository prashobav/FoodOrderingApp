package com.upgrad.FoodOrderingApp.api.controller;
import com.upgrad.FoodOrderingApp.api.model.ItemList;
import com.upgrad.FoodOrderingApp.api.model.ItemListResponse;
import com.upgrad.FoodOrderingApp.service.businness.ItemService;
import com.upgrad.FoodOrderingApp.service.businness.RestaurantBusinessService;
import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
@ComponentScan
@RestController
@CrossOrigin
@RequestMapping("/")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private RestaurantBusinessService restaurantBusinessService;

    @RequestMapping(method = RequestMethod.GET, path = "/item/restaurant/{restaurant_id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<ItemListResponse> getItemByPopularity(@PathVariable("restaurant_id") final String restaurantUuid)
            throws RestaurantNotFoundException {

        RestaurantEntity restaurantEntity = restaurantBusinessService.getRestaurantByUUId(restaurantUuid);

        if (restaurantEntity == null) {
            throw new RestaurantNotFoundException("RNF-001", "No restaurant by this id");
        }

        List<ItemEntity> itemEntityList = itemService.getItemsByPopularity(restaurantEntity);

        ItemListResponse itemListResponse = new ItemListResponse();

        int itemCount = 0;

       

        return new ResponseEntity<ItemListResponse>(itemListResponse, HttpStatus.OK);
    }
}
