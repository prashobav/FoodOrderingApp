package com.upgrad.FoodOrderingApp.service.entity;

public class AddressEntity {
	id
	SERIAL
	 uuid VARCHAR(200) UNIQUE NOT NULL
	flat_buil_number VARCHAR(255)
	 locality VARCHAR(255)
	city VARCHAR(30)
	pincode VARCHAR(30)
	 state_id INTEGER
	 active INTEGER DEFAULT(1)
	 PRIMARY KEY (id)
	FOREIGN KEY (state_id) REFERENCES STATE(id) ON DELETE CASCADE);

}
