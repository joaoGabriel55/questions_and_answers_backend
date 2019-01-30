package com.alamedapps.questionsandanswers.service;

import java.io.Serializable;

import org.springframework.stereotype.Component;

import com.alamedapps.questionsandanswers.entity.User;

@Component
public interface UserService extends GenericService<User, Serializable> {
	
	User findByEmail(String email);
}
