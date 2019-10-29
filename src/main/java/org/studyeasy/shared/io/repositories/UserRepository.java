package org.studyeasy.shared.io.repositories;

import org.hamcrest.Matcher;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.studyeasy.entity.UserEntity;

@Repository
public interface UserRepository extends PagingAndSortingRepository<UserEntity, Integer> { 
	
	// UserEntity is entity class and Long is data type of id 
      UserEntity findByEmail(String email);
	  UserEntity findByUserId(String userId);
	
	
	// This is custom method for fetching the details using custom values.
	/*
	  @Query("SELECT a FROM Article a WHERE a.title=:title and a.category=:category"
	  ) List<Article> fetchArticles(@Param("title") String
	  title, @Param("category") String category);
	 */
	
	}
