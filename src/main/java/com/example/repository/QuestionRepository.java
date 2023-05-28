package com.example.repository;

import com.example.model.Question;
import com.example.utils.JavaAPIExtractor;
import com.example.utils.JavaCodeExtractor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

  @Query(value = "SELECT q.question_id, " +
          "(SELECT COUNT(DISTINCT owner_account_id) FROM " +
          "(SELECT owner_account_id FROM answer WHERE question_id = q.question_id " +
          "UNION " +
          "SELECT owner_account_id FROM comment WHERE post_id = q.question_id) AS subquery) AS total_user_count, " +
          "COUNT(DISTINCT a.owner_account_id) AS answer_user_count, " +
          "COUNT(DISTINCT c.owner_account_id) AS comment_user_count, " +
          "(SELECT COUNT(DISTINCT owner_account_id) FROM " +
          "(SELECT owner_account_id FROM answer " +
          "UNION " +
          "SELECT owner_account_id FROM comment ) AS subquery) AS ccount "+
          "FROM question q " +
          "LEFT JOIN answer a ON q.question_id = a.question_id " +
          "LEFT JOIN comment c ON q.question_id = c.post_id " +
          "GROUP BY q.question_id", nativeQuery = true)
  List<Object[]> countUniqueUsersPerQuestion();


  @Query(value = "SELECT q.question_id,\n"
          + "       a.display_name AS most_active_answer_user,\n"
          + "       a.cnt AS user_answer_cnt,\n"
          + "       c.display_name AS most_active_comment_user,\n"
          + "       c.cnt AS user_comment_cnt,\n"
          + "       u.display_name AS most_active_user,\n"
          + "       COALESCE(u.total_cnt, 0) AS total_activity_cnt,\n"
          + "       w.answer_owner AS most_users_per_Q,\n"
          + "       w.activity AS activity_cnt\n"
          + "FROM question q\n"
          + "LEFT JOIN (\n"
          + "    SELECT a.question_id, u.display_name, COUNT(DISTINCT a.answer_id) AS cnt,\n"
          + "           ROW_NUMBER() OVER (PARTITION BY a.question_id ORDER BY COUNT(DISTINCT a.answer_id) DESC) AS rn\n"
          + "    FROM answer a\n"
          + "    JOIN user_ u ON u.account_id = a.owner_account_id\n"
          + "    GROUP BY a.question_id, u.display_name\n"
          + ") a ON a.question_id = q.question_id AND a.rn = 1\n"
          + "LEFT JOIN (\n"
          + "    SELECT c.post_id, u.display_name, COUNT(DISTINCT c.comment_id) AS cnt,\n"
          + "           ROW_NUMBER() OVER (PARTITION BY c.post_id ORDER BY COUNT(DISTINCT c.comment_id) DESC) AS rn\n"
          + "    FROM comment c\n"
          + "    JOIN user_ u ON u.account_id = c.owner_account_id\n"
          + "    GROUP BY c.post_id, u.display_name\n"
          + ") c ON c.post_id = q.question_id AND c.rn = 1\n"
          + "LEFT JOIN (\n"
          + "    SELECT question_id, answer_owner, answercnt, comment_owner, commentcnt, activity,\n"
          + "           ROW_NUMBER() OVER (PARTITION BY question_id ORDER BY activity DESC) AS rn\n"
          + "    FROM (\n"
          + "        SELECT question_id,\n"
          + "               answer_owner,\n"
          + "               SUM(answer_count) AS answercnt,\n"
          + "               comment_owner,\n"
          + "               SUM(COALESCE(comment_count, 0)) AS commentcnt,\n"
          + "               SUM((answer_count + COALESCE(comment_count, 0))) AS activity\n"
          + "        FROM (\n"
          + "            SELECT q.question_id,\n"
          + "                   x.display_name AS answer_owner,\n"
          + "                   x.cnt AS answer_count,\n"
          + "                   y.display_name AS comment_owner,\n"
          + "                   y.cnt AS comment_count\n"
          + "            FROM question q\n"
          + "            LEFT JOIN (\n"
          + "                SELECT a.question_id,\n"
          + "                       u.display_name,\n"
          + "                       COUNT(DISTINCT a.answer_id) AS cnt,\n"
          + "                       ROW_NUMBER() OVER (PARTITION BY a.question_id ORDER BY COUNT(DISTINCT a.answer_id) DESC) AS rn\n"
          + "                FROM answer a\n"
          + "                JOIN user_ u ON u.account_id = a.owner_account_id\n"
          + "                GROUP BY a.question_id, u.display_name\n"
          + "            ) x ON x.question_id = q.question_id\n"
          + "            LEFT JOIN (\n"
          + "                SELECT c.post_id,\n"
          + "                       u.display_name,\n"
          + "                       COUNT(DISTINCT c.comment_id) AS cnt,\n"
          + "                       ROW_NUMBER() OVER (PARTITION BY c.post_id ORDER BY COUNT(DISTINCT c.comment_id) DESC) AS rn\n"
          + "                FROM comment c\n"
          + "                JOIN user_ u ON u.account_id = c.owner_account_id\n"
          + "                GROUP BY c.post_id, u.display_name\n"
          + "            ) y ON y.post_id = q.question_id AND y.display_name = x.display_name\n"
          + "        ) qwq\n"
          + "        GROUP BY question_id, answer_owner, comment_owner\n"
          + "        ORDER BY question_id DESC, activity DESC\n"
          + "    ) main\n"
          + ") w ON w.question_id = q.question_id AND w.rn = 1\n"
          + "JOIN (\n"
          + "    SELECT u.account_id, u.display_name, COALESCE(SUM(DISTINCT a.answer_id), 0) + COALESCE(SUM(DISTINCT c.comment_id), 0) AS total_cnt,\n"
          + "           ROW_NUMBER() OVER (ORDER BY COALESCE(SUM(DISTINCT a.answer_id), 0) + COALESCE(SUM(DISTINCT c.comment_id), 0) DESC) AS rn\n"
          + "    FROM user_ u\n"
          + "    LEFT JOIN answer a ON u.account_id = a.owner_account_id\n"
          + "    LEFT JOIN comment c ON u.account_id = c.owner_account_id\n"
          + "    GROUP BY u.account_id, u.display_name\n"
          + ") u ON u.rn = 1;", nativeQuery = true)
  List<Object[]> findMostActiveUsersPerQuestion();


  @Query(value = "SELECT a.body FROM answer a UNION " +
      "SELECT q.body FROM question q UNION " +
      "SELECT c.body FROM comment c", nativeQuery = true)
  List<String> findAllBodies();

}
