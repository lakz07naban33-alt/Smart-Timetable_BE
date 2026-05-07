package com.example.timetable.service;

import com.example.timetable.model.TimeSlot;
import com.example.timetable.repository.TimeSlotRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TimeSlotService {

    private final TimeSlotRepository timeSlotRepository;

    public TimeSlotService(TimeSlotRepository timeSlotRepository) {
        this.timeSlotRepository = timeSlotRepository;
    }

    public List<TimeSlot> findAll() {
        return timeSlotRepository.findAll();
    }

    public TimeSlot create(TimeSlot timeSlot) {
        validateTime(timeSlot);
        return timeSlotRepository.save(timeSlot);
    }

    public TimeSlot update(Long id, TimeSlot timeSlot) {
        validateTime(timeSlot);
        TimeSlot existing = timeSlotRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Time slot not found"));
        existing.setDay(timeSlot.getDay());
        existing.setStartTime(timeSlot.getStartTime());
        existing.setEndTime(timeSlot.getEndTime());
        return timeSlotRepository.save(existing);
    }

    public void delete(Long id) {
        timeSlotRepository.deleteById(id);
    }

    private void validateTime(TimeSlot timeSlot) {
        if (timeSlot.getStartTime() != null
                && timeSlot.getEndTime() != null
                && !timeSlot.getEndTime().isAfter(timeSlot.getStartTime())) {
            throw new IllegalArgumentException("End time must be after start time");
        }
    }
}
