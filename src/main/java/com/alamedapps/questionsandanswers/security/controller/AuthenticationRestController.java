package com.alamedapps.questionsandanswers.security.controller;

import javax.naming.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.alamedapps.questionsandanswers.controller.UserController;
import com.alamedapps.questionsandanswers.entity.User;
import com.alamedapps.questionsandanswers.response.Response;
import com.alamedapps.questionsandanswers.security.jwt.JwtAuthenticationRequest;
import com.alamedapps.questionsandanswers.security.jwt.JwtTokenUtil;
import com.alamedapps.questionsandanswers.security.model.CurrentUser;
import com.alamedapps.questionsandanswers.service.UserService;

@RestController
@CrossOrigin(origins = "*")
public class AuthenticationRestController {

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private UserService userService;
	
	@Autowired
	private UserController userController;

	@PostMapping(value = "/auth")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ResponseEntity<?> createAuthentication(@RequestBody JwtAuthenticationRequest authenticationRequest)
			throws Exception {
		
		Response<User> response = new Response<User>();
		
		final Authentication auth = authenticate(authenticationRequest.getEmail(), authenticationRequest.getPassword());
		SecurityContextHolder.getContext().setAuthentication(auth);
		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());
		final String token = jwtTokenUtil.generateToken(userDetails);
		final User user = userService.findByEmail(authenticationRequest.getEmail());
		
		
		if(user == null) {
			response.getErrors().add("Email not registred");
			return ResponseEntity.badRequest().body(response);
		}
		
		if(!userController.getEncoder().matches(authenticationRequest.getPassword(), user.getPassword())) {
			response.getErrors().add("Password is incorrect.");
			return ResponseEntity.badRequest().body(response);
		}
		
		user.setPassword(null);
		return ResponseEntity.ok(new CurrentUser(token, user));

	}

	@PostMapping(value = "/signup")
	public ResponseEntity<Response<User>> createOrUpdate(@RequestBody User user, BindingResult result) {
		return userController.createOrUpdateUser(user, result, userService);
	}

	@PostMapping(value = "/refresh/{token}")
	public ResponseEntity<?> refreshAndGetAuthenticationToken(@PathVariable("token") String token) {
		String username = jwtTokenUtil.getUsernameFromToken(token);
		final User user = userService.findByEmail(username);

		if (jwtTokenUtil.canTokenBeRefreshed(token)) {
			String refreshedToken = jwtTokenUtil.refreshToken(token);
			return ResponseEntity.ok(new CurrentUser(refreshedToken, user));
		} else {
			return ResponseEntity.badRequest().body(null);
		}
	}

	public Authentication authenticate(String email, String password) throws AuthenticationException {
		return new UsernamePasswordAuthenticationToken(email, password);
	}

}
