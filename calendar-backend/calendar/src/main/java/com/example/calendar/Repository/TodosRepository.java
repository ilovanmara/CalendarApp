package com.example.calendar.Repository;

import com.example.calendar.Entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TodosRepository extends JpaRepository<Todo, Long> {
    List<Todo> findByUserId(Long userId);
}
