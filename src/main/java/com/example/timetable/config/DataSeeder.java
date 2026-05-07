package com.example.timetable.config;

import com.example.timetable.model.AppUser;
import com.example.timetable.repository.AppUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final AppUserRepository appUserRepository;

    public DataSeeder(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    public void run(String... args) {
        appUserRepository.findByEmailIgnoreCase("admin@timetable.com").orElseGet(() -> {
            AppUser admin = new AppUser();
            admin.setName("Admin");
            admin.setEmail("admin@timetable.com");
            admin.setPassword("admin123");
            return appUserRepository.save(admin);
        });
    }
}
