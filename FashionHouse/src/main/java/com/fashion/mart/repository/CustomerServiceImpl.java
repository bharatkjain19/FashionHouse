package com.fashion.mart.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.fashion.mart.model.Customer;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<Customer> findAll() {

		String sql = "SELECT * FROM Customer";

		List<Customer> customers = jdbcTemplate.query(sql, new BeanPropertyRowMapper(Customer.class));

		return customers;
	}
}
