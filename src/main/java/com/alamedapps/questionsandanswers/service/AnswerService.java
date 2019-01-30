package com.alamedapps.questionsandanswers.service;

import java.io.Serializable;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.alamedapps.questionsandanswers.entity.Answer;

@Component
public interface AnswerService extends GenericService<Answer, Serializable>{
	Page<Answer> findAnswersByQuestionId(final int id, final int page, final int size);
}
