package com.alamedapps.questionsandanswers.security.jwt;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.alamedapps.questionsandanswers.entity.User;
import com.alamedapps.questionsandanswers.enums.ProfileEnum;

public class JwtUserFactory {

	private JwtUserFactory() {
	}

	public static JwtUser create(User user) {
		return new JwtUser(user.getId(), user.getEmail(), user.getPassword(),
				mapToGrantedAuthorities(user.getProfile()));
	}

	private static List<GrantedAuthority> mapToGrantedAuthorities(ProfileEnum profile) {
		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(profile == null ? "ROLE_USER" : profile.toString()));
		return authorities;
	}

}
