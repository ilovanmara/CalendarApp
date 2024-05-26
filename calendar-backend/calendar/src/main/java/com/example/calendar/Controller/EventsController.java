package com.example.calendar.Controller;

import com.example.calendar.DTO.EventDTO;
import com.example.calendar.DTO.EventOutDTO;
import com.example.calendar.Entity.Events;
import com.example.calendar.Service.EventsService;
import com.example.calendar.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = "http://localhost:3000")
public class EventsController {

    private final EventsService eventsService;
    private final UserService userService;

    @Autowired
    public EventsController(EventsService eventsService, UserService userService) {
        this.eventsService = eventsService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Events> saveEvent(@RequestBody EventDTO eventDto) {
        Long userId = userService.findLoggedInUserId();
        Events savedEvent = eventsService.saveEvent(eventDto, userId);
        return new ResponseEntity<>(savedEvent, HttpStatus.CREATED);
    }
    @PostMapping("/{eventId}/invite")
    public ResponseEntity<String> inviteFriendsToEvent(@PathVariable Long eventId, @RequestBody List<Long> friendIds) {
        Events event = eventsService.inviteFriendsToEvent(eventId, friendIds);
        if (event != null) {
            return ResponseEntity.ok("Friends successfully invited to the event.");
        } else {
            return ResponseEntity.badRequest().body("Failed to invite friends to the event.");
        }
    }

    @GetMapping
    @Secured("ROLE_USER")
    public ResponseEntity<?> getEventsByUser() {
        Long userId = userService.findLoggedInUserId();
        List<Events> events = eventsService.getEventsByUserId(userId);
        List<EventOutDTO> eventsDto = new ArrayList<>(events.size());
        for(int i = 0; i < events.size(); i++){
            EventOutDTO e = new EventOutDTO();
            e.setReminder(events.get(i).isReminder());
            e.setTitle(events.get(i).getTitle());
            e.setEndTime(events.get(i).getEndTime().toString());
            e.setStartTime(events.get(i).getStartTime().toString());
            if(events.get(i).isReminder()) {
                e.setReminderDate(events.get(i).getReminderDate().toString());
            }
            e.setTitle(events.get(i).getTitle());
            e.setId(events.get(i).getEventId());
            eventsDto.add(e);
        }
        return new ResponseEntity<>(eventsDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEvent(@PathVariable("id") long id) {
        String message = eventsService.deleteEvent(id);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Events> updateEvent(@PathVariable("id") long id, @RequestBody EventDTO eventDto) {
        Events updatedEvent = eventsService.updateEvents(id, eventDto);
        return new ResponseEntity<>(updatedEvent, HttpStatus.OK);
    }
}