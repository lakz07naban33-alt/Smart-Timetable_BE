package com.example.timetable.service;

import com.example.timetable.model.Subject;
import com.example.timetable.repository.SubjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectService {

    private final SubjectRepository subjectRepository;

    public SubjectService(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    public List<Subject> findAll() {
        return subjectRepository.findAll();
    }

    public Subject create(Subject subject) {
        return subjectRepository.save(subject);
    }

    public Subject update(Long id, Subject subject) {
        Subject existing = subjectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Subject not found"));
        existing.setName(subject.getName());
        existing.setHoursPerWeek(subject.getHoursPerWeek());
        return subjectRepository.save(existing);
    }

    public void delete(Long id) {
        subjectRepository.deleteById(id);
    }
}
