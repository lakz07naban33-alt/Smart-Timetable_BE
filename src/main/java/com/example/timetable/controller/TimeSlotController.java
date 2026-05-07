package com.example.timetable.controller;

import com.example.timetable.model.TimeSlot;
import com.example.timetable.service.TimeSlotService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/timeslots")
public class TimeSlotController {

    private final TimeSlotService timeSlotService;

    public TimeSlotController(TimeSlotService timeSlotService) {
        this.timeSlotService = timeSlotService;
    }

    @GetMapping
    public List<TimeSlot> getAll() {
        return timeSlotService.findAll();
    }

    @PostMapping
    public TimeSlot create(@Valid @RequestBody TimeSlot timeSlot) {
        return timeSlotService.create(timeSlot);
    }

    @PutMapping("/{id}")
    public TimeSlot update(@PathVariable Long id, @Valid @RequestBody TimeSlot timeSlot) {
        return timeSlotService.update(id, timeSlot);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        timeSlotService.delete(id);
    }
}
