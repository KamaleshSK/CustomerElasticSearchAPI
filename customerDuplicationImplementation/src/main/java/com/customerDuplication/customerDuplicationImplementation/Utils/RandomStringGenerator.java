package com.customerDuplication.customerDuplicationImplementation.Utils;

import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class RandomStringGenerator {

	public String generateRandomString() {
		int leftLimit = 97; // letter 'a'
	    int rightLimit = 122; // letter 'z'
	    int targetStringLength = 7;
	    Random random = new Random();

	    String generatedString = random.ints(leftLimit, rightLimit + 1)
	      .limit(targetStringLength)
	      .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
	      .toString();

	    return generatedString;
	}
	
}
