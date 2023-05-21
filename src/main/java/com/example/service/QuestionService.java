package com.example.service;

import com.example.model.Question;
import com.example.repository.QuestionRepository;
import com.example.utils.CodeSnippetExtractor;
import com.example.utils.JavaAPIExtractor;
import com.example.utils.JavaCodeExtractor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

  public List<Object[]> getJavaAPIFrequency() {
    // 获取question, answer, comment里所有的body
    List<String> jsonBodies = questionRepository.findAllBodies();

    // 获取body里的Java代码块
    CodeSnippetExtractor codeSnippetExtractor = new CodeSnippetExtractor();
    List<String> javaCodeSnippets = codeSnippetExtractor.extractJavaCodeSnippets(jsonBodies);

    // 获取代码块中的API
    JavaAPIExtractor javaAPIExtractor = new JavaAPIExtractor();
    List<String> javaAPIs = javaAPIExtractor.extractJavaAPIs(javaCodeSnippets);

    // 统计 API 出现次数
    Map<String, Integer> apiFrequencyMap = new HashMap<>();
    for (String api : javaAPIs) {
      apiFrequencyMap.put(api, apiFrequencyMap.getOrDefault(api, 0) + 1);
    }

    // 转换为 List<Object[]>，包含 API 和出现次数
    List<Object[]> apiFrequencyList = new ArrayList<>();
    for (Map.Entry<String, Integer> entry : apiFrequencyMap.entrySet()) {
      String api = entry.getKey();
      int frequency = entry.getValue();
      Object[] apiFrequency = {api, frequency};
      apiFrequencyList.add(apiFrequency);
    }

    // 按照出现次数降序排序
    apiFrequencyList.sort((a, b) -> ((Integer) b[1]).compareTo((Integer) a[1]));

    return apiFrequencyList;
  }

}
