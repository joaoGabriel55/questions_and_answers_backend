package com.alamedapps.questionsandanswers.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.alamedapps.questionsandanswers.entity.User;
import com.alamedapps.questionsandanswers.response.Response;
import com.alamedapps.questionsandanswers.service.UserService;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")
public class UserController {

	@Autowired
	private UserService userService;

	@Bean
	public PasswordEncoder getEncoder() {
		return new BCryptPasswordEncoder();
	}

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@PreAuthorize("hasAnyRole('ADMIN')")
	public List<User> findAll() {
		List<User> users = new ArrayList<User>();
		users = userService.findAll();
		return users;
	}

	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Response<User>> findById(@PathVariable("id") int id) {
		Response<User> response = new Response<>();

		Optional<User> user = userService.findById(id);

		if (user.get() == null) {
			response.getErrors().add("User not found id: " + id);
			return ResponseEntity.badRequest().body(response);
		}

		response.setData(user.get());
		return ResponseEntity.ok(response);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Response<User>> createOrUpdate(@RequestBody User user, BindingResult result) {
		return createOrUpdateUser(user, result, userService);
	}

	public ResponseEntity<Response<User>> createOrUpdateUser(User user, BindingResult result, UserService userService) {
		Response<User> response = new Response<User>();

		try {
			validateUser(user, result);
			if (result.hasErrors()) {
				result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
				return ResponseEntity.badRequest().body(response);
			}

			user.setPassword(getEncoder().encode(user.getPassword()));

			User userPersisted = user;
			userService.saveOrUpdate(userPersisted);

			response.setData(userPersisted);

		} catch (DuplicateKeyException dE) {
			response.getErrors().add("Email already registred");
			return ResponseEntity.badRequest().body(response);
		} catch (Exception e) {
			response.getErrors().add(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}

		return ResponseEntity.ok(response);
	}

	public void validateUser(User user, BindingResult result) {
		if (user.getEmail() == null || user.getEmail().trim() == "")
			result.addError(new ObjectError("User", "Email no information"));

		if (user.getNome() == null || user.getNome().trim() == "")
			result.addError(new ObjectError("User", "Name no information"));

		if (user.getPassword() == null || user.getPassword().trim() == "")
			result.addError(new ObjectError("User", "Password no information"));
	}

	@DeleteMapping(value = "{id}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Response<String>> delete(@PathVariable("id") int id) {

		Response<String> response = new Response<String>();
		Optional<User> user = userService.findById(id);

		if (user.get() == null) {
			response.getErrors().add("Register not found, id: " + id);
			return ResponseEntity.badRequest().body(response);
		}

		userService.delete(user.get());
		return ResponseEntity.ok(new Response<String>());

	}

}
