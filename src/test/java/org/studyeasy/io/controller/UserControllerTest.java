package org.studyeasy.io.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.studyeasy.controller.UserController;
import org.studyeasy.response.model.UserResponseModel;
import org.studyeasy.service.impl.UserServiceImpl;
import org.studyeasy.shared.dto.AddressDTO;
import org.studyeasy.shared.dto.UserDTO;


class UserControllerTest {
	
	UserDTO userDto;
	final String USER_ID = "lmsekn3FslkdKS8dskl";
	
	@InjectMocks
	UserController userController;
	
	@Mock
	UserServiceImpl userService;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		userDto =new UserDTO();
		userDto.setFirstName("Sergey");
		userDto.setLastName("Lana");
		userDto.setEmail("s@lana.com");
		userDto.setEmailVerificationStatus(Boolean.FALSE);
		userDto.setEmailVerificationToken(null);
		userDto.setUserId(USER_ID);
		userDto.setAddresses(getAddressDTO());
		userDto.setEncryptedPassword("lksam3kn9dJlksdml");
	}

	@Test
	void testGetUserById() {
		when(userService.getUserById(anyString())).thenReturn(userDto);
		
		UserResponseModel userResponse = userController.getUserById(USER_ID);
		
		assertNotNull(userResponse);
		assertEquals(USER_ID,userResponse.getUserId());
		assertEquals(userResponse.getFirstName(), userDto.getFirstName());
		assertEquals(userResponse.getLastName(), userDto.getLastName());
		assertTrue(userResponse.getAddresses().size() == userDto.getAddresses().size());
		
	}


	private List<AddressDTO> getAddressDTO() {
		AddressDTO addressDto = new AddressDTO();
		addressDto.setType("shipping");
		addressDto.setCity("Lucknow");
		addressDto.setCountry("India");
		addressDto.setStreetName("Gomti Nagar");
		addressDto.setPostalCode("222154");
		
		AddressDTO billingAddressDto = new AddressDTO();
		billingAddressDto.setType("billing");
		billingAddressDto.setCity("Lucknow");
		billingAddressDto.setCountry("India");
		billingAddressDto.setStreetName("Gandhi Market");
		billingAddressDto.setPostalCode("222160");
		
		List<AddressDTO> addresses = new ArrayList<>();
		addresses.add(addressDto);
		addresses.add(billingAddressDto);
		return addresses;
	}
}
