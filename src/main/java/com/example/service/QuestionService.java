package com.example.service;

import com.example.model.Question;
import com.example.repository.QuestionRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestionService {

  private final QuestionRepository questionRepository;

  @Autowired
  public QuestionService(QuestionRepository questionRepository) {
    this.questionRepository = questionRepository;
  }

  public List<Question> getQuestions() {
    return questionRepository.findAll();
  }

  public Long getNoAnswerQuestionCount() {
    return questionRepository.countByAnswerCountEquals(Long.valueOf(0));
  }

  public Long getMaxAnswerCount() {
    return questionRepository.findMaxAnswerCount();
  }

  public Double getAvgAnswerCount() {
    return questionRepository.findAvgAnswerCount();
  }

  public List<Object[]> getAnswerCountDistribution() {
    return questionRepository.findAnswerCountDistribution();
  }

  public Long getCountByAcceptedAnswerIdIsNotNull() {
    return questionRepository.countByAcceptedAnswerIdIsNotNull();
  }

  public List<Object[]> getQuestionResolutionTimeDistribution() {
    return questionRepository.findQuestionResolutionTimeDistribution();
  }

  public Long getCountQuestionsWithLowerAcceptedAnswerScore() {
    return questionRepository.countQuestionsWithLowerAcceptedAnswerScore();
  }

  public List<Object[]> getCountUniqueUsersPerQuestion() {
    return questionRepository.countUniqueUsersPerQuestion();
  }

  public List<Object[]> getMostActiveUsersPerQuestion() {
    return questionRepository.findMostActiveUsersPerQuestion();
  }

}
