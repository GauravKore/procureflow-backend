package com.procureflow.controller;

import com.procureflow.dto.NotificationResponseDTO;
import com.procureflow.entity.User;
import com.procureflow.exception.ResourceNotFoundException;
import com.procureflow.repository.UserRepository;
import com.procureflow.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private final UserRepository userRepository;

    public NotificationController(NotificationService notificationService, UserRepository userRepository) {
        this.notificationService = notificationService;
        this.userRepository = userRepository;
    }

    private User currentUser(Authentication authentication) {
        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + authentication.getName()));
    }

    @GetMapping
    public ResponseEntity<List<NotificationResponseDTO>> myNotifications(Authentication authentication) {
        User user = currentUser(authentication);
        List<NotificationResponseDTO> result = notificationService.getMyNotifications(user)
                .stream().map(NotificationResponseDTO::fromEntity).toList();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/unread-count")
    public ResponseEntity<Map<String, Long>> unreadCount(Authentication authentication) {
        User user = currentUser(authentication);
        return ResponseEntity.ok(Map.of("unread", notificationService.countUnread(user)));
    }

    @PostMapping("/{id}/read")
    public ResponseEntity<NotificationResponseDTO> markRead(@PathVariable Long id, Authentication authentication) {
        User user = currentUser(authentication);
        return ResponseEntity.ok(NotificationResponseDTO.fromEntity(notificationService.markRead(id, user)));
    }
}
