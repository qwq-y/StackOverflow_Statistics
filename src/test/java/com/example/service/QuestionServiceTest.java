package com.example.service;

import com.example.model.Question;
import com.example.resportory.QuestionResportory;
import java.util.List;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

@SpringBootTest
public class QuestionServiceTest {

  @Autowired
  private QuestionService questionService;

  @Autowired
  private QuestionResportory questionResportory;

  @Test
  void testGetQuestions() {
    System.out.println("-------------------------------------------------");
    List<Question> questions = questionService.getQuestions();
    for (Question question : questions) {
      System.out.println(question.getQuestionId());
    }
    System.out.println("-------------------------------------------------");
    System.out.println(questions.size());
    assert (questions.size() == 600);
//    assertThat(questions.size()).isEqualTo(2);
  }

}
