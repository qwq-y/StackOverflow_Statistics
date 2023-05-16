package com.example.resportory;

import com.example.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;



public interface QuestionRepository extends JpaRepository<Question, Long> {

  long countByAnswerCountEquals(Long answerCount);

  long countByAnswerCountGreaterThan(Long answerCount);

  long countByAnswerCountLessThan(Long answerCount);

  long countByAnswerCountBetween(Long min, Long max);

  @Query(value = "SELECT MAX(answer_count) FROM question", nativeQuery = true)
  long findMaxAnswerCount();

  @Query(value = "SELECT AVG(answer_count) FROM question", nativeQuery = true)
  double findAvgAnswerCount();

}
