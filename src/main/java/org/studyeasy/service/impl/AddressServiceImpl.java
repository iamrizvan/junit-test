package org.studyeasy.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.studyeasy.AddressRepository;
import org.studyeasy.entity.AddressEntity;
import org.studyeasy.entity.UserEntity;
import org.studyeasy.service.AddressService;
import org.studyeasy.shared.dto.AddressDTO;
import org.studyeasy.shared.io.repositories.UserRepository;

@Service
public class AddressServiceImpl implements AddressService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	AddressRepository addressRepository;
	
	

	@Override
	public List<AddressDTO> getAddresses(String userId) {
		List<AddressDTO> returnValue = new ArrayList();
		ModelMapper modelMapper = new ModelMapper();

		UserEntity userEntity = userRepository.findByUserId(userId);
		if (userEntity == null)
			return returnValue;

		Iterable<AddressEntity> addresses = addressRepository.findAllByUserDetails(userEntity);
		for (AddressEntity addressEntity : addresses) {
			returnValue.add(modelMapper.map(addressEntity, AddressDTO.class));
		}
		return returnValue;
	}

	@Override
	public AddressDTO getAddress(String addressId) {
		AddressDTO addressDTO = null;
		ModelMapper modelMapper = new ModelMapper();
		AddressEntity addressEntity = addressRepository.findByAddressId(addressId);
		if (addressEntity != null) {
			return modelMapper.map(addressEntity, AddressDTO.class);
		}
		return addressDTO;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return null;
	}

}
