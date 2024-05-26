package com.example.calendar.DTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TodoDTO {
    private String title;
    private String description;
    private String todoStatus;
    private String startTime;
    private String endTime;
}
