package org.studyeasy.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;
import org.modelmapper.convention.NamingConventions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.studyeasy.entity.UserEntity;
import org.studyeasy.exception.NonUniqueException;
import org.studyeasy.exception.UserServiceException;
import org.studyeasy.response.model.ErrorMessages;
import org.studyeasy.service.UserService;
import org.studyeasy.shared.dto.AddressDTO;
import org.studyeasy.shared.dto.UserDTO;
import org.studyeasy.shared.io.Utils;
import org.studyeasy.shared.io.repositories.UserRepository;


@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepo;
	
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
		
	
	public UserDTO createUser(UserDTO user) {
		ModelMapper modelMapper = new ModelMapper();
		if(userRepo.findByEmail(user.getEmail()) != null)throw new NonUniqueException(ErrorMessages.EMAIL_ALREADY_EXISTS.getErrorMessage());	
		for (int i = 0; i <user.getAddresses().size(); i++) {
			AddressDTO address = user.getAddresses().get(i);
			address.setUserDetails(user);
			address.setAddressId(new Utils().generateAddressId(30));
			user.getAddresses().set(i, address);
		}
		UserEntity userEntity = modelMapper.map(user, UserEntity.class);
		userEntity.setEmailVerificationStatus(false);
		userEntity.setUserId(new Utils().generateUserId(30));
		userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		UserEntity storedUserDetails = userRepo.save(userEntity);
		UserDTO returnValue = modelMapper.map(storedUserDetails, UserDTO.class);
		return returnValue;
		
	}

	@Override
	public List<UserDTO> getUsers() {
		ModelMapper modelMapper = new ModelMapper();
		List<UserEntity> users = new ArrayList();				
		List<UserDTO> returnValue = new ArrayList<>();	
		userRepo.findAll().forEach(item -> users.add(item));
		for(UserEntity userEntities : users)
		{
			UserDTO userDTOs =	modelMapper.map(users, UserDTO.class);
			returnValue.add(userDTOs);
		}
		return returnValue;
	}

	@Override
	public UserDTO getUserById(String userId) {
		ModelMapper modelMapper = new ModelMapper();
		UserEntity userEntity = userRepo.findByUserId(userId);
		if(userEntity == null)throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()); 
		return modelMapper.map(userEntity, UserDTO.class);
	}

	@Override
	public String deleteUser(String userId) {
		UserEntity userEntity = userRepo.findByUserId(userId);
		if(userEntity == null)throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()); 
		userRepo.deleteById(userEntity.getId());
		return "User has been deleted successfully.";
	}

	@Override
	public UserDTO updateUser(String userId, UserDTO updatedUser) {
		ModelMapper modelMapper = new ModelMapper();
		UserEntity userEntity = userRepo.findByUserId(userId);
		if(userEntity == null)throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()); 
		userEntity.setFirstName(updatedUser.getFirstName());
		userEntity.setLastName(updatedUser.getLastName());
		userEntity.setEmail(updatedUser.getEmail());
		UserEntity returnEntity = userRepo.save(userEntity);
		return modelMapper.map(returnEntity, UserDTO.class);
	}

	@Override
	public List<UserDTO> getUsers(int page, int limit) {
		List<UserDTO> returnValue = new ArrayList<>();
		Pageable pageableRequest = PageRequest.of(page-1, limit);
		List<UserEntity> users = userRepo.findAll(pageableRequest).getContent();
		for(UserEntity userEntities : users)
		{
			UserDTO userDTOs =	new UserDTO();
			BeanUtils.copyProperties(userEntities, userDTOs);
			returnValue.add(userDTOs);
		}
		return returnValue;
	}
	
	
	@Override
	public UserDTO getUser(String email) {
	//	ModelMapper modelMapper = new ModelMapper();
		UserEntity userEntity = userRepo.findByEmail(email);
		if(userEntity == null)throw new UsernameNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()); 
	//	modelMapper.getConfiguration().setFieldMatchingEnabled(true);
    // 	modelMapper.getConfiguration().setFieldAccessLevel(AccessLevel.PROTECTED);
	//	modelMapper.getConfiguration().setSourceNamingConvention(NamingConventions.JAVABEANS_MUTATOR);
		UserDTO userDto = new UserDTO();
		BeanUtils.copyProperties(userEntity, userDto);
	//	UserDTO  userDto =  modelMapper.map(userEntity, UserDTO.class);
		return userDto;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		UserEntity userEntity = userRepo.findByEmail(email);
		if(userEntity == null) throw new UsernameNotFoundException(email);
		return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
	}	

}
