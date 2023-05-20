package com.example.repository;

import com.example.model.QuestionTag;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface QuestionTagRepository extends JpaRepository<QuestionTag, Long> {

  @Query(value = "SELECT DISTINCT tag FROM question_tag", nativeQuery = true)
  List<String> findAllTags();

  @Query(value = "SELECT qt.tag, COUNT(*) AS count " +
      "FROM question_tag qt " +
      "WHERE qt.question_id IN (SELECT q.question_id FROM question q, question_tag qt2 " +
      "WHERE q.question_id = qt2.question_id AND qt2.tag = 'java') " +
      "AND qt.tag != 'java' " +
      "GROUP BY qt.tag " +
      "ORDER BY count DESC", nativeQuery = true)
  List<Object[]> findTagWithJavaFrequency();

  @Query(value = "SELECT CONCAT(qt1.tag, ',', qt2.tag) AS tag_combination, SUM(q.score) AS total_score " +
      "FROM question_tag qt1 " +
      "JOIN question_tag qt2 ON qt1.question_id = qt2.question_id " +
      "JOIN question q ON qt1.question_id = q.question_id " +
      "WHERE qt1.tag < qt2.tag " +
      "GROUP BY tag_combination " +
      "ORDER BY total_score DESC", nativeQuery = true)
  List<Object[]> findTagCombinationScores();

  @Query(value = "SELECT CONCAT(qt1.tag, ',', qt2.tag) AS tag_combination, SUM(q.view_count) AS total_view " +
          "FROM question_tag qt1 " +
          "JOIN question_tag qt2 ON qt1.question_id = qt2.question_id " +
          "JOIN question q ON qt1.question_id = q.question_id " +
          "WHERE qt1.tag < qt2.tag " +
          "GROUP BY tag_combination " +
          "ORDER BY total_view DESC", nativeQuery = true)
  List<Object[]> findTagCombinationViews();

}
