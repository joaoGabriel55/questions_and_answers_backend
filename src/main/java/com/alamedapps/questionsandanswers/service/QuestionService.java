package com.alamedapps.questionsandanswers.service;

import java.io.Serializable;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.alamedapps.questionsandanswers.entity.Question;

@Component
public interface QuestionService extends GenericService<Question, Serializable> {

	public Page<Question> findPaginatedByUserId(final int id, final int page, final int size);

	Page<Question> findPaginated(int page, int size);
}
