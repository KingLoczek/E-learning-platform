package edu.sigmaportal.platform.service.impl;

import edu.sigmaportal.platform.dto.CourseDto;
import edu.sigmaportal.platform.dto.EnrollmentDto;
import edu.sigmaportal.platform.exception.EntityNotFoundException;
import edu.sigmaportal.platform.exception.InvalidAccessKeyException;
import edu.sigmaportal.platform.exception.OperationExecutionException;
import edu.sigmaportal.platform.model.CourseModel;
import edu.sigmaportal.platform.model.EnrollmentModel;
import edu.sigmaportal.platform.model.StatusType;
import edu.sigmaportal.platform.repository.CourseRepository;
import edu.sigmaportal.platform.repository.EnrollmentsRepository;
import edu.sigmaportal.platform.service.CourseService;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.Optional;

@Service
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courses;
    private final EnrollmentsRepository enrolls;

    public CourseServiceImpl(CourseRepository courses, EnrollmentsRepository enrolls) {
        this.courses = courses;
        this.enrolls = enrolls;
    }

    @Override
    public EnrollmentDto enroll(String courseId, String studentId, String key) {
        int cid = strToCourseId(courseId);
        int sid = strToStudentId(studentId);

        Optional<EnrollmentModel> existing = enrolls.findByStudentIdAndCourseId(sid, cid);
        if (existing.isPresent()) {
            EnrollmentModel model = existing.get();
            return new EnrollmentDto(idToStr(model.enrollmentId()), idToStr(model.studentId()), idToStr(model.courseId()), model.joinedAt().toInstant(), model.status());
        }

        CourseModel course = courses.findById(cid)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));

        if (!course.accessKey().equals(key)) {
            throw new InvalidAccessKeyException("access_key is invalid");
        }

        if (course.status() != StatusType.ONGOING) {
            throw new OperationExecutionException("Cannot enroll into not ONGOING course");
        }

        OffsetDateTime now = OffsetDateTime.now();
        EnrollmentModel model = new EnrollmentModel(0, sid, cid, now, StatusType.ONGOING);
        EnrollmentModel saved = enrolls.save(model);
        return new EnrollmentDto(idToStr(saved.enrollmentId()), idToStr(saved.studentId()), idToStr(saved.courseId()), saved.joinedAt().toInstant(), saved.status());
    }

    @Override
    public CourseDto create(String creatorId, CourseDto course) {
        int instructorId = strToInstructorId(creatorId);
        String cleanKey = cleanAccessKey(course.accessKey);
        CourseModel model = new CourseModel(0, course.name, course.description, cleanKey, instructorId, StatusType.ONGOING);
        CourseModel saved = courses.save(model);
        return new CourseDto(idToStr(saved.courseId()), saved.name(), saved.description(), idToStr(saved.instructorId()), null, saved.status());
    }

    @Override
    public CourseDto update(String courseId, CourseDto course) {
        int cid = strToCourseId(courseId);
        CourseModel old = courses.findById(cid).orElseThrow(() -> new EntityNotFoundException("Course not found"));
        String key = course.accessKey == null ? old.accessKey() : cleanAccessKey(course.accessKey);
        String name = course.name == null ? old.name() : course.name;
        String description = course.description == null ? old.description() : course.description;
        StatusType type = course.status == null ? old.status() : course.status;

        CourseModel merged = new CourseModel(old.courseId(), name, description, key, old.instructorId(), type);
        CourseModel saved = courses.save(merged);
        return new CourseDto(idToStr(saved.courseId()), saved.name(), saved.description(), idToStr(saved.instructorId()), null, saved.status());
    }

    @Override
    public boolean owns(String instructorId, String courseId) {
        int cid = strToCourseId(courseId);
        CourseModel course = courses.findById(cid).orElseThrow(() -> new EntityNotFoundException("Course not found"));

        int iid = strToInstructorId(instructorId);
        return course.instructorId() == iid;
    }

    @Override
    public Collection<CourseDto> courses() {
        return courses.findAll().stream()
                .map(m -> new CourseDto(idToStr(m.courseId()), m.name(), m.description(), idToStr(m.instructorId()), null, m.status()))
                .toList();
    }

    @Override
    public void delete(String courseId) {
        int cid = strToCourseId(courseId);
        if (!courses.existsById(cid))
            throw new EntityNotFoundException("Course not found");

        courses.deleteById(cid);
    }

    @Override
    public CourseDto find(String courseId) {
        int cid = strToCourseId(courseId);
        CourseModel model = courses.findById(cid).orElseThrow(() -> new EntityNotFoundException("Course not found"));
        return new CourseDto(idToStr(model.courseId()), model.name(), model.description(), idToStr(model.instructorId()), null, model.status());
    }

    private String cleanAccessKey(String accessKey) {
        if (accessKey == null || accessKey.isBlank()) {
            return null;
        }
        return accessKey.trim();
    }

    private int strToCourseId(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            throw new EntityNotFoundException("Course not found");
        }
    }

    private int strToStudentId(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            throw new EntityNotFoundException("Student not found");
        }
    }

    private int strToInstructorId(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            throw new EntityNotFoundException("Instructor not found");
        }
    }

    private String idToStr(int id) {
        return Integer.toString(id);
    }
}
