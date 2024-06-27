package com.prachi.service;

import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.prachi.dto.StudentOrder;
import com.prachi.repo.StudentOrderRepo;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.razorpay.*;

import com.prachi.dto.StudentOrder;
import com.prachi.repo.StudentOrderRepo;

@Service
public class StudentService {
	
	@Autowired
	private StudentOrderRepo studentRepo;

	private RazorpayClient client;

	// Field injection for Razor pay keys
	@Value("${razorpay.key.id}")
	private String razorPayKey;

	@Value("${razorpay.secret.key}")
	private String razorPaySecret;

	/*
	 * Method to create an order using Razor pay client
	 */
	public StudentOrder createOrder(StudentOrder stuOrder) throws Exception {
		/*
		 * converting the studentOrder into Json object and storing the amount and
		 * currency and receipt as json object
		 * amount we are getting it in rupees and we have to convert
		 * it into paise that's why *100
		 */
		JSONObject orderReq = new JSONObject();
		orderReq.put("amount", stuOrder.getAmount()*100 ); // amount in paisa
		orderReq.put("currency", "INR");
		orderReq.put("receipt", stuOrder.getEmail());

		/*
		 * Initialize Razorpay client with keys razorpay 
		 * client object we are creating
		 * using key and secret
		 * so in which razorpay account the amount should be added so the api 
		 * keys we have configured here with the razorpay client object by this
		 * we are then calling orders.create method
		 */
		this.client = new RazorpayClient(razorPayKey, razorPaySecret);

		/*
		 * Create order in Razorpay and will generate the order id 
		 * how much amount the customer should pay for that one order 
		 * will be created it will take time because whatever the json we prepared
		 * we are giving that json object as a input for the create method 
		 * it should create the order and it should give the order
		 */
		Order razorPayOrder = client.orders.create(orderReq);
		System.out.println(razorPayOrder);
		/*
		 * Set Razor pay order ID and status to student order 
		 * taking that order id and
		 * order status and saving into db table
		 */
		stuOrder.setRazorpayOrderId(razorPayOrder.get("id"));
		stuOrder.setOrderStatus(razorPayOrder.get("status"));

		// Save student order using repository to db
		studentRepo.save(stuOrder);

		return stuOrder;
	}
	/* Logic to get and update the details into the db */
	public StudentOrder updateOrder(Map<String,String>responsePayLoad) {
		String razorPayOrderId = responsePayLoad.get("razorpay_order_id");
		StudentOrder order = studentRepo.findByRazorPayId(razorPayOrderId);
		order.setOrderStatus("Payment_Completed");
		StudentOrder updatedOrder = studentRepo.save(order);
		return updatedOrder;
		
		}
}
