package com.example.calendar.Service;

import com.example.calendar.DTO.EventDTO;
import com.example.calendar.Entity.EventInvitation;
import com.example.calendar.Entity.Events;
import com.example.calendar.Entity.User;
import com.example.calendar.Repository.EventInvitationRepository;
import com.example.calendar.Repository.EventsRepository;
import com.example.calendar.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class EventsService {

    private final EventsRepository eventsRepository;
    private final UserRepository userRepository;
    private final EventInvitationRepository eventInvitationRepository;

    public EventsService(EventsRepository eventsRepository, UserRepository userRepository, EventInvitationRepository eventInvitationRepository) {
        this.eventsRepository = eventsRepository;
        this.userRepository = userRepository;
        this.eventInvitationRepository = eventInvitationRepository;
    }
    public Events saveEvent(EventDTO eventDto, Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Events event = new Events();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        LocalDateTime startDate = LocalDateTime.parse(eventDto.getStartTime(), formatter);
        LocalDateTime endDate = LocalDateTime.parse(eventDto.getEndTime(), formatter);
        if(eventDto.isReminder()) {
            LocalDateTime reminderDate = LocalDateTime.parse(eventDto.getReminderDate(), formatter);
            event.setReminderDate(reminderDate);
        }
        event.setTitle(eventDto.getTitle());
        event.setStartTime(startDate);
        event.setEndDate(endDate);
        event.setReminder(eventDto.isReminder());
        event.setUser(user);
        user.getEvents().add(event);
        return eventsRepository.save(event);
    }
    public Events addFriendsToEvent(Long eventId, List<Long> friendIds) {
        Events event = eventsRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));

        List<User> invitedFriends = userRepository.findAllById(friendIds);
        //event.getInvitedFriends().addAll(invitedFriends);
        return eventsRepository.save(event);
    }
    public List<Events> getEvents(){

        return eventsRepository.findAll();
    }

    public String deleteEvent(long id){
        eventsRepository.deleteById(id);
        return "event removed";
    }

    public List<Events> getEventsByUserId(Long userId){

        return eventsRepository.findByUserId(userId);
    }

    public Events updateEvents(long id, EventDTO eventDto){
        Events event = eventsRepository.findById(id).orElse(null);
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        LocalDateTime startDate = LocalDateTime.parse(eventDto.getStartTime(), formatter);
        LocalDateTime endDate = LocalDateTime.parse(eventDto.getEndTime(), formatter);
        LocalDateTime reminderDate = LocalDateTime.parse(eventDto.getReminderDate(), formatter);
        event.setTitle(eventDto.getTitle());
        event.setStartTime(startDate);
        event.setEndDate(endDate);
        event.setReminder(eventDto.isReminder());
        if(eventDto.isReminder()){
            event.setReminderDate(reminderDate);
        }
        return eventsRepository.save(event);
    }
    public Events inviteFriendsToEvent(Long eventId, List<Long> friendIds) {
        Events event = eventsRepository.findByEventId(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));
        System.out.println(event.getTitle());
        for (Long friendId : friendIds) {
            User friend = userRepository.findById(friendId)
                    .orElseThrow(() -> new IllegalArgumentException("Friend not found"));

            EventInvitation attendance = new EventInvitation();
            attendance.setUser(friend);
            attendance.setEvents(event);
            event.getEventInvitationSet().add(attendance);
            friend.getInvitations().add(attendance);
            eventInvitationRepository.save(attendance);
        }
        EventDTO ev = new EventDTO();
        ev.setEndTime(event.getEndTime().toString());
        ev.setStartTime(event.getStartTime().toString());
        ev.setTitle(event.getTitle());
        ev.setReminder(event.isReminder());
        if(event.isReminder()){
            ev.setReminderDate(event.getReminderDate().toString());
        }
        for (Long friendId : friendIds) {
            this.saveEvent(ev, friendId);
        }

        return event;
    }


}
