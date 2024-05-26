package com.example.calendar.DTO;

import lombok.Data;

@Data
public class FriendDTO {
    private Long id;
    private String username;

    public FriendDTO(Long id, String username) {
        this.id = id;
        this.username = username;
    }
}
