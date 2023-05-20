package com.example.service;

import com.example.model.QuestionTag;
import com.example.repository.QuestionTagRepository;
import java.util.ArrayList;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionTagService {

    private final QuestionTagRepository questionTagRepository;

    @Autowired
    public QuestionTagService(QuestionTagRepository questionTagRepository) {
        this.questionTagRepository = questionTagRepository;
    }

    public List<QuestionTag> getQuestionTags() {
        return questionTagRepository.findAll();
    }

    public List<String> getTags() {
        return questionTagRepository.findAllTags();
    }

    public List<Object[]> getTagWithJavaFrequency() {
        return questionTagRepository.findTagWithJavaFrequency();
    }

    public List<Object[]> getTagCombinationScores() {
        return questionTagRepository.findTagCombinationScores();
    }

    public List<Object[]> getTagCombinationViews() {
        return questionTagRepository.findTagCombinationViews();
    }
}
