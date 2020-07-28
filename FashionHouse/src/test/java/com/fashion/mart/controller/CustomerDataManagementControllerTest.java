package com.fashion.mart.controller;

import java.net.URI;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fashion.mart.FashionHouseApp;
import com.fashion.mart.config.CustomerDataLoadingBatch;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = FashionHouseApp.class)
@TestPropertySource(locations="classpath:application.properties")
@ContextConfiguration(classes = { CustomerDataLoadingBatch.class }, 
		  loader = AnnotationConfigContextLoader.class)
@Transactional
public class CustomerDataManagementControllerTest {

	@Mock
	JobLauncher jobLauncher;

	@Mock
	Job processJob;
	
	@Mock
	CustomerDataManagementController customerDataManagementController;

	@Ignore
	@Test
	public void testLoadCSVSuccess() throws Exception 
	{
	    RestTemplate restTemplate = new RestTemplate();
	     
	    final String baseUrl = "http://localhost:8080/sales/csv";
	    URI uri = new URI(baseUrl);
	    ResponseEntity<String> result = restTemplate.postForEntity(uri, null, null);

	     
	    //Verify request succeed
	    Assert.assertEquals(200, result.getStatusCodeValue());
	    Assert.assertEquals(true, result.getBody().contains("customerData"));
	}
	@Ignore
	@Test
	public void testGetCustomerDataSuccess() throws Exception 
	{
	    RestTemplate restTemplate = new RestTemplate();
	     
	    final String baseUrl = "http://localhost:8080/sales/json";
	    URI uri = new URI(baseUrl);
	    ResponseEntity<String> result = restTemplate.getForEntity(uri, String.class);

	    //Verify request succeed
	    Assert.assertEquals(200, result.getStatusCodeValue());
	    Assert.assertEquals(true, result.getBody().contains("customerList"));
	}
}