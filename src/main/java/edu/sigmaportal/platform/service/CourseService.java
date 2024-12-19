package edu.sigmaportal.platform.service;

import edu.sigmaportal.platform.dto.CourseDto;
import edu.sigmaportal.platform.dto.EnrollmentDto;
import jakarta.validation.constraints.NotEmpty;

import java.util.Collection;

/**
 * Service for managing courses and enrolling into a course.
 */
public interface CourseService {
    /**
     * Enroll a student into a course.
     *
     * @param courseId ID of the course to enroll into
     * @param studentId ID of the enrolling student
     * @param key course access key
     * @return enrollment object
     */
    EnrollmentDto enroll(String courseId, String studentId, String key);

    /**
     * Create a course. <br/>
     * Ignores {@code instructorId} and {@code id} passed inside {@code course}.
     * Instead {@code creatorId} parameter will be used and for {@code id} a fresh one will be created.
     * User that creates a course becomes automatically its instructor.
     *
     * @param creatorId ID of the creating user
     * @param course course object to create
     * @return Created course
     */
    CourseDto create(String creatorId, CourseDto course);

    /**
     * Update a course.
     * Ignores {@code id} passed inside {@code course}, {@code courseId} is used instead.
     * ID will not and cannot change as a result of this operation.
     * {@code instructorId} cannot be changed and if passed will be ignored.
     * To retain the old value pass a {@code null} in the respective field in {@code course}.
     *
     * @param courseId ID of the course to update
     * @param course course object to update values from
     * @return Updated course
     */
    CourseDto update(String courseId, CourseDto course);

    /**
     * Checks if a user owns a course.
     *
     * @param instructorId ID of the user to check
     * @param courseId ID of the course to check
     * @return {@code true} if ID of the course instructor equals {@code instructorId}, {@code false} otherwise
     */
    boolean owns(String instructorId, String courseId);

    /**
     * Find all courses.
     * @return collection of all stored courses
     */
    Collection<CourseDto> courses();

    /**
     * Delete a course.
     * @param courseId ID of the course to delete
     */
    void delete(String courseId);

    /**
     * Find a course. <br/>
     *
     * Throws if a course is not found.
     * @param courseId ID of the course to find
     * @return Found course
     */
    CourseDto find(String courseId);

    /**
     * Checks course existence.
     *
     * @param courseId ID of the course to check
     * @return {@code true} if there is a course with ID equal to {@code courseId}, {@code false} otherwise
     */
    boolean exists(String courseId);
}
