package org.studyeasy.controller;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.studyeasy.entity.ErrorMessages;
import org.studyeasy.entity.UserEntity;
import org.studyeasy.exception.UserServiceException;
import org.studyeasy.response.model.UserResponseModel;
import org.studyeasy.service.impl.UserServiceImpl;
import org.studyeasy.service.impl.UserServiceImplementation;
import org.studyeasy.shared.dto.UserDTO;

@RestController
@RequestMapping("users") // http:localhost:8080/users
public class UserController {

	@Autowired
	UserServiceImplementation userService;
	
	@Autowired
	UserServiceImpl userServiceImpl;
	
	/*
	 * @RequestMapping(method = RequestMethod.GET) public List<UserEntity> getUser()
	 * { return userService.getUsers(); }
	 */
	
	@GetMapping("/{userId}")
	public UserEntity getUserById(@PathVariable("userId") int id)
	{	
		return userService.getUserById(id);
	}
	
	/*
	@PostMapping
	public UserEntity createUser(@RequestBody UserEntity userDetails) throws Exception
	{
		if(userDetails.getFirstName().isEmpty()) throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELDS);
		return userService.createUser(userDetails);
	}  */
	
	
	@PostMapping
	public UserResponseModel createUser(@RequestBody UserEntity userDetails) throws Exception
	{	
		if(userDetails.getFirstName().isEmpty()) throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELDS);
		UserDTO userDto = new UserDTO();
		BeanUtils.copyProperties(userDetails, userDto);
		UserDTO createdUser = userServiceImpl.createUser(userDto);
		UserResponseModel returnValue = new UserResponseModel();
		BeanUtils.copyProperties(createdUser, returnValue);		
		return returnValue;
	}
	
	@PutMapping
	public UserEntity updateUser(@RequestBody UserEntity updatedUser) {
		return userService.updateUser(updatedUser); 
	}
	
	@DeleteMapping("/{userId}")
	public String deleteUser(@PathVariable("userId") int id) throws Exception
	{
		if(id == 0) throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND);
		return userService.deleteUser(id);
	}

	@GetMapping
	public List<UserEntity> getUsers(@RequestParam(value= "page", defaultValue = "1") int page,
			@RequestParam(value= "limit", defaultValue = "25") int limit)
	{	
		return userService.getUsers(page,limit);
	}
}
