package com.procureflow.service;

import com.procureflow.entity.Notification;
import com.procureflow.entity.User;
import com.procureflow.repository.NotificationRepository;
import com.procureflow.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public NotificationService(NotificationRepository notificationRepository,
                                UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void notifyUser(User user, String message, Long purchaseRequestId) {
        Notification n = new Notification();
        n.setUser(user);
        n.setMessage(message);
        n.setPurchaseRequestId(purchaseRequestId);
        notificationRepository.save(n);
    }

    /** Notify every active user holding the given role (e.g. all MANAGER users). */
    @Transactional
    public void notifyRole(String roleName, String message, Long purchaseRequestId) {
        List<User> users = userRepository.findByRole_NameAndActiveTrue(roleName);
        for (User u : users) {
            notifyUser(u, message, purchaseRequestId);
        }
    }

    public List<Notification> getMyNotifications(User user) {
        return notificationRepository.findByUserOrderByCreatedAtDesc(user);
    }

    public long countUnread(User user) {
        return notificationRepository.countByUserAndIsReadFalse(user);
    }

    @Transactional
    public Notification markRead(Long id, User user) {
        Notification n = notificationRepository.findById(id)
                .filter(x -> x.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new com.procureflow.exception.ResourceNotFoundException(
                        "Notification not found: " + id));
        n.setRead(true);
        return notificationRepository.save(n);
    }
}
