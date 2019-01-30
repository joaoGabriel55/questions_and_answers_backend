package com.alamedapps.questionsandanswers.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.alamedapps.questionsandanswers.entity.Question;
import com.alamedapps.questionsandanswers.repository.QuestionRepository;
import com.alamedapps.questionsandanswers.service.QuestionService;

@Service
public class QuestionServiceImpl implements QuestionService {

	@Autowired
	private QuestionRepository questionRepository;

	@Override
	public void saveOrUpdate(Question object) {
		this.questionRepository.save(object);
	}

	@Override
	public void delete(Question object) {
		this.questionRepository.delete(object);
	}

	@Override
	public Optional<Question> findById(int id) {
		return this.questionRepository.findById(id);
	}

	@Override
	public List<Question> findAll() {
		return this.questionRepository.findAll();
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public Page<Question> findPaginated(int page, int size) {
		return questionRepository.findPaginated(new PageRequest(page, size));
	}

	@SuppressWarnings("deprecation")
	@Override
	public Page<Question> findPaginatedByUserId(int id, int page, int size) {
		return questionRepository.findByQuestionsByUserId(id, new PageRequest(page, size));
	}

}
