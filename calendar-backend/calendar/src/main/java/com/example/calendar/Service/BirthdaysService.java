package com.example.calendar.Service;

import com.example.calendar.DTO.BirthdayDTO;
import com.example.calendar.Entity.Birthday;
import com.example.calendar.Entity.User;
import com.example.calendar.Repository.BirthdayRepository;
import com.example.calendar.Repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.swing.text.DateFormatter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class BirthdaysService {

    private final BirthdayRepository birthdayRepository;

    private final UserRepository userRepository;


    public BirthdaysService(BirthdayRepository birthdayRepository, UserRepository userRepository) {
        this.birthdayRepository = birthdayRepository;
        this.userRepository = userRepository;
    }

    public Birthday saveBirthday(BirthdayDTO birthdayDto, Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Birthday birthday = new Birthday();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;
        LocalDate date = LocalDate.parse(birthdayDto.getDate(), formatter);
        birthday.setTitle(birthdayDto.getTitle());
        birthday.setDate(date);
        LocalDateTime reminderDateTime = date.atTime(15,35,00);
        birthday.setReminderDate(reminderDateTime);
        birthday.setUser(user);
        user.getBirthdays().add(birthday);
        user.getBirthdays().add(birthday);
        //userRepository.save(user);
        return birthdayRepository.save(birthday);
    }
    public List<Birthday> getBirthdays(){
        return birthdayRepository.findAll();
    }
    public String deleteBirthday(long id){
        birthdayRepository.deleteById(id);
        return "birthday deleted";
    }
    public List<Birthday> getBirthdaysByUserId(Long userId){
        return birthdayRepository.findByUserId(userId);
    }

    public List<Birthday> getBirthdaysByDate(LocalDate date) {
        return birthdayRepository.findByDate(date);
    }
    public Birthday updateBirthday(long id, BirthdayDTO birthdayDto) {
        Optional<Birthday> optionalBirthday = birthdayRepository.findById(id);

        if (!optionalBirthday.isPresent()) {
            throw new EntityNotFoundException("Birthday not found with id: " + id);
        }

        Birthday birthday = optionalBirthday.get();

        if (birthdayDto.getDate() == null || birthdayDto.getTitle() == null) {
            throw new IllegalArgumentException("Date and title must not be null");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;
        LocalDate date;
        try {
            date = LocalDate.parse(birthdayDto.getDate(), formatter);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid date format, expected format is yyyy-MM-dd", e);
        }

        birthday.setTitle(birthdayDto.getTitle());
        birthday.setDate(date);
        LocalDateTime reminderDateTime = date.atTime(10, 0, 0);
        birthday.setReminderDate(reminderDateTime);

        return birthdayRepository.save(birthday);
    }

}
