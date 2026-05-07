package com.example.timetable.controller;

import com.example.timetable.model.TimetableEntry;
import com.example.timetable.service.TimetableService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/timetable")
public class TimetableController {

    private final TimetableService timetableService;

    public TimetableController(TimetableService timetableService) {
        this.timetableService = timetableService;
    }

    @GetMapping
    public List<TimetableEntry> getTimetable() {
        return timetableService.findAll();
    }

    @PostMapping("/generate")
    public List<TimetableEntry> generateTimetable() {
        return timetableService.generateTimetable();
    }
}
