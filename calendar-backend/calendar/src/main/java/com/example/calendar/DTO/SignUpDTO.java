package com.example.calendar.DTO;

import com.example.calendar.Entity.Role;
import lombok.Data;

@Data
public class SignUpDTO {

    private String username;
    private String email;
    private String password;
    private String lastName;
    private String firstName;
    private String role;

}