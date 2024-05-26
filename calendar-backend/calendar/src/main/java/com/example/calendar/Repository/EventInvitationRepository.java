package com.example.calendar.Repository;

import com.example.calendar.Entity.EventInvitation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventInvitationRepository extends JpaRepository<EventInvitation, Long> {
}
