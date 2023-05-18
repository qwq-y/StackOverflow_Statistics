package com.example.service;

import com.example.model.QuestionTag;
import com.example.resportory.QuestionTagRepository;
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
}
