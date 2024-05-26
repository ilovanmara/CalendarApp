package com.example.calendar.Service;

import com.example.calendar.DTO.UserDetailsDTO;
import com.example.calendar.Entity.Friendship;
import com.example.calendar.Entity.FriendshipStatus;
import com.example.calendar.Entity.User;
import com.example.calendar.Repository.FriendshipRepository;
import com.example.calendar.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FriendshipService {
    @Autowired
    private FriendshipRepository friendshipRepository;
    @Autowired
    private UserRepository userRepository;

    public void sendFriendRequest(Long requesterId, Long receiverId) {
        User requester = userRepository.findById(requesterId).orElse(null);
        User receiver = userRepository.findById(receiverId).orElse(null);

        if (requester == null || receiver == null) {
            throw new IllegalArgumentException("Requester or Receiver not found");
        }

        Friendship friendship = new Friendship();
        friendship.setFriendRequester(requester);
        friendship.setFriendReceiver(receiver);
        friendship.setFriendshipStatus(FriendshipStatus.PENDING);
        friendshipRepository.save(friendship);

        if (requester.getRequestedFriends() == null) {
            requester.setRequestedFriends(new HashSet<>());
        }
        if (receiver.getRequestedFriends() == null) {
            receiver.setRequestedFriends(new HashSet<>());
        }

        requester.getRequestedFriends().add(friendship);
        receiver.getRequestedFriends().add(friendship);
    }

    public void acceptFriendRequest(Long requesterId, Long receiverId) {
        Friendship friendship = friendshipRepository.findByFriendRequesterIdAndFriendReceiverId(requesterId, receiverId);
        if (friendship != null && friendship.getFriendshipStatus() == FriendshipStatus.PENDING) {
            friendship.setFriendshipStatus(FriendshipStatus.ACCEPTED);
            friendshipRepository.save(friendship);

            User requester = friendship.getFriendRequester();
            User receiver = friendship.getFriendReceiver();

            requester.getRequestedFriends().remove(friendship);
            receiver.getRequestedFriends().remove(friendship);
            requester.getReceivedFriends().add(friendship);
            receiver.getReceivedFriends().add(friendship);
            requester.setFriendCount(requester.getFriendCount() + 1);
            receiver.setFriendCount(receiver.getFriendCount() + 1);

            userRepository.save(requester);
            userRepository.save(receiver);
        }
    }

    public void declineFriendRequest(Long requesterId, Long receiverId) {
        Friendship friendship = friendshipRepository.findByFriendRequesterIdAndFriendReceiverId(requesterId, receiverId);
        if (friendship != null && friendship.getFriendshipStatus() == FriendshipStatus.PENDING) {
            friendship.setFriendshipStatus(FriendshipStatus.DECLINED);
            friendshipRepository.save(friendship);

            User requester = friendship.getFriendRequester();
            User receiver = friendship.getFriendReceiver();

            requester.getRequestedFriends().remove(friendship);
            receiver.getRequestedFriends().remove(friendship);

            userRepository.save(requester);
            userRepository.save(receiver);
        }
    }

    public List<UserDetailsDTO> findPendingFriendRequestsByUserId(Long userId) {
        List<Friendship> friendships = friendshipRepository.findByFriendReceiverIdAndFriendshipStatus(userId, FriendshipStatus.PENDING);
        return extractUserDetailsDTOs(friendships, userId);
    }

    public List<UserDetailsDTO> findAcceptedFriendsByUserId(Long userId) {
        List<Friendship> acceptedFriendships = friendshipRepository.findByFriendReceiverIdAndFriendshipStatusOrFriendRequesterIdAndFriendshipStatus(
                userId, FriendshipStatus.ACCEPTED, userId, FriendshipStatus.ACCEPTED);
        return extractUserDetailsDTOs(acceptedFriendships, userId);
    }

    private List<UserDetailsDTO> extractUserDetailsDTOs(List<Friendship> friendships, Long userId) {
        List<UserDetailsDTO> friendDTOs = new ArrayList<>();
        for (Friendship friendship : friendships) {
            User friendUser;
            if (friendship.getFriendReceiver().getId().equals(userId)) {
                friendUser = friendship.getFriendRequester();
            } else {
                friendUser = friendship.getFriendReceiver();
            }
            friendDTOs.add(new UserDetailsDTO(friendUser.getId(), friendUser.getUsername(), friendUser.getEmail(), friendUser.getFirstName(), friendUser.getLastName(), friendUser.getFriendCount()));
        }
        return friendDTOs;
    }

    public List<UserDetailsDTO> getNonFriendsAndPending(Long currentUserId) {
        List<User> allUsers = userRepository.findAll();
        List<Friendship> friendships = friendshipRepository.findFriendshipsByUserIdAndStatus(currentUserId, FriendshipStatus.ACCEPTED, FriendshipStatus.PENDING);

        List<Long> friendIds = friendships.stream()
                .map(friendship -> {
                    if (friendship.getFriendRequester().getId().equals(currentUserId)) {
                        return friendship.getFriendReceiver().getId();
                    } else {
                        return friendship.getFriendRequester().getId();
                    }
                })
                .collect(Collectors.toList());

        return allUsers.stream()
                .filter(user -> !user.getId().equals(currentUserId) && !friendIds.contains(user.getId()))
                .map(user -> new UserDetailsDTO(user.getId(), user.getUsername(), user.getEmail(), user.getFirstName(), user.getLastName(), user.getFriendCount()))
                .collect(Collectors.toList());
    }
}
