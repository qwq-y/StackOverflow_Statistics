package com.example.controller;

import com.example.model.QuestionTag;
import com.example.service.QuestionService;
import java.util.List;
import java.util.Map;

import com.example.service.QuestionTagService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/questionTags")
public class QuestionTagController {

    private final QuestionTagService questionTagService;

    public QuestionTagController(QuestionTagService questionTagService) {
        this.questionTagService = questionTagService;
    }

    @GetMapping
    public List<QuestionTag> getQuestionTags() {
        return questionTagService.getQuestionTags();
    }

}
