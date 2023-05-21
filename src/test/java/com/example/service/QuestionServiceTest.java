package com.example.service;

import com.example.model.Question;
import com.example.repository.QuestionRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class QuestionServiceTest {

  @Autowired
  private QuestionService questionService;

  @Autowired
  private QuestionRepository questionRepository;

  @Test
  void testGetQuestions() {
    List<Question> questions = questionService.getQuestions();
    assert (questions.size() == 600);
  }

  @Test
  void testCountNoAnswerQuestions() {
    long cnt = questionService.getNoAnswerQuestionCount();
    System.out.println("# no answer questions ===========> " + cnt);
  }

  @Test
  void testGetMaxAnswerCount() {
    long cnt = questionService.getMaxAnswerCount();
    System.out.println("# max answer count ===========> " + cnt);
  }

}
