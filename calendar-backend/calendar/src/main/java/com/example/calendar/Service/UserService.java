package com.example.calendar.Service;

import com.example.calendar.DTO.SignUpDTO;
import com.example.calendar.Entity.Friendship;
import com.example.calendar.Entity.Role;
import com.example.calendar.Entity.User;
import com.example.calendar.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    private long currentId;
    private String currentRole;


    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }
    public ResponseEntity<?> createUser(SignUpDTO signUpDTO) {
        if (userRepository.existsByUsername(signUpDTO.getUsername())) {
            return ResponseEntity.badRequest().body("Username is already taken!");
        }

        if (userRepository.existsByEmail(signUpDTO.getEmail())) {
            return ResponseEntity.badRequest().body("Email is already taken!");
        }
        String encodedPassword = passwordEncoder.encode(signUpDTO.getPassword());
        String correctRole;
        if(signUpDTO.getRole().equals("admin")){
            correctRole = "ROLE_ADMIN";
        } else if(signUpDTO.getRole().equals("user")){
            correctRole = "ROLE_USER";
        } else if(signUpDTO.getRole().equals("ROLE_USER") || signUpDTO.getRole().equals("ROLE_ADMIN")){
            correctRole = signUpDTO.getRole();
        } else {
            return ResponseEntity.badRequest().body("Role not found!");
        }

        Role role = Role.valueOf(correctRole);
//        this.currentRole = correctRole;

        User user = new User();
        user.setLastName(signUpDTO.getLastName());
        user.setFirstName(signUpDTO.getFirstName());
        user.setUsername(signUpDTO.getUsername());
        user.setEmail(signUpDTO.getEmail());
        user.setPassword(encodedPassword);
        user.setRole(role);
        user.setFriendCount(0);
        user.setReceivedFriends(new HashSet<Friendship>());
        user.setRequestedFriends(new HashSet<Friendship>());
        user.setBirthdays(new HashSet<>());
        user.setTodos(new HashSet<>());
        user.setEvents(new HashSet<>());

        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully");
    }
    public ResponseEntity<String> authenticate(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                username, password
        ));
        User user = findUserByUsername(username);
        this.currentRole = user.getRole().toString();
        System.out.println(username);
        this.currentId = user.getId();
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return ResponseEntity.ok("User signed-in successfully!.");
    }
    public ResponseEntity<String> logout() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("User logged out successfully.");
    }
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));
    }

    public Long findLoggedInUserId() {
        return this.currentId;
    }

    public String findLoggedInUserRole(){
        return this.currentRole;
    }
    public List<User> getUsers(){return userRepository.findAll();}
    public String deleteUser(long id){
        userRepository.deleteById(id);
        return "user deleted";
    }

}
