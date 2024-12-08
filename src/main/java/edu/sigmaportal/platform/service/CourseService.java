package edu.sigmaportal.platform.service;

import edu.sigmaportal.platform.dto.CourseDto;
import edu.sigmaportal.platform.dto.EnrollmentDto;

import java.util.Collection;

public interface CourseService {
    EnrollmentDto enroll(String courseId, String studentId, String key);
    CourseDto create(String creatorId, CourseDto course);
    CourseDto update(String courseId, CourseDto course);
    boolean owns(String instructorId, String courseId);
    Collection<CourseDto> courses();
    void delete(String courseId);
    CourseDto find(String courseId);
}
