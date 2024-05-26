package com.example.calendar.Repository;

import com.example.calendar.Entity.Birthday;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BirthdayRepository extends JpaRepository<Birthday, Long> {
    List<Birthday> findByUserId(Long userId);

    List<Birthday> findByDate(LocalDate date);
}
