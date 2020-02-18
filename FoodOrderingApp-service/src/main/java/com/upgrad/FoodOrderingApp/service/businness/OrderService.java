package com.upgrad.FoodOrderingApp.service.businness;

 
import com.upgrad.FoodOrderingApp.service.dao.CustomerAddressDao;
import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.dao.OrderDao;
import com.upgrad.FoodOrderingApp.service.dao.OrderItemDao;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private CustomerService customerService;
 

    @Autowired
    private ItemService itemService;

    @Autowired
    private OrderItemDao orderItemDao;

    @Transactional
    public CouponEntity getCouponByName(String couponName, final String authorizationToken) throws AuthorizationFailedException {

        
        customerService.validateAccessToken(authorizationToken);
        return orderDao.getCouponByName(couponName);
    }

    @Transactional
    public List<OrdersEntity> getCustomerOrders(final CustomerEntity customerEntity) {

        return orderDao.getCustomerOrders(customerEntity);
    }

    
}
