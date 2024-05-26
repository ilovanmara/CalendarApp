package com.example.calendar.Repository;

import com.example.calendar.Entity.Events;
import com.example.calendar.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EventsRepository extends JpaRepository<Events, Long> {
    List<Events> findByUserId(Long userId);
    Optional<Events> findByEventId(Long id);

}
