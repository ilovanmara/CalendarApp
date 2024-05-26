package com.example.calendar.Controller;

import com.example.calendar.DTO.FriendDTO;
import com.example.calendar.DTO.UserDetailsDTO;
import com.example.calendar.Service.FriendshipService;
import com.example.calendar.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/friendship")
public class FriendshipController {
    @Autowired
    private FriendshipService friendshipService;
    @Autowired
    private UserService userService;

    @PostMapping("/send")
    public ResponseEntity<String> sendFriendRequest(@RequestParam Long receiverId) {
        friendshipService.sendFriendRequest(userService.findLoggedInUserId(), receiverId);
        return ResponseEntity.ok("Friend request sent successfully.");
    }

    @PostMapping("/accept")
    public ResponseEntity<String> acceptFriendRequest(@RequestParam Long requesterId, @RequestParam Long receiverId) {
        friendshipService.acceptFriendRequest(requesterId, receiverId);
        return ResponseEntity.ok("Friend request accepted successfully.");
    }

    @PostMapping("/decline")
    public ResponseEntity<String> declineFriendRequest(@RequestParam Long requesterId, @RequestParam Long receiverId) {
        friendshipService.declineFriendRequest(requesterId, receiverId);
        return ResponseEntity.ok("Friend request declined successfully.");
    }

    @GetMapping("/pendingRequests")
    public ResponseEntity<List<UserDetailsDTO>> getPendingFriendRequests() {
        Long userId = userService.findLoggedInUserId();
        List<UserDetailsDTO> pendingRequests = friendshipService.findPendingFriendRequestsByUserId(userId);
        return ResponseEntity.ok(pendingRequests);
    }

    @GetMapping("/acceptedFriends")
    public ResponseEntity<List<UserDetailsDTO>> getAcceptedFriends() {
        Long userId = userService.findLoggedInUserId();
        List<UserDetailsDTO> acceptedFriends = friendshipService.findAcceptedFriendsByUserId(userId);
        return ResponseEntity.ok(acceptedFriends);
    }

    @GetMapping("/non-friends-and-pending")
    public ResponseEntity<List<UserDetailsDTO>> getNonFriendsAndPending() {
        Long userId = userService.findLoggedInUserId();
        List<UserDetailsDTO> nonFriendsAndPending = friendshipService.getNonFriendsAndPending(userId);
        return ResponseEntity.ok(nonFriendsAndPending);
    }
}
