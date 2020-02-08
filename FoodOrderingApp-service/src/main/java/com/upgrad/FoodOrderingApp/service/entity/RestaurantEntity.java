package com.upgrad.FoodOrderingApp.service.entity;

public class RestaurantEntity {
	id
	SERIAL
	uuid VARCHAR(200) UNIQUE NOT NULL
	 restaurant_name VARCHAR(50) NOT NULL
	 photo_url VARCHAR(255)
	 customer_rating DECIMAL NOT NULL
	 average_price_for_two INTEGER NOT NULL
	 number_of_customers_rated INTEGER NOT NULL DEFAULT 0
	 address_id INTEGER NOT NULL 
	 PRIMARY KEY(id)
	 FOREIGN KEY (address_id) REFERENCES ADDRESS(id) ON DELETE CASCADE);

}
