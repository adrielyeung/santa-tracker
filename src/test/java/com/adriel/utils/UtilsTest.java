package com.adriel.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.Assert.*;

@ExtendWith(MockitoExtension.class)
public class UtilsTest {
	
	@Test
	public void whenGenerateRandomAlphanumericString_thenReturnRandomStringOfSelectedLength() {
		
		String randomStringOfLength10 = Utils.generateRandomAlphanumericString(10);
		
		assertEquals(10, randomStringOfLength10.length());
	}
	
}
