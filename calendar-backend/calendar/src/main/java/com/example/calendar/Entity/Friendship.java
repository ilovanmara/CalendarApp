package com.example.calendar.Entity;

import jakarta.persistence.*;

@Entity
public class Friendship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFriendship;

    @ManyToOne
    @JoinColumn(name = "requester_id")
    private User friendRequester;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User friendReceiver;

    @Enumerated(EnumType.STRING)
    private FriendshipStatus friendshipStatus;

    public Long getIdFriendship() {
        return idFriendship;
    }

    public void setIdFriendship(Long idFriendship) {
        this.idFriendship = idFriendship;
    }

    public User getFriendRequester() {
        return friendRequester;
    }

    public void setFriendRequester(User friendRequester) {
        this.friendRequester = friendRequester;
    }

    public User getFriendReceiver() {
        return friendReceiver;
    }

    public void setFriendReceiver(User friendReceiver) {
        this.friendReceiver = friendReceiver;
    }

    public FriendshipStatus getFriendshipStatus() {
        return friendshipStatus;
    }

    public void setFriendshipStatus(FriendshipStatus friendshipStatus) {
        this.friendshipStatus = friendshipStatus;
    }
}
