package com.example.service;

import com.example.model.Question;
import com.example.resportory.QuestionResportory;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestionService {

  private final QuestionResportory questionResportory;

  @Autowired
  private QuestionService(QuestionResportory questionResportory) {
    this.questionResportory = questionResportory;
  }

  public List<Question> getQuestions() {
    return questionResportory.findAll();
  }
}
