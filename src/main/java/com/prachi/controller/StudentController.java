package com.prachi.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.prachi.dto.StudentOrder;
import com.prachi.service.StudentService;

@Controller
public class StudentController {

@Autowired
private StudentService service;
//this method is used to display the ui page
	@GetMapping("/")
	public String init() {
		return "index";
	}
	/*
	 * when for is submitted i want to capture the data of the form and create an
	 * order in the database taking @RequestBody annotation to get the form data
	 */
	@PostMapping(value="/create-order",produces="application/json")
	@ResponseBody//to provide direct response from the controller method(controller+responsebody=restController)
	public ResponseEntity<StudentOrder>createOrder(@RequestBody StudentOrder studentOrder)throws Exception{
		StudentOrder createdOrder = service.createOrder(studentOrder);
		return new ResponseEntity<>(createdOrder,HttpStatus.CREATED);
	}
	/* the method to update the order record in the database */
	@PostMapping("/handle-payment-callback")
	public String handlePaymentCallBack(Map<String,String>respPayLoad) {
		System.err.println(respPayLoad);
	 service.updateOrder(respPayLoad);
		return "Success";
	}
}
