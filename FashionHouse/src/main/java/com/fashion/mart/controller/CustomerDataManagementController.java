package com.fashion.mart.controller;

import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fashion.mart.model.Customer;
import com.fashion.mart.repository.CustomerService;

@RestController
public class CustomerDataManagementController {

	@Autowired
	JobLauncher jobLauncher;

	@Autowired
	Job processJob;

	@Autowired
	CustomerService customerService;

	@PostMapping("/sales/csv")
	public String handle() throws Exception {

		JobParameters jobParameters = new JobParametersBuilder()
				.addString("JobID", String.valueOf(System.currentTimeMillis())).toJobParameters();
		jobLauncher.run(processJob, jobParameters);

		return "Customer CSV Data Loading Batch Job has been invoked";
	}

	@GetMapping("/sales/json")
	public List<Customer> getCustomerData() throws Exception {

		return customerService.findAll();
	}
}