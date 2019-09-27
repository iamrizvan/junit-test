package org.studyeasy.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.studyeasy.UserRepository;
import org.studyeasy.entity.UserEntity;

@Controller
public class UserServiceImplementation {

	@Autowired
	UserRepository userRepo;

	public UserEntity createUser(UserEntity userDetails) {
		return userRepo.save(userDetails);
	}

	public List<UserEntity> getUsers() {
		List<UserEntity> users = new ArrayList();
		userRepo.findAll().forEach(item -> users.add(item));
		return users;
	}

	public UserEntity getUserById(int id) {
		return userRepo.findById(id).get();
	}

	public String deleteUser(int id) {
		userRepo.deleteById(id);
		return "User has been deleted successfully.";
	}

	public UserEntity updateUser(UserEntity updatedUser) {
		return userRepo.save(updatedUser);
	}

	public List<UserEntity> getUsers(int page, int limit) {
		Pageable pageableRequest = PageRequest.of(page-1, limit);
		return userRepo.findAll(pageableRequest).getContent();
	}

	

}
