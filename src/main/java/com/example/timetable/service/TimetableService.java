package com.example.timetable.service;

import com.example.timetable.model.Faculty;
import com.example.timetable.model.Room;
import com.example.timetable.model.Subject;
import com.example.timetable.model.TimeSlot;
import com.example.timetable.model.TimetableEntry;
import com.example.timetable.repository.FacultyRepository;
import com.example.timetable.repository.RoomRepository;
import com.example.timetable.repository.SubjectRepository;
import com.example.timetable.repository.TimeSlotRepository;
import com.example.timetable.repository.TimetableEntryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class TimetableService {

    private final SubjectRepository subjectRepository;
    private final FacultyRepository facultyRepository;
    private final RoomRepository roomRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final TimetableEntryRepository timetableEntryRepository;

    public TimetableService(
            SubjectRepository subjectRepository,
            FacultyRepository facultyRepository,
            RoomRepository roomRepository,
            TimeSlotRepository timeSlotRepository,
            TimetableEntryRepository timetableEntryRepository
    ) {
        this.subjectRepository = subjectRepository;
        this.facultyRepository = facultyRepository;
        this.roomRepository = roomRepository;
        this.timeSlotRepository = timeSlotRepository;
        this.timetableEntryRepository = timetableEntryRepository;
    }

    public List<TimetableEntry> findAll() {
        return timetableEntryRepository.findAll();
    }

    @Transactional
    public List<TimetableEntry> generateTimetable() {
        List<Subject> subjects = subjectRepository.findAll();
        List<Faculty> faculties = facultyRepository.findAll();
        List<Room> rooms = roomRepository.findAll();
        List<TimeSlot> timeSlots = timeSlotRepository.findAll();

        if (subjects.isEmpty() || faculties.isEmpty() || rooms.isEmpty() || timeSlots.isEmpty()) {
            throw new IllegalArgumentException("Please add subjects, faculty, rooms and time slots before generating.");
        }

        subjects.sort(Comparator.comparing(Subject::getId));
        faculties.sort(Comparator.comparing(Faculty::getId));
        rooms.sort(Comparator.comparing(Room::getId));
        timeSlots.sort(Comparator.comparing(TimeSlot::getDay).thenComparing(TimeSlot::getStartTime));

        timetableEntryRepository.deleteAll();

        List<TimetableEntry> generatedEntries = new ArrayList<>();
        Set<String> busyFaculty = new HashSet<>();
        Set<String> busyRooms = new HashSet<>();

        for (int subjectIndex = 0; subjectIndex < subjects.size(); subjectIndex++) {
            Subject subject = subjects.get(subjectIndex);
            Faculty faculty = faculties.get(subjectIndex % faculties.size());

            for (int hour = 0; hour < subject.getHoursPerWeek(); hour++) {
                TimetableEntry entry = findAvailableEntry(subject, faculty, rooms, timeSlots, busyFaculty, busyRooms);
                if (entry == null) {
                    throw new IllegalStateException("Could not schedule all subjects. Add more rooms, faculty, or time slots.");
                }

                generatedEntries.add(entry);
                busyFaculty.add(key(faculty.getId(), entry.getTimeslot().getId()));
                busyRooms.add(key(entry.getRoom().getId(), entry.getTimeslot().getId()));
            }
        }

        return timetableEntryRepository.saveAll(generatedEntries);
    }

    private TimetableEntry findAvailableEntry(
            Subject subject,
            Faculty faculty,
            List<Room> rooms,
            List<TimeSlot> timeSlots,
            Set<String> busyFaculty,
            Set<String> busyRooms
    ) {
        for (TimeSlot timeSlot : timeSlots) {
            if (busyFaculty.contains(key(faculty.getId(), timeSlot.getId()))) {
                continue;
            }

            for (Room room : rooms) {
                if (!busyRooms.contains(key(room.getId(), timeSlot.getId()))) {
                    TimetableEntry entry = new TimetableEntry();
                    entry.setSubject(subject);
                    entry.setFaculty(faculty);
                    entry.setRoom(room);
                    entry.setTimeslot(timeSlot);
                    return entry;
                }
            }
        }

        return null;
    }

    private String key(Long resourceId, Long timeSlotId) {
        return resourceId + "-" + timeSlotId;
    }
}
