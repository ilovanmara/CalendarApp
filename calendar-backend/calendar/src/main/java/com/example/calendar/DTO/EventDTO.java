package com.example.calendar.DTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventDTO {
    private String title;
    private String startTime;
    private String endTime;
    private boolean reminder;
    private String reminderDate;
}
