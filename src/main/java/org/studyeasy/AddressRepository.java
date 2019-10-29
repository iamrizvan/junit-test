package org.studyeasy;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.studyeasy.entity.AddressEntity;
import org.studyeasy.entity.UserEntity;

@Repository
public interface AddressRepository extends CrudRepository<AddressEntity, Integer>{

	List<AddressEntity> findAllByUserDetails(UserEntity userEntity);
	AddressEntity findByAddressId(String addressId);
}
