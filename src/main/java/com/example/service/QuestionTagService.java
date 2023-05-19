package com.example.service;

import com.example.model.QuestionTag;
import com.example.repository.QuestionTagRepository;
import java.util.ArrayList;
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

//    public List<List<String>> getAllTagCombinations() {
//        List<String> allTags = questionTagRepository.findAllTags();
//        return generateTagCombinations(allTags);
//    }
//
//    public Long getTagCombinationScore(List<String> tags) {
//        return questionTagRepository.getTagCombinationScore(tags);
//    }
//
//    // 生成所有可能的标签组合
//    private List<List<String>> generateTagCombinations(List<String> tags) {
//        List<List<String>> combinations = new ArrayList<>();
//        for (int i = 1; i <= tags.size(); i++) {
//            generateCombinationsHelper(tags, i, 0, new ArrayList<>(), combinations);
//        }
//        return combinations;
//    }
//
//    // 递归生成组合的辅助函数
//    private void generateCombinationsHelper(List<String> tags, int k, int start, List<String> currentCombination, List<List<String>> combinations) {
//        if (k == 0) {
//            combinations.add(new ArrayList<>(currentCombination));
//            return;
//        }
//        for (int i = start; i < tags.size(); i++) {
//            currentCombination.add(tags.get(i));
//            generateCombinationsHelper(tags, k - 1, i + 1, currentCombination, combinations);
//            currentCombination.remove(currentCombination.size() - 1);
//        }
//    }

    public List<Object[]> getTagCombinationScores() {
        return questionTagRepository.findTagCombinationScores();
    }
}
