package org.studyeasy.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.studyeasy.shared.dto.UserDTO;

public interface UserService extends UserDetailsService {
	UserDTO createUser(UserDTO user);
	List<UserDTO> getUsers();
	UserDTO getUserById(String userId);
	String deleteUser(String userId);
	UserDTO updateUser(String userId,UserDTO updatedUser);
	List<UserDTO> getUsers(int page, int limit);
	UserDTO getUser(String email);
	
}
