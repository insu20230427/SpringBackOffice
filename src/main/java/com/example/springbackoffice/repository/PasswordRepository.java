package com.example.springbackoffice.repository;

import com.example.springbackoffice.entity.PasswordHistory;
import com.example.springbackoffice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PasswordRepository extends JpaRepository <PasswordHistory, Long> {
    List<PasswordHistory> findTop3ByUserOrderByModifiedAtDesc(User user);
}
