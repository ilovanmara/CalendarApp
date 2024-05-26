package com.example.calendar.Service;

import com.example.calendar.Entity.Birthday;
import com.example.calendar.Entity.Events;
import com.example.calendar.Repository.BirthdayRepository;
import com.example.calendar.Repository.EventsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class ReminderService {
    private final BirthdayRepository birthdayRepository;
    private final EventsRepository eventsRepository;
    @Autowired
    private JavaMailSender mailSender;

    public ReminderService(BirthdayRepository birthdayRepository, EventsRepository eventsRepository) {
        this.birthdayRepository = birthdayRepository;
        this.eventsRepository = eventsRepository;

    }

    @Scheduled(fixedRate = 60000)
    public void reminderMailBirthday(){
        List<Birthday> birthdays = birthdayRepository.findAll();
        System.out.println("reminderMailBirthday() called at: " + LocalDateTime.now());

        LocalDateTime currentDateTime = LocalDateTime.now();
        if(birthdays.size() != 0) {
            for (Birthday birthday : birthdays) {
                System.out.println("reminderMailBirthday() called at: " + birthday.getReminderDate());
                LocalDateTime truncatedDate1 = currentDateTime.truncatedTo(java.time.temporal.ChronoUnit.MINUTES);
                LocalDateTime truncatedDate2 = birthday.getReminderDate().truncatedTo(java.time.temporal.ChronoUnit.MINUTES);
                if (birthday.getReminderDate() != null && truncatedDate1.equals(truncatedDate2)) {
                    sendEmail("mara_ilovan13@yahoo.com", "Reminder: " + birthday.getTitle(), "Today is " + birthday.getTitle());
                }
            }
        }
    }

    @Scheduled(fixedRate = 60000)
    public void reminderMailEvent(){
        List<Events> events = eventsRepository.findAll();
        System.out.println("reminderMailEvents() called at: " + LocalDateTime.now());

        LocalDateTime currentDateTime = LocalDateTime.now();
        if(events.size() != 0) {
            for (Events e : events) {
                if(e.isReminder()) {
                    System.out.println("reminderMailBirthday() called at: " + e.getReminderDate());
                    LocalDateTime truncatedDate1 = currentDateTime.truncatedTo(java.time.temporal.ChronoUnit.MINUTES);
                    LocalDateTime truncatedDate2 = e.getReminderDate().truncatedTo(java.time.temporal.ChronoUnit.MINUTES);
                    if (e.getReminderDate() != null && truncatedDate1.equals(truncatedDate2) && e.isReminder()) {
                        sendEmail("mara_ilovan13@yahoo.com", "Reminder: " + e.getTitle(), "Today is " + e.getTitle());
                    }
                }
            }
        }
    }

    private void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        message.setFrom("marailovan02@gmail.com");
        mailSender.send(message);
    }


}
