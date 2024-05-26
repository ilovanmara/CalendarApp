package com.example.calendar.Controller;

import com.example.calendar.DTO.LoginDTO;
import com.example.calendar.DTO.SignUpDTO;
import com.example.calendar.DTO.UserDetailsDTO;
import com.example.calendar.Entity.Role;
import com.example.calendar.Entity.User;
import com.example.calendar.Repository.UserRepository;
import com.example.calendar.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> createUser(@RequestBody SignUpDTO signUpDTO) {
        return userService.createUser(signUpDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody LoginDTO loginDTO) {
        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();

        return userService.authenticate(username, password);
    }
    @DeleteMapping("/logout")
    public ResponseEntity<String> logoutUser() {
        return userService.logout();
    }

    @GetMapping("/currentuser")
    public ResponseEntity<?> loggedinUser(){
        Long userId = userService.findLoggedInUserId();
        return ResponseEntity.ok(userId);
    }

    @GetMapping("/role")
    public ResponseEntity<?> loggedinUserRole(){
        String role = userService.findLoggedInUserRole();
        return ResponseEntity.ok(role);
    }

    @GetMapping()
    public ResponseEntity<?> getUsers(){
        List<User> users = userService.getUsers();
        List<UserDetailsDTO> usersDetails = new ArrayList<>();
        for(int i = 0; i < users.size(); i++){
            UserDetailsDTO user = new UserDetailsDTO();
            user.setEmail(users.get(i).getEmail());
            user.setUsername(users.get(i).getUsername());
            user.setLastName(users.get(i).getLastName());
            user.setFirstName(users.get(i).getFirstName());
            user.setFriendCount(users.get(i).getFriendCount());
            user.setId(users.get(i).getId());
            if(users.get(i).getRole().equals(Role.ROLE_USER)) {
                usersDetails.add(user);
            }
        }
        return new ResponseEntity<>(usersDetails, HttpStatus.OK);
    }
    @GetMapping("/currentUserId")
    public ResponseEntity<Long> getCurrentUserId() {
        Long userId = userService.findLoggedInUserId();
        return ResponseEntity.ok(userId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser (@PathVariable("id") long id){
        String message = userService.deleteUser(id);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }


}
