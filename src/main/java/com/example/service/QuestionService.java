package com.example.service;

import com.example.model.Question;
import com.example.resportory.QuestionRepository;
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

  public Long getCountByAnswerCount(Long answerCount) {
    return questionRepository.countByAnswerCountEquals(answerCount);
  }

  public Long getCountByAnswerCountGreaterThan(Long answerCount) {
    return questionRepository.countByAnswerCountGreaterThan(answerCount);
  }

  public Long getCountByAnswerCountLessThan(Long answerCount) {
    return questionRepository.countByAnswerCountLessThan(answerCount);
  }

  public Long getCountByAnswerCountBetween(Long min, Long max) {
    return questionRepository.countByAnswerCountBetween(min, max);
  }

  public Long getMaxAnswerCount() {
    return questionRepository.findMaxAnswerCount();
  }

  public Double getAvgAnswerCount() {
    return questionRepository.findAvgAnswerCount();
  }

}
