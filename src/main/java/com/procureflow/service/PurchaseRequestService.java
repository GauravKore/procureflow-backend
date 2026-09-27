package com.procureflow.service;

import com.procureflow.dto.PurchaseRequestCreateDTO;
import com.procureflow.dto.PurchaseRequestItemDTO;
import com.procureflow.entity.*;
import com.procureflow.exception.InvalidStatusTransitionException;
import com.procureflow.exception.ResourceNotFoundException;
import com.procureflow.exception.UnauthorizedActionException;
import com.procureflow.repository.PurchaseRequestRepository;
import com.procureflow.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PurchaseRequestService {

    private final PurchaseRequestRepository purchaseRequestRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public PurchaseRequestService(PurchaseRequestRepository purchaseRequestRepository,
                                   UserRepository userRepository,
                                   NotificationService notificationService) {
        this.purchaseRequestRepository = purchaseRequestRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + email));
    }

    @Transactional
    public PurchaseRequest create(String requesterEmail, PurchaseRequestCreateDTO dto) {
        User requester = getUserByEmail(requesterEmail);

        PurchaseRequest pr = new PurchaseRequest();
        pr.setTitle(dto.getTitle());
        pr.setDescription(dto.getDescription());
        pr.setRequester(requester);
        pr.setStatus(RequestStatus.PENDING_MANAGER_APPROVAL);

        for (PurchaseRequestItemDTO itemDTO : dto.getItems()) {
            PurchaseRequestItem item = new PurchaseRequestItem();
            item.setItemName(itemDTO.getItemName());
            item.setQuantity(itemDTO.getQuantity());
            item.setEstimatedUnitPrice(itemDTO.getEstimatedUnitPrice());
            pr.addItem(item);
        }

        PurchaseRequest saved = purchaseRequestRepository.save(pr);

        notificationService.notifyRole("MANAGER",
                "New purchase request \"" + saved.getTitle() + "\" awaits your approval", saved.getId());

        return saved;
    }

    public PurchaseRequest getById(Long id) {
        return purchaseRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase request not found: " + id));
    }

    public List<PurchaseRequest> getMyRequests(String email) {
        User user = getUserByEmail(email);
        return purchaseRequestRepository.findByRequester(user);
    }

    public List<PurchaseRequest> getAll() {
        return purchaseRequestRepository.findAll();
    }

    public List<PurchaseRequest> getByStatus(RequestStatus status) {
        return purchaseRequestRepository.findByStatus(status);
    }

    /** EMPLOYEE views their own request; MANAGER/PROCUREMENT/FINANCE can view any. */
    public PurchaseRequest getForViewer(Long id, String viewerEmail, String viewerRole) {
        PurchaseRequest pr = getById(id);

        if ("EMPLOYEE".equals(viewerRole) && !pr.getRequester().getEmail().equals(viewerEmail)) {
            throw new UnauthorizedActionException("You can only view your own purchase requests");
        }

        return pr;
    }

    @Transactional
    public PurchaseRequest managerDecision(Long id, boolean approve, String comment) {
        PurchaseRequest pr = getById(id);

        if (pr.getStatus() != RequestStatus.PENDING_MANAGER_APPROVAL) {
            throw new InvalidStatusTransitionException(
                    "Request is not pending manager approval (current status: " + pr.getStatus() + ")");
        }

        pr.setManagerComment(comment);
        pr.setStatus(approve ? RequestStatus.APPROVED_BY_MANAGER : RequestStatus.REJECTED_BY_MANAGER);

        PurchaseRequest saved = purchaseRequestRepository.save(pr);

        notificationService.notifyUser(pr.getRequester(),
                "Your request \"" + pr.getTitle() + "\" was " + (approve ? "approved" : "rejected") + " by the manager",
                pr.getId());

        if (approve) {
            notificationService.notifyRole("PROCUREMENT",
                    "Request \"" + pr.getTitle() + "\" is approved and needs a vendor", pr.getId());
        }

        return saved;
    }

    @Transactional
    public PurchaseRequest attachVendorAndComment(Long id, Vendor vendor, String comment) {
        PurchaseRequest pr = getById(id);

        if (pr.getStatus() != RequestStatus.APPROVED_BY_MANAGER) {
            throw new InvalidStatusTransitionException(
                    "Request must be approved by manager before procurement can act on it (current status: "
                            + pr.getStatus() + ")");
        }

        pr.setVendor(vendor);
        pr.setProcurementComment(comment);
        pr.setStatus(RequestStatus.VENDOR_ASSIGNED);

        return purchaseRequestRepository.save(pr);
    }

    @Transactional
    public void markPendingFinanceApproval(PurchaseRequest pr) {
        pr.setStatus(RequestStatus.PENDING_FINANCE_APPROVAL);
        purchaseRequestRepository.save(pr);

        notificationService.notifyRole("FINANCE",
                "Request \"" + pr.getTitle() + "\" has a vendor order and needs financial approval", pr.getId());
    }

    @Transactional
    public PurchaseRequest financeDecision(Long id, boolean approve, String comment) {
        PurchaseRequest pr = getById(id);

        if (pr.getStatus() != RequestStatus.PENDING_FINANCE_APPROVAL) {
            throw new InvalidStatusTransitionException(
                    "Request is not pending finance approval (current status: " + pr.getStatus() + ")");
        }

        pr.setFinanceComment(comment);
        pr.setStatus(approve ? RequestStatus.APPROVED_BY_FINANCE : RequestStatus.REJECTED_BY_FINANCE);

        PurchaseRequest saved = purchaseRequestRepository.save(pr);

        notificationService.notifyUser(pr.getRequester(),
                "Your request \"" + pr.getTitle() + "\" was " + (approve ? "approved" : "rejected") + " by finance",
                pr.getId());

        return saved;
    }

    @Transactional
    public PurchaseRequest markCompleted(Long id) {
        PurchaseRequest pr = getById(id);
        pr.setStatus(RequestStatus.COMPLETED);
        PurchaseRequest saved = purchaseRequestRepository.save(pr);

        notificationService.notifyUser(pr.getRequester(),
                "Your request \"" + pr.getTitle() + "\" has been paid and completed", pr.getId());

        return saved;
    }

    @Transactional
    public PurchaseRequest cancel(Long id, String requesterEmail) {
        PurchaseRequest pr = getById(id);

        if (!pr.getRequester().getEmail().equals(requesterEmail)) {
            throw new UnauthorizedActionException("You can only cancel your own purchase requests");
        }

        if (pr.getStatus() != RequestStatus.PENDING_MANAGER_APPROVAL) {
            throw new InvalidStatusTransitionException(
                    "Only requests pending manager approval can be cancelled (current status: " + pr.getStatus() + ")");
        }

        pr.setStatus(RequestStatus.CANCELLED);
        return purchaseRequestRepository.save(pr);
    }
}
