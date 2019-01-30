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

import com.alamedapps.questionsandanswers.dto.AnswerUser;
import com.alamedapps.questionsandanswers.entity.Answer;
import com.alamedapps.questionsandanswers.entity.Question;
import com.alamedapps.questionsandanswers.entity.User;
import com.alamedapps.questionsandanswers.response.Response;
import com.alamedapps.questionsandanswers.service.AnswerService;
import com.alamedapps.questionsandanswers.service.QuestionService;
import com.alamedapps.questionsandanswers.service.UserService;

@RestController
@RequestMapping("/answers")
@CrossOrigin(origins = "*")
public class AnswerController {

	@Autowired
	private QuestionService questionService;

	@Autowired
	private UserService userService;

	@Autowired
	private AnswerService answerService;

	@GetMapping(params = { "page", "size", "questionId" })
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ResponseEntity<Response<List<AnswerUser>>> findPaginateQuestions(@RequestParam("page") int page,
			@RequestParam("size") int size, @RequestParam("questionId") int questionId) {

		Response<List<AnswerUser>> response = new Response<List<AnswerUser>>();

		List<AnswerUser> answerUsers = new ArrayList<>();

		Page<Answer> answers = null;
		if (questionId > 0)
			answers = answerService.findAnswersByQuestionId(questionId, page, size);
		else
			return ResponseEntity.badRequest().body(response);

		if (page > answers.getTotalPages()) {
			response.getErrors().add("Page not found");
			return ResponseEntity.badRequest().body(response);
		}

		for (Iterator<Answer> iterator = answers.getContent().iterator(); iterator.hasNext();) {
			Answer answer = (Answer) iterator.next();

			AnswerUser answerUser = new AnswerUser();
			answerUser.setAuthorId(answer.getAuthor().getId());
			answerUser.setAuthor(answer.getAuthor().getNome());
			answerUser.setAnswerId(answer.getId());
			answerUser.setAnswerContent(answer.getContent());
			answerUser.setQuestionId(answer.getQuestion().getId());

			answerUsers.add(answerUser);

			answerUser = new AnswerUser();
		}

		response.setData(answerUsers);

		return ResponseEntity.ok(response);
	}

	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ResponseEntity<Response<Answer>> findById(@PathVariable("id") int id) {
		Response<Answer> response = new Response<>();

		Optional<Answer> answer = answerService.findById(id);

		if (answer.get() == null) {
			response.getErrors().add("Question not found id: " + id);
			return ResponseEntity.badRequest().body(response);
		}

		response.setData(answer.get());
		return ResponseEntity.ok(response);
	}

	@PostMapping("/{idUser}/{idQuestion}")
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public ResponseEntity<Response<Answer>> createOrUpdate(@RequestBody Answer answer,
			@PathVariable("idUser") int idUser, @PathVariable("idQuestion") int idQuestion) {

		Response<Answer> response = new Response<>();

		Optional<User> author = userService.findById(idUser);
		Optional<Question> question = questionService.findById(idQuestion);

		if (author != null && question != null) {
			answer.setAuthor(author.get());
			answer.setQuestion(question.get());
			answerService.saveOrUpdate(answer);
			response.setData(answer);
			return ResponseEntity.ok().body(response);
		} else {
			response.getErrors().add("Author and Question not exists!");
			return ResponseEntity.badRequest().body(response);
		}
	}

	@DeleteMapping(value = "{id}")
	public ResponseEntity<Response<String>> delete(@PathVariable("id") int id) {

		Response<String> response = new Response<String>();
		Optional<Answer> answer = answerService.findById(id);

		if (answer.get() == null) {
			response.getErrors().add("Register not found, id: " + id);
			return ResponseEntity.badRequest().body(response);
		}

		answerService.delete(answer.get());
		return ResponseEntity.ok(new Response<String>());

	}
}
