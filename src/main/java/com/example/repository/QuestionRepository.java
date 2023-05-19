package com.example.repository;

import com.example.model.Question;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface QuestionRepository extends JpaRepository<Question, Long> {

  long countByAnswerCountEquals(Long answerCount);

  @Query(value = "SELECT q.answer_count, COUNT(*) AS count " +
      "FROM question q " +
      "GROUP BY q.answer_count " +
      "ORDER BY q.answer_count", nativeQuery = true)
  List<Object[]> findAnswerCountDistribution();

  @Query(value = "SELECT MAX(answer_count) FROM question", nativeQuery = true)
  long findMaxAnswerCount();

  @Query(value = "SELECT AVG(answer_count) FROM question", nativeQuery = true)
  double findAvgAnswerCount();

  long countByAcceptedAnswerIdIsNotNull();

  @Query(value = "SELECT a.creation_date - q.creation_date AS duration, COUNT(*) AS count " +
      "FROM question q JOIN answer a ON q.accepted_answer_id = a.answer_id " +
      "GROUP BY duration " +
      "ORDER BY duration", nativeQuery = true)
  List<Object[]> findQuestionResolutionTimeDistribution();

  @Query(value = "SELECT COUNT(DISTINCT q.question_id) " +
      "FROM question q " +
      "JOIN answer a ON q.accepted_answer_id = a.answer_id " +
      "WHERE a.score < (SELECT MAX(a2.score) FROM answer a2 WHERE a2.question_id = q.question_id)",
      nativeQuery = true)
  long countQuestionsWithLowerAcceptedAnswerScore();

}
