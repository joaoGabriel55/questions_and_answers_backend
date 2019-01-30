package com.alamedapps.questionsandanswers.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.alamedapps.questionsandanswers.entity.Question;

public interface QuestionRepository extends JpaRepository<Question, Integer>{
	
	@Query(
			  value = "select q.id, q.content, q.author_id, u.nome, u.id from questions q "
			  		+ "inner join users u on q.author_id = u.id where q.author_id = :id order by q.id desc", 
			  countQuery = "SELECT count(*) FROM questions q where q.author_id = :id", 
			  nativeQuery = true)
	Page<Question> findByQuestionsByUserId(@Param("id") Integer id, Pageable pageable);
	
	
	@Query(
			  value = "select q.id, q.content, q.author_id, u.nome, u.id from questions q "
			  		+ "inner join users u on q.author_id = u.id order by q.id desc", 
			  countQuery = "SELECT count(*) FROM questions", 
			  nativeQuery = true)
	Page<Question> findPaginated(Pageable pageable);
	
}
