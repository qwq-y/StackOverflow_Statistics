package com.example.resportory;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.model.Question;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;

@Mapper
public interface QuestionResportory extends BaseMapper<Question> {

}

//public interface QuestionResportory extends JpaRepository<Question, Long> {
//
//}