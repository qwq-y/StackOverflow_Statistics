package com.example.controller;

import com.example.model.Question;
import com.example.service.QuestionService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * What percentage of questions don't have any answer?
 * What is the average and maximum number of answers?
 * What is the distribution of the number of answers?
 *
 * What percentage of questions have accepted answers (one question could only have one accepted answer)?
 * What is the distribution of question resolution time (i.e., the duration between the question posting
 * time and the posting time of the accepted answer)?
 * What percentage of questions have non-accepted answers (i.e., answers that are not marked as
 * accepted) that have received more upvotes than the accepted answers?
 *
 * Many users could participate in a thread discussion. What is the distribution of such participation (i.e.,
 * the number of distinct users who post the question, answers, or comments in a thread)?
 * Which are the most active users who frequently participate in thread discussions?
 */

@RestController
@RequestMapping("/questions")
public class QuestionController {

  private final QuestionService questionService;

  public QuestionController(QuestionService questionService) {
    this.questionService = questionService;
  }

  @GetMapping
  public List<Question> getQuestions() {
    return questionService.getQuestions();
  }

  @GetMapping("/noAnswerQuestionCount")
  public Long getNoAnswerQuestionCount() {
    return questionService.getNoAnswerQuestionCount();
  }

  @GetMapping("/maxAnswerCount")
  public Long getMaxAnswerCount() {
    return questionService.getMaxAnswerCount();
  }

  @GetMapping("/avgAnswerCount")
  public Double getAvgAnswerCount() {
    return questionService.getAvgAnswerCount();
  }

  @GetMapping("/answerCountDistribution")
  public List<Object[]> getAnswerCountDistribution() {
    return questionService.getAnswerCountDistribution();
  }

  @GetMapping("/countByAcceptedAnswerIdIsNotNull")
  public Long getCountByAcceptedAnswerIdIsNotNull() {
    return questionService.getCountByAcceptedAnswerIdIsNotNull();
  }

  @GetMapping("/questionResolutionTimeDistribution")
  public List<Object[]> getQuestionResolutionTimeDistribution() {
    return questionService.getQuestionResolutionTimeDistribution();
  }

  @GetMapping("/countQuestionsWithLowerAcceptedAnswerScore")
  public Long getCountQuestionsWithLowerAcceptedAnswerScore() {
    return questionService.getCountQuestionsWithLowerAcceptedAnswerScore();
  }

  @GetMapping("/countUniqueUsersPerQuestion")
  public List<Object[]> getCountUniqueUsersPerQuestion() {
    return questionService.getCountUniqueUsersPerQuestion();
  }

  @GetMapping("/mostActiveUsersPerQuestion")
  public List<Object[]> getMostActiveUsersPerQuestion() {
    return questionService.getMostActiveUsersPerQuestion();
  }

}
