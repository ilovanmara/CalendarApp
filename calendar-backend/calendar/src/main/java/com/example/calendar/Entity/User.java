package com.example.calendar.Entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String username;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column
    private String lastName;
    @Column
    private String firstName;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Column
    private long friendCount;
    @OneToMany(mappedBy = "friendRequester", cascade = CascadeType.ALL)
    private Set<Friendship> requestedFriends;

    @OneToMany(mappedBy = "friendReceiver", cascade = CascadeType.ALL)
    private Set<Friendship> receivedFriends;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Events> events;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Birthday> birthdays;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Todo> todos;

    public List<EventInvitation> getInvitations() {
        return invitations;
    }

    public void setInvitations(List<EventInvitation> invitations) {
        this.invitations = invitations;
    }

    @OneToMany(mappedBy = "user")
    private List<EventInvitation> invitations = new ArrayList<>();

    public Set<Birthday> getBirthdays() {
        return birthdays;
    }

    public void setBirthdays(Set<Birthday> birthdays) {
        this.birthdays = birthdays;
    }

    public Set<Todo> getTodos() {
        return todos;
    }

    public void setTodos(Set<Todo> todos) {
        this.todos = todos;
    }

    public Set<Events> getEvents() {
        return events;
    }

    public void setEvents(Set<Events> events) {
        this.events = events;
    }

    public long getFriendCount() {
        return friendCount;
    }

    public void setFriendCount(long friendCount) {
        this.friendCount = friendCount;
    }

    public Set<Friendship> getRequestedFriends() {
        return requestedFriends;
    }

    public void setRequestedFriends(Set<Friendship> requestedFriends) {
        this.requestedFriends = requestedFriends;
    }

    public Set<Friendship> getReceivedFriends() {
        return receivedFriends;
    }

    public void setReceivedFriends(Set<Friendship> receivedFriends) {
        this.receivedFriends = receivedFriends;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }


}
