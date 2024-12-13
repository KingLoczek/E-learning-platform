package edu.sigmaportal.platform.service;

import edu.sigmaportal.platform.dto.EnrollmentDto;

public interface EnrollmentsService {
    void remove(String courseId, String userId);
    void remove(String id);
    boolean owns(String enrollmentId, String userId);
    EnrollmentDto find(String id);
    EnrollmentDto update(String id, EnrollmentDto enrollment);
    boolean isEnrolled(String courseId, String userId);
}
