package com.example.calendar.Repository;

import com.example.calendar.Entity.Friendship;
import com.example.calendar.Entity.FriendshipStatus;
import com.example.calendar.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    Optional<Friendship> findById(Long id);
    List<Friendship> findByFriendReceiverIdAndFriendshipStatus(Long userId, FriendshipStatus status);
    List<Friendship> findByFriendRequesterIdAndFriendshipStatus(Long userId, FriendshipStatus status);
    @Query("SELECT f FROM Friendship f WHERE (f.friendRequester.id = :userId OR f.friendReceiver.id = :userId) AND (f.friendshipStatus = :acceptedStatus OR f.friendshipStatus = :pendingStatus)")
    List<Friendship> findFriendshipsByUserIdAndStatus(Long userId, FriendshipStatus acceptedStatus, FriendshipStatus pendingStatus);

    List<Friendship> findByFriendReceiverIdAndFriendshipStatusOrFriendRequesterIdAndFriendshipStatus(Long userId, FriendshipStatus friendshipStatus, Long userId1, FriendshipStatus friendshipStatus1);

    Friendship findByFriendRequesterIdAndFriendReceiverId(Long requesterId, Long receiverId);
}
