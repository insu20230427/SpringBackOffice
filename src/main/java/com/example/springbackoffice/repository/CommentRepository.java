package com.example.springbackoffice.repository;

import com.example.springbackoffice.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
