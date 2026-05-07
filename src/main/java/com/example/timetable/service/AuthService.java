package com.example.timetable.service;

import com.example.timetable.dto.AuthRequest;
import com.example.timetable.dto.AuthResponse;
import com.example.timetable.model.AppUser;
import com.example.timetable.repository.AppUserRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AppUserRepository appUserRepository;

    public AuthService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    public AuthResponse signup(AppUser user) {
        appUserRepository.findByEmailIgnoreCase(user.getEmail()).ifPresent(existing -> {
            throw new IllegalArgumentException("An account with this email already exists.");
        });

        AppUser savedUser = appUserRepository.save(user);
        return toResponse(savedUser);
    }

    public AuthResponse login(AuthRequest request) {
        AppUser user = appUserRepository.findByEmailIgnoreCase(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));

        if (!user.getPassword().equals(request.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password.");
        }

        return toResponse(user);
    }

    private AuthResponse toResponse(AppUser user) {
        return new AuthResponse(user.getId(), user.getName(), user.getEmail());
    }
}
