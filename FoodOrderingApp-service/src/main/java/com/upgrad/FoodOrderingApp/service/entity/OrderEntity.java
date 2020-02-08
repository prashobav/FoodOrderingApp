package com.upgrad.FoodOrderingApp.service.entity;

public class OrderEntity {
	id
	SERIAL
	uuid VARCHAR(200) UNIQUE NOT NULL
	 bill DECIMAL NOT NULL
	 coupon_id INTEGER
	discount DECIMAL DEFAULT 0
	 date TIMESTAMP NOT NULL 
	 payment_id INTEGER
	customer_id INTEGER NOT NULL
	 address_id INTEGER NOT NULL
	PRIMARY KEY(id)
	 restaurant_id INTEGER NOT NULL 
	FOREIGN KEY (payment_id) REFERENCES PAYMENT(id)
	FOREIGN KEY (restaurant_id) REFERENCES RESTAURANT(id)
	FOREIGN KEY (customer_id) REFERENCES CUSTOMER(id) ON DELETE CASCADE
	 FOREIGN KEY (address_id) REFERENCES ADDRESS(id)
	FOREIGN KEY (coupon_id) REFERENCES COUPON(id));

}
