package com.example.calendar.DTO;

import lombok.Data;

@Data
public class TodoOutDto {
    private String title;
    private String description;
    private String todoStatus;
    private String startTime;
    private String endTime;
    private long id;
}
