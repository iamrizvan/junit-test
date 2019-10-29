package org.studyeasy.controller;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.studyeasy.exception.UserServiceException;
import org.studyeasy.request.model.UserRequestModel;
import org.studyeasy.response.model.AddresseResponseModel;
import org.studyeasy.response.model.DeleteResponseModel;
import org.studyeasy.response.model.ErrorMessages;
import org.studyeasy.response.model.UserResponseModel;
import org.studyeasy.service.AddressService;
import org.studyeasy.service.UserService;
import org.studyeasy.shared.dto.AddressDTO;
import org.studyeasy.shared.dto.UserDTO;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;;

@RestController
@RequestMapping("/users") // http:localhost:8080/mobile-app-ws/users
public class UserController {

	@Autowired
	UserService userService;

	@Autowired
	AddressService addressService;

	@GetMapping("/{userId}")
	public UserResponseModel getUserById(@PathVariable("userId") String userId) {
		ModelMapper modelMapper = new ModelMapper();
		UserDTO returnUser = userService.getUserById(userId);
		return modelMapper.map(returnUser, UserResponseModel.class);
	}
	
	/*
	@GetMapping("/{email}")
	public UserResponseModel getUser(@PathVariable("email") String email) {
		ModelMapper modelMapper = new ModelMapper();
		UserDTO returnUser = userService.getUser(email);
		return modelMapper.map(returnUser, UserResponseModel.class);
	}
*/
	@PostMapping
	public UserResponseModel createUser(@RequestBody UserRequestModel userDetails){
		System.out.println(ErrorMessages.MISSING_REQUIRED_FIELDS.getErrorMessage());
		if (userDetails.getFirstName().isEmpty()) throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELDS.getErrorMessage());
		ModelMapper modelMapper = new ModelMapper();
		UserDTO userDto = modelMapper.map(userDetails, UserDTO.class);
		UserDTO createdUser = userService.createUser(userDto);
		UserResponseModel returnValue = modelMapper.map(createdUser, UserResponseModel.class);
		return returnValue;
	}

	@PutMapping("/{userId}")
	public UserResponseModel updateUser(@PathVariable String userId, @RequestBody UserRequestModel updatedUser) {
		if (updatedUser.getFirstName().isEmpty())
			throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELDS.getErrorMessage());
		ModelMapper modelMapper = new ModelMapper();
		UserDTO userDto = modelMapper.map(updatedUser, UserDTO.class);
		UserDTO createdUser = userService.updateUser(userId, userDto);
		UserResponseModel returnValue = modelMapper.map(createdUser, UserResponseModel.class);
		return returnValue;
	}

	@DeleteMapping("/{userId}")
	public DeleteResponseModel deleteUser(@PathVariable("userId") String userId) {
		if (userId == null || userId.equalsIgnoreCase(""))
			throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		String result = userService.deleteUser(userId);
		DeleteResponseModel response = new DeleteResponseModel();
		response.setMessage(result);
		return response;
	}

	@GetMapping
	public List<UserResponseModel> getUsers(@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "limit", defaultValue = "50") int limit) {
		ModelMapper modelMapper = new ModelMapper();
		List<UserResponseModel> returnValue = new ArrayList<>();
		List<UserDTO> users = userService.getUsers(page, limit);
		for (UserDTO userDTO : users) {
			UserResponseModel userResponseModel = modelMapper.map(userDTO, UserResponseModel.class);
			returnValue.add(userResponseModel);
		}
		return returnValue;
	}

	// http://localhost:8080/mobile-app-ws/users/kjdadgfdavty/addresses
	@GetMapping(path = "/{userId}/addresses", produces = {"application/hal+json"})
	public Resources<AddresseResponseModel> getUserAddresses(@PathVariable String userId) {
		ModelMapper modelMapper = new ModelMapper();
		List<AddresseResponseModel> returnValue = new ArrayList<>();
		List<AddressDTO> addressDto = addressService.getAddresses(userId);
		
		if (addressDto != null && !addressDto.isEmpty()) {
			Type listType = new TypeToken<List<AddresseResponseModel>>() {
			}.getType(); // Type java.lang.reflect package import
			returnValue = modelMapper.map(addressDto, listType);
			for(AddresseResponseModel addresses:returnValue)
			{
				Link addressLink = linkTo(methodOn(UserController.class).getUserAddress(userId,addresses.getAddressId())).withSelfRel();
				addresses.add(addressLink);
				Link userLink = linkTo(methodOn(UserController.class).getUserById(userId)).withRel("user");
				addresses.add(userLink);
			}
		}
		return new Resources<>(returnValue);	
	}
	

	// http://localhost:8080/mobile-app-ws/users/kjdadgfdavty/addresses//lksd6549dehfuisudh
	@GetMapping(path = "/{userId}/addresses/{addressId}" , produces = {"application/hal+json"})
	public Resource<AddresseResponseModel> getUserAddress(@PathVariable String userId, @PathVariable String addressId) {
		ModelMapper modelMapper = new ModelMapper(); 
		AddresseResponseModel returnValue = null;
		AddressDTO addressDto = addressService.getAddress(addressId);		
		// HATEOAS link
		Link addressLink = linkTo(methodOn(UserController.class).getUserAddress(userId, addressId)).withSelfRel(); 
		Link userLink = linkTo(UserController.class).slash(userId).withRel("user");
		Link addressesLink = linkTo(methodOn(UserController.class).getUserAddresses(userId)).withRel("addresses");
		if (addressDto != null ) {
			returnValue = modelMapper.map(addressDto, AddresseResponseModel.class);
		}
		returnValue.add(addressLink);
		returnValue.add(userLink);
		returnValue.add(addressesLink);		
		return new Resource<>(returnValue);
	}

}
