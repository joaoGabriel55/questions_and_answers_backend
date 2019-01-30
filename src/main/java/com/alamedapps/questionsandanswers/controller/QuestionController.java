package com.alamedapps.questionsandanswers.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.alamedapps.questionsandanswers.dto.QuestionUser;
import com.alamedapps.questionsandanswers.entity.Question;
import com.alamedapps.questionsandanswers.entity.User;
import com.alamedapps.questionsandanswers.response.Response;
import com.alamedapps.questionsandanswers.service.QuestionService;
import com.alamedapps.questionsandanswers.service.UserService;

@RestController
@RequestMapping("/questions")
@CrossOrigin(origins = "*")
public class QuestionController {

	@Autowired
	private QuestionService questionService;

	@Autowired
	private UserService userService;

	@GetMapping(params = { "page", "size", "userId" })
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ResponseEntity<Response<List<QuestionUser>>> findPaginateQuestions(@RequestParam("page") int page,
			@RequestParam("size") int size, @RequestParam("userId") int userId) {
		
		Response<List<QuestionUser>> response = new Response<List<QuestionUser>>();

		List<QuestionUser> questionUserList = new ArrayList<>();

		Page<Question> questions = null;
		if (userId > 0)
			questions = questionService.findPaginatedByUserId(userId, page, size);
		else
			questions = questionService.findPaginated(page, size);

		if (page > questions.getTotalPages()) {
			response.getErrors().add("Page not found");
			return ResponseEntity.badRequest().body(response);
		}

		for (Iterator<Question> iterator = questions.getContent().iterator(); iterator.hasNext();) {
			Question question = (Question) iterator.next();

			QuestionUser questionUser = new QuestionUser();
			questionUser.setUserId(question.getAuthor().getId());
			questionUser.setUserName(question.getAuthor().getNome());
			questionUser.setQuestionId(question.getId());
			questionUser.setQuestionContent(question.getContent());

			questionUserList.add(questionUser);

			questionUser = new QuestionUser();
		}

		response.setData(questionUserList);

		return ResponseEntity.ok(response);
	}

	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ResponseEntity<Response<Question>> findById(@PathVariable("id") int id) {
		Response<Question> response = new Response<>();

		Optional<Question> question = questionService.findById(id);

		if (question.get() == null) {
			response.getErrors().add("Question not found id: " + id);
			return ResponseEntity.badRequest().body(response);
		}

		response.setData(question.get());
		return ResponseEntity.ok(response);
	}

	@PostMapping("/{idUser}")
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public ResponseEntity<Response<Question>> createOrUpdate(@RequestBody Question question,
			@PathVariable("idUser") int idUser) {

		Response<Question> response = new Response<>();

		Optional<User> author = userService.findById(idUser);
		if (author != null) {
			question.setAuthor(author.get());
			questionService.saveOrUpdate(question);
			response.setData(question);
			return ResponseEntity.ok().body(response);
		} else {
			response.getErrors().add("Author not exists!");
			return ResponseEntity.badRequest().body(response);
		}
	}

	@DeleteMapping(value = "{id}")
	public ResponseEntity<Response<String>> delete(@PathVariable("id") int id) {

		Response<String> response = new Response<String>();
		Optional<Question> question = questionService.findById(id);

		if (question.get() == null) {
			response.getErrors().add("Register not found, id: " + id);
			return ResponseEntity.badRequest().body(response);
		}

		questionService.delete(question.get());
		return ResponseEntity.ok(new Response<String>());

	}

}
