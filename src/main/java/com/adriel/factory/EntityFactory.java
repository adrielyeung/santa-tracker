package com.adriel.factory;

import com.adriel.entity.Person;
import com.adriel.entity.Message;
import com.adriel.entity.Order;
import com.adriel.entity.OrderDetail;
import com.adriel.entity.Product;
import com.adriel.entity.SantaTrackerEntity;
import com.adriel.entity.SantaTrackerEntityType;

public class EntityFactory {
	public static SantaTrackerEntity getEntity(SantaTrackerEntityType ste) {
		SantaTrackerEntity ent = null;
		
		switch (ste) {
		case PERSON:
			ent = new Person();
			break;
		case PRODUCT:
			ent = new Product();
			break;
		case ORDER:
			ent = new Order();
			break;
		case ORDERDETAIL:
			ent = new OrderDetail();
			break;
		case MESSAGE:
			ent = new Message();
			break;
		}
		return ent;
	}
}
