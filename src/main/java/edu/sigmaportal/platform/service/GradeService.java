package edu.sigmaportal.platform.service;

import edu.sigmaportal.platform.dto.GradeDto;

public interface GradeService {
    GradeDto create(String instructorId, GradeDto grade);
    GradeDto update(String id, GradeDto grade);
    GradeDto find(String id);
    void delete(String id);
    String owningCourseId(String id);
    boolean hasAccess(String id, String userId);
}
