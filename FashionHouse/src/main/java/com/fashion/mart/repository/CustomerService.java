package com.fashion.mart.repository;

import java.util.List;

import com.fashion.mart.model.Customer;

public interface CustomerService {
	
    public List<Customer> findAll();

}