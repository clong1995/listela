package com.cassiomolin.listela.checklist.service;

import com.cassiomolin.listela.checklist.domain.Checklist;
import com.cassiomolin.listela.user.domain.User;
import com.cassiomolin.listela.user.repository.UserRepository;
import com.cassiomolin.listela.checklist.repository.ChecklistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ChecklistService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChecklistRepository checklistRepository;

    public Checklist createChecklist(Checklist checklist) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new RuntimeException("User not found")); // Fix

        checklist.setOwner(user);
        return checklistRepository.insert(checklist);
    }

    public Optional<Checklist> findById(String id) {
        return checklistRepository.findById(id);
    }
}