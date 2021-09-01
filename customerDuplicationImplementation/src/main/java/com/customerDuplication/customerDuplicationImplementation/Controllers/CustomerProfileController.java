package com.customerDuplication.customerDuplicationImplementation.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.customerDuplication.customerDuplicationImplementation.Models.CustomerProfileDocument;
import com.customerDuplication.customerDuplicationImplementation.Services.CustomerProfileService;

@RestController
@RequestMapping("/api/v1")
public class CustomerProfileController {
	
	@Autowired
	private CustomerProfileService service;
	
	@PostMapping("/profiles")
	public ResponseEntity<String> createCustomerProfile(@RequestBody CustomerProfileDocument document) throws Exception {
		return new ResponseEntity<String>(service.createProfileDocument(document), HttpStatus.CREATED);
	}
	
    @GetMapping("/profiles/{id}")
    public CustomerProfileDocument findById(@PathVariable String id) throws Exception {
        return service.findById(id);
    }
    
    @PutMapping("/profiles")
    public ResponseEntity<String> updateProfile(@RequestBody CustomerProfileDocument document) throws Exception {
        return new ResponseEntity<String>(service.updateProfile(document), HttpStatus.CREATED);
    }
    
    @GetMapping("/profiles")
    public List<CustomerProfileDocument> findAll() throws Exception {
           return service.findAll();
    }
    
    @DeleteMapping("profiles/{id}")
    public String deleteProfile(@PathVariable String id) throws Exception {
         return service.deleteProfileDocument(id);

    }
    
    @GetMapping(value = "profiles/search")
    public List<CustomerProfileDocument> search(@RequestParam(value = "phoneNumber") String phoneNumber) throws Exception {
    	return service.searchByPhoneNumber(phoneNumber);
    }
    
    @GetMapping(value = "profiles/countPhoneNumber")
    public long countPhoneNumberMatches(@RequestParam(value = "phoneNumber") String phoneNumber) throws Exception {
    	return service.getMatchingPhoneNumberCount(phoneNumber);
    }
    
    @GetMapping(value = "profiles/countPanNumber")
    public long countPanNumberMatches(@RequestParam(value = "panNumber") String panNumber) throws Exception {
    	return service.getMatchingPanNumberCount(panNumber);
    }
	
    @GetMapping(value = "profiles/duplicateCheckPhoneNumber")
    public boolean isDuplicatePhoneNumber(@RequestParam(value = "phoneNumber") String phoneNumber) throws Exception {
    	return service.isDuplicatePhoneNumber(phoneNumber);
    }
    
    @GetMapping(value = "profiles/duplicateCheckPanNumber")
    public boolean isDuplicatePanNumber(@RequestParam(value = "panNumber") String panNumber) throws Exception {
    	return service.isDuplicatePanNumber(panNumber);
    }
}
