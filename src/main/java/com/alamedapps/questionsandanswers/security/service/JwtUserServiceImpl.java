package com.alamedapps.questionsandanswers.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.alamedapps.questionsandanswers.entity.User;
import com.alamedapps.questionsandanswers.security.jwt.JwtUserFactory;
import com.alamedapps.questionsandanswers.service.UserService;

@Service
public class JwtUserServiceImpl implements UserDetailsService{
	
	@Autowired
	private UserService userservice;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userservice.findByEmail(email);
		
		if(user == null)
			throw new UsernameNotFoundException(String.format("No user found with username '%s'.", email));
		else
			return JwtUserFactory.create(user);
	}
	
	
	
}
