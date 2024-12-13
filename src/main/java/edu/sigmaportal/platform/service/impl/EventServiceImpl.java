package edu.sigmaportal.platform.service.impl;

import edu.sigmaportal.platform.dto.EventDto;
import edu.sigmaportal.platform.exception.BadRequestException;
import edu.sigmaportal.platform.exception.DependentEntityNotFoundException;
import edu.sigmaportal.platform.exception.EntityNotFoundException;
import edu.sigmaportal.platform.model.EventModel;
import edu.sigmaportal.platform.repository.EventRepository;
import edu.sigmaportal.platform.service.CourseService;
import edu.sigmaportal.platform.service.EventService;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository repo;
    private final CourseService courses;

    public EventServiceImpl(EventRepository repo, CourseService courses) {
        this.repo = repo;
        this.courses = courses;
    }

    @Override
    public EventDto create(EventDto event) {
        OffsetDateTime start = event.startDate.truncatedTo(ChronoUnit.MINUTES);
        OffsetDateTime end = event.endDate.truncatedTo(ChronoUnit.MINUTES);
        OffsetDateTime now = OffsetDateTime.now().truncatedTo(ChronoUnit.MINUTES);

        if (start.isAfter(end))
            throw new BadRequestException("start_date is after end_date");

        if (end.isBefore(now))
            throw new BadRequestException("end_date is in the past");

        if (!courses.exists(event.courseId))
            throw new DependentEntityNotFoundException("Course does not exist");

        int cid = strToCourseId(event.courseId);
        EventModel model = new EventModel(0, event.title.trim(), start, end, cid);
        EventModel saved = repo.save(model);
        return new EventDto(idToStr(saved.eventId()), idToStr(saved.courseId()), saved.title(), saved.startDate(), saved.endDate());
    }

    @Override
    public EventDto update(String id, EventDto event) {
        int eid = strToEventId(id);
        EventModel old = repo.findById(eid).orElseThrow(() -> new EntityNotFoundException("Event not found"));

        OffsetDateTime now = OffsetDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        OffsetDateTime end;
        if (event.endDate != null) {
            end = event.endDate.truncatedTo(ChronoUnit.MINUTES);

            if (end.isBefore(now))
                throw new BadRequestException("end_date is in the past");
        } else {
            end = old.endDate();
        }

        OffsetDateTime start;
        if (event.startDate != null) {
            start = event.startDate.truncatedTo(ChronoUnit.MINUTES);

            if (start.isBefore(now))
                throw new BadRequestException("start_date is in the past");

            if (start.isAfter(end))
                throw new BadRequestException("start_date is after end_date");
        } else {
            start = old.startDate();
        }

        String title = Objects.isNull(event.title) ? old.title() : event.title.trim();
        EventModel merged = new EventModel(old.eventId(), title, start, end, old.courseId());
        EventModel saved = repo.save(merged);
        return new EventDto(idToStr(saved.eventId()), idToStr(saved.courseId()), saved.title(), saved.startDate(), saved.endDate());
    }

    @Override
    public String owningCourseId(String id) {
        return idToStr(repo.findById(strToEventId(id)).orElseThrow(() -> new EntityNotFoundException("Event not foudn")).courseId());
    }

    @Override
    public void delete(String id) {
        int eid = strToEventId(id);
        EventModel model = repo.findById(eid).orElseThrow(() -> new EntityNotFoundException("Event not found"));
        repo.delete(model);
    }

    @Override
    public EventDto find(String id) {
        int eid = strToEventId(id);
        EventModel model = repo.findById(eid).orElseThrow(() -> new EntityNotFoundException("Event not found"));
        return new EventDto(idToStr(model.eventId()), idToStr(model.courseId()), model.title(), model.startDate(), model.endDate());
    }

    @Override
    public Collection<String> findOwnedBy(String id) {
        int cid = strToCourseId(id);
        return repo.findAllByCourseId(cid).map(m -> idToStr(m.eventId())).toList();
    }

    private String idToStr(int id) {
        return Integer.toString(id);
    }

    private int strToEventId(String eventId) {
        try {
            return Integer.parseInt(eventId);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Invalid courseId");
        }
    }

    private int strToCourseId(String courseId) {
        try {
            return Integer.parseInt(courseId);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Invalid courseId");
        }
    }
}
