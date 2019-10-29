package org.studyeasy.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.studyeasy.shared.dto.AddressDTO;

public interface AddressService extends UserDetailsService {
	List<AddressDTO> getAddresses(String userId);
	AddressDTO getAddress(String addressId);
}
