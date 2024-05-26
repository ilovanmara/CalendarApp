package com.example.calendar.DTO;

import lombok.Data;

@Data
public class EventOutDTO {
    private String title;
    private String startTime;
    private String endTime;
    private boolean reminder;
    private String reminderDate;
    private long id;
}
