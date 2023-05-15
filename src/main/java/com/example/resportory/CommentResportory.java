package com.example.resportory;

import com.example.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentResportory extends JpaRepository<Comment, Long> {

}
