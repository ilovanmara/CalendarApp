package com.example.calendar.DTO;

import lombok.Data;

@Data
public class UserDetailsDTO {
    private String username;
    private String email;
    private long friendCount;
    private String firstName;
    private String lastName;
    private long id;

    public UserDetailsDTO(Long id, String username, String email, String firstName, String lastName, long friendCounnt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.friendCount = friendCounnt;
    }
    public UserDetailsDTO(){}
}
