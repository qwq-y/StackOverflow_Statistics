package com.example.controller;

import com.example.model.QuestionTag;
import java.util.ArrayList;
import java.util.List;
import com.example.service.QuestionTagService;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Which tags frequently appear together with the java tag?
 * Which tags or tag combinations receive the most upvotes?
 * Which tags or tag combinations receive the most views?
 */

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

    @GetMapping("/tags")
    public List<String> getTags() {
        return questionTagService.getTags();
    }

    @GetMapping("/tagWithJavaFrequency")
    public List<Object[]> getMostFrequentTagWithJava() {
        return questionTagService.getTagWithJavaFrequency();
    }

    @GetMapping("/tagCombinationScores")
    public List<Object[]> getTagCombinationScores() {
        return questionTagService.getTagCombinationScores();
    }

    @GetMapping("/tagCombinationViews")
    public List<Object[]> getTagCombinationViews() {
        return questionTagService.getTagCombinationViews();
    }

}
