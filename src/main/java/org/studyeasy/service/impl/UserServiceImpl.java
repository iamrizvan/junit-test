package org.studyeasy.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.studyeasy.UserRepository;
import org.studyeasy.entity.ErrorMessages;
import org.studyeasy.entity.UserEntity;
import org.studyeasy.exception.NonUniqueException;
import org.studyeasy.service.UserService;
import org.studyeasy.shared.dto.UserDTO;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;
		
	@Override
	public UserDTO createUser(UserDTO user) {
		if(userRepository.findByEmail(user.getEmail()) != null)throw new NonUniqueException(ErrorMessages.EMAIL_ALREADY_EXISTS);
		UserEntity  userEntity = new UserEntity();
		BeanUtils.copyProperties(user, userEntity);	
		UserEntity storedUserDetails = userRepository.save(userEntity);
		UserDTO returnValue = new UserDTO();
		BeanUtils.copyProperties(storedUserDetails, returnValue);
		return returnValue;
	}
	
	

}
