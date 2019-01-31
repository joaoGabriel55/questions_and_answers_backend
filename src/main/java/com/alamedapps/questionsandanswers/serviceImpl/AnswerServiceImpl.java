package com.alamedapps.questionsandanswers.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.alamedapps.questionsandanswers.entity.Answer;
import com.alamedapps.questionsandanswers.repository.AnswerRepository;
import com.alamedapps.questionsandanswers.service.AnswerService;

@Service
public class AnswerServiceImpl implements AnswerService {

	@Autowired
	private AnswerRepository answerRepository;

	@Override
	public void saveOrUpdate(Answer object) {
		this.answerRepository.save(object);
	}

	@Override
	public void delete(Answer object) {
		this.answerRepository.delete(object);
	}

	@Override
	public Optional<Answer> findById(int id) {
		return this.answerRepository.findById(id);
	}

	@Override
	public List<Answer> findAll() {
		return this.answerRepository.findAll();
	}

	@SuppressWarnings("deprecation")
	@Override
	public Page<Answer> findAnswersByQuestionId(int id, int page, int size) {
		return this.answerRepository.findAnswersByQuestionId(id, new PageRequest(page, size));
	}

	@Override
	public List<Answer> findAllAnswersByQuestionId(Integer id) {
		return this.answerRepository.findAllAnswersByQuestionId(id);
	}


}
