package com.example.controller;

import com.example.model.Question;
import com.example.service.QuestionService;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * What percentage of questions don't have any answer?
 * What is the average and maximum number of answers?
 * What is the distribution of the number of answers?
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

  @GetMapping("/countByAnswerCount/{answerCount}")
  private Long getCountByAnswerCount(@PathVariable Long answerCount) {
    return questionService.getCountByAnswerCount(answerCount);
  }

  @GetMapping("/countByAnswerCountGreaterThan/{answerCount}")
  public Long getCountByAnswerCountGreaterThan(@PathVariable Long answerCount) {
    return questionService.getCountByAnswerCountGreaterThan(answerCount);
  }

  @GetMapping("/countByAnswerCountLessThan/{answerCount}")
  public Long getCountByAnswerCountLessThan(@PathVariable Long answerCount) {
    return questionService.getCountByAnswerCountLessThan(answerCount);
  }

  @GetMapping("/countByAnswerCountBetween/{min}/{max}")
  public Long getCountByAnswerCountBetween(@PathVariable Long min, @PathVariable Long max) {
    return questionService.getCountByAnswerCountBetween(min, max);
  }

}
