package com.alamedapps.questionsandanswers.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.alamedapps.questionsandanswers.entity.Answer;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {

	@Query(value = "select a.id, a.content, u.id as author_id, u.nome as author, a.question_id from answers a "
			+ "inner join questions q on a.question_id = q.id inner join users u on a.author_id = u.id "
			+ "where a.question_id = :id", nativeQuery = true)
	Page<Answer> findAnswersByQuestionId(@Param("id") Integer id, Pageable pageable);

	@Query(value = "select a.id, a.content, u.id as author_id, u.nome as author, a.question_id from answers a "
			+ "inner join questions q on a.question_id = q.id inner join users u on a.author_id = u.id "
			+ "where a.question_id = :id", nativeQuery = true)
	List<Answer> findAllAnswersByQuestionId(@Param("id") Integer id);

}
