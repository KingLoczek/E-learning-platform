package edu.sigmaportal.platform.service;

import edu.sigmaportal.platform.dto.AssignmentDto;

public interface AssignmentService {
    AssignmentDto create(String assignerId, AssignmentDto assignment);
    AssignmentDto update(String assignmentId, AssignmentDto assigment);
    AssignmentDto find(String assignmentId);
    String owningCourseId(String assignmentId);
    void delete(String assignmentId);
    boolean exists(String assignmentId);
    boolean isClosed(int assignmentId);
}
