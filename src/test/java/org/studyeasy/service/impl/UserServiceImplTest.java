package org.studyeasy.service.impl;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.studyeasy.entity.AddressEntity;
import org.studyeasy.entity.UserEntity;
import org.studyeasy.exception.NonUniqueException;
import org.studyeasy.shared.dto.AddressDTO;
import org.studyeasy.shared.dto.UserDTO;
import org.studyeasy.shared.io.Utils;
import org.studyeasy.shared.io.repositories.UserRepository;

class UserServiceImplTest {

	String userId = "lkdjfm32kndo2mk";
	String addressId = "jdkmbAsvcgs2s452k";
	String encryptedPassword = "kdn3lm4lmnk5lm2lm";
	UserEntity userEntity;

	@InjectMocks
	UserServiceImpl userServiceImpl;

	@Mock
	UserRepository userRepository;
	
	@Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;	

	@Mock
	Utils utils;

	@BeforeEach
	void setUp() throws Exception {
		
		MockitoAnnotations.initMocks(this);

		userEntity = new UserEntity();
		userEntity.setId(15);
		userEntity.setFirstName("Sergey");
		userEntity.setUserId(userId);
		userEntity.setEncryptedPassword(encryptedPassword);
		userEntity.setEmail("test@test.com");
		userEntity.setEmailVerificationToken("daksnd32lkmdewoi4");
		userEntity.setAddresses(getAddressEntity());
		
	}

	@Test
	void testGetUsers() {
		when(userRepository.findByEmail(anyString())).thenReturn(userEntity);
		UserDTO userDto = userServiceImpl.getUser("test@test.com");
		assertNotNull(userDto);
		assertEquals("Sergey", userDto.getFirstName());
	}
	

	// @Test(expected= UserServiceException.class) // JUnit 4
	@Test
	final void testGetUser_UsernameNotFoundException() {
		when(userRepository.findByEmail(anyString())).thenReturn(null);
		assertThrows(UsernameNotFoundException.class, () -> {                     
			userServiceImpl.getUser("test@gmail.com");
		});     // lamda expression. // Junit 5
	}
	
	
	@Test
	final void testCreateUser_CreateUserNonUniqueException() {
		when(userRepository.findByEmail(anyString())).thenReturn(userEntity);
		UserDTO userDto = new UserDTO();
		userDto.setAddresses(getAddressDTO()); 		
		userDto.setFirstName("Sergey");
		userDto.setLastName("Kumar");
		userDto.setPassword("123456");
		userDto.setEmail("test@test.com");
		
		assertThrows(NonUniqueException.class, () -> {                     
			userServiceImpl.createUser(userDto);
		}); 
	}
	
	
	@Test
	final void testCreateUser() {
		
		when(userRepository.findByEmail(anyString())).thenReturn(null);
	    when(utils.generateAddressId(anyInt())).thenReturn(addressId);
	    when(utils.generateUserId(anyInt())).thenReturn(userId);
		when(bCryptPasswordEncoder.encode(anyString())).thenReturn(encryptedPassword);
		when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
		
		UserDTO userDto = new UserDTO();
		userDto.setAddresses(getAddressDTO()); 		
		userDto.setFirstName("Sergey");
		userDto.setLastName("Kumar");
		userDto.setPassword("123456");
		userDto.setEmail("test@test.com");
		UserDTO storedUserDetails = userServiceImpl.createUser(userDto);
		
		assertNotNull(storedUserDetails);
		assertEquals(userEntity.getFirstName(), storedUserDetails.getFirstName());
		assertEquals(userEntity.getLastName(), storedUserDetails.getLastName());
		assertNotNull(storedUserDetails.getUserId());
		assertEquals(storedUserDetails.getAddresses().size(), userEntity.getAddresses().size());
	//  	verify(utils,times(storedUserDetails.getAddresses().size())).generateAddressId(30);
		verify(bCryptPasswordEncoder,times(1)).encode("123456");
		verify(userRepository,times(1)).save(any(UserEntity.class));
		
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
	
	private List<AddressEntity> getAddressEntity()
	{
		List<AddressDTO> addresses = getAddressDTO();
		Type listType = new TypeToken<List<AddressEntity>>() {}.getType();
		return new ModelMapper().map(addresses,listType);
	}
}
