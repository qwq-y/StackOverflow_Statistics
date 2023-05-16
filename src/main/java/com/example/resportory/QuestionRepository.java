package com.example.resportory;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.model.Question;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;

//@Mapper
//public interface QuestionResportory extends BaseMapper<Question> {
//
//}

public interface QuestionRepository extends JpaRepository<Question, Long> {

}
