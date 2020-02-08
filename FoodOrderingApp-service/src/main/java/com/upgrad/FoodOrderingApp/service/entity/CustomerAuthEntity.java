package com.upgrad.FoodOrderingApp.service.entity;

public class CustomerAuthEntity {
	id
	SERIAL
	uuid VARCHAR(200) UNIQUE NOT NULL
	 customer_id INTEGER NOT NULL
	 access_token VARCHAR(500)
	 login_at TIMESTAMP
	 logout_at TIMESTAMP
	expires_at TIMESTAMP
	 PRIMARY KEY (id)
	 FOREIGN KEY (customer_id) REFERENCES CUSTOMER(id) ON DELETE CASCADE);

}
