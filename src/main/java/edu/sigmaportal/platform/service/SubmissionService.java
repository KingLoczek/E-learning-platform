package edu.sigmaportal.platform.service;

import edu.sigmaportal.platform.dto.SubmissionDto;

public interface SubmissionService {
    SubmissionDto create(String studentId, SubmissionDto submission);
    boolean owns(String id, String studentId);
    SubmissionDto update(String id, SubmissionDto submission);
    boolean submit(String id);
    String owningCourseId(String id);
    void delete(String id);
    SubmissionDto find(String id);
    boolean exists(String submissionId);
    boolean isSubmitted(int sid);
}
