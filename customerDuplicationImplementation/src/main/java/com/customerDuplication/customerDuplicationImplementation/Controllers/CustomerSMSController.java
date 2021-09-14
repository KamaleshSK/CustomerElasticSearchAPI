package com.customerDuplication.customerDuplicationImplementation.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.customerDuplication.customerDuplicationImplementation.Models.CustomerProfileDocument;
import com.customerDuplication.customerDuplicationImplementation.Models.CustomerSMSDocument;
import com.customerDuplication.customerDuplicationImplementation.Services.CustomerSMSService;

@RestController
@RequestMapping("/api/v2")
public class CustomerSMSController {
	
	@Autowired
	private CustomerSMSService service;
	
	// create new customer sms entry
	@PostMapping("/smsprofiles")
	public ResponseEntity<String> createCustomerSMS(@RequestBody CustomerSMSDocument document) throws Exception {
		return new ResponseEntity<String>(service.createSMSDocument(document), HttpStatus.CREATED);
	}
	
	@PostMapping("/smsprofiles/bulk") 
	public ResponseEntity<String> createBulkCustomerSMS(@RequestBody List<CustomerSMSDocument> documentList) throws Exception {
		return new ResponseEntity<String>(service.createBulkSMSDocument(documentList), HttpStatus.CREATED);
	}
	
	//get all customer profiles details
    @GetMapping("/smsprofiles")
    public List<CustomerSMSDocument> findAll() throws Exception {
           return service.findAll();
    }
    
    //change to delete mapping after implementation of deleteRequest in service
    @GetMapping("/smsprofiles/clearjunk")
    public List<CustomerSMSDocument> deleteJunk(@RequestBody List<String> keywordList) throws Exception {
    	return service.deleteJunkSMS(keywordList);
    }
}
