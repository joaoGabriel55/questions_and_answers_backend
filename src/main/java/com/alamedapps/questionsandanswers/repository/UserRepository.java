package com.alamedapps.questionsandanswers.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.alamedapps.questionsandanswers.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	@Query(value = "SELECT * FROM users u where u.email = :email", nativeQuery = true)
	User findByEmail(@Param("email") String email);

//	@Query(value = "SELECT * FROM users u where u.id = :id", nativeQuery = true)
//	User findByUserId(@Param("id") int id);



}
