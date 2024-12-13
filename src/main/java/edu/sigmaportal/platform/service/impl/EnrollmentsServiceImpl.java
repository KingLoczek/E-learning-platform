package edu.sigmaportal.platform.service.impl;

import edu.sigmaportal.platform.dto.EnrollmentDto;
import edu.sigmaportal.platform.exception.EntityNotFoundException;
import edu.sigmaportal.platform.model.EnrollmentModel;
import edu.sigmaportal.platform.model.StatusType;
import edu.sigmaportal.platform.repository.EnrollmentsRepository;
import edu.sigmaportal.platform.service.EnrollmentsService;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class EnrollmentsServiceImpl implements EnrollmentsService {

    private final EnrollmentsRepository repo;

    public EnrollmentsServiceImpl(EnrollmentsRepository repo) {
        this.repo = repo;
    }

    @Override
    public void remove(String courseId, String userId) {
        int sid = strToStudentId(userId);
        int cid = strToCourseId(courseId);

        EnrollmentModel enrollment = repo.findByStudentIdAndCourseId(sid, cid)
                .orElseThrow(() -> new EntityNotFoundException("Not enrolled"));
        repo.delete(enrollment);
    }

    @Override
    public void remove(String id) {
        int eid = strToEnrollmentId(id);
        EnrollmentModel model = repo.findById(eid).orElseThrow(() -> new EntityNotFoundException("Enrollment not found"));
        repo.delete(model);
    }

    @Override
    public boolean owns(String enrollmentId, String userId) {
        int sid = strToStudentId(userId);
        int eid = strToEnrollmentId(enrollmentId);
        return repo.existsByEnrollmentIdAndStudentId(eid, sid);
    }

    @Override
    public EnrollmentDto find(String id) {
        int eid = strToEnrollmentId(id);
        EnrollmentModel model = repo.findById(eid).orElseThrow(() -> new EntityNotFoundException("Enrollment not found"));
        return new EnrollmentDto(idToStr(model.enrollmentId()), idToStr(model.studentId()), idToStr(model.courseId()), model.joinedAt().toInstant(), model.status());
    }

    @Override
    public EnrollmentDto update(String id, EnrollmentDto enrollment) {
        int eid = strToEnrollmentId(id);
        EnrollmentModel model = repo.findById(eid).orElseThrow(() -> new EntityNotFoundException("Enrollment not found"));
        StatusType status = Objects.isNull(enrollment.status) ? model.status() : enrollment.status;
        EnrollmentModel merged = new EnrollmentModel(model.enrollmentId(), model.studentId(), model.courseId(), model.joinedAt(), status);
        EnrollmentModel saved = repo.save(merged);
        return new EnrollmentDto(idToStr(saved.enrollmentId()), idToStr(saved.studentId()), idToStr(saved.courseId()), saved.joinedAt().toInstant(), saved.status());
    }

    @Override
    public boolean isEnrolled(String courseId, String userId) {
        int cid = strToCourseId(courseId);
        int uid = strToStudentId(userId);
        return repo.existsByCourseIdAndStudentId(cid, uid);
    }

    private int strToCourseId(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            throw new EntityNotFoundException("Invalid course id");
        }
    }

    private int strToStudentId(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            throw new EntityNotFoundException("Invalid student id");
        }
    }

    private int strToEnrollmentId(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            throw new EntityNotFoundException("Invalid enrollment id");
        }
    }

    private String idToStr(int id) {
        return Integer.toString(id);
    }
}
