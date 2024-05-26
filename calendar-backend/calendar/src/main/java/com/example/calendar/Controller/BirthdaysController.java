package com.example.calendar.Controller;

import com.example.calendar.DTO.BirthdayDTO;
import com.example.calendar.DTO.BirthdayOutDTO;
import com.example.calendar.Entity.Birthday;
import com.example.calendar.Service.BirthdaysService;
import com.example.calendar.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/birthdays")
@CrossOrigin(origins = "http://localhost:3000")
public class BirthdaysController {
    @Autowired
    private  BirthdaysService birthdaysService;
    @Autowired
    private  UserService userService;


    @PostMapping
    public ResponseEntity<Birthday> saveBirthday(@RequestBody BirthdayDTO birthdayDto) {
        Long userId = userService.findLoggedInUserId();
        Birthday savedBirthday = birthdaysService.saveBirthday(birthdayDto, userId);
        return new ResponseEntity<>(savedBirthday, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<?> getBirthdaysByUser() {
        Long userId = userService.findLoggedInUserId();
        List<Birthday> birthdays = birthdaysService.getBirthdaysByUserId(userId);
        List<BirthdayOutDTO> birthdayDTOS = new ArrayList<>(birthdays.size());
        for(int i = 0; i < birthdays.size(); i++){
            BirthdayOutDTO b = new BirthdayOutDTO();
            b.setDate(birthdays.get(i).getDate().toString());
            b.setTitle(birthdays.get(i).getTitle());
            b.setId(birthdays.get(i).getId());
            birthdayDTOS.add(b);
        }
        return new ResponseEntity<>(birthdayDTOS, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBirthday(@PathVariable("id") long id) {
        String message = birthdaysService.deleteBirthday(id);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Birthday> updateBirthday(@PathVariable("id") long id, @RequestBody BirthdayDTO birthdayDto) {
        Birthday updatedBirthday = birthdaysService.updateBirthday(id, birthdayDto);
        return new ResponseEntity<>(updatedBirthday, HttpStatus.OK);
    }
    @GetMapping("/date/{date}")
    public ResponseEntity<?> getBirthdaysByDate(@PathVariable("date") String dateString) {
        LocalDate date = LocalDate.parse(dateString);
        List<Birthday> birthdays = birthdaysService.getBirthdaysByDate(date);
        return new ResponseEntity<>(birthdays, HttpStatus.OK);
    }

}
