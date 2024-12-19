package edu.sigmaportal.platform.service;

import edu.sigmaportal.platform.dto.EnrollmentDto;

/**
 * Service for managing enrollments. <br/>
 * For creating an enrollment (enrolling into a course) see {@link CourseService#enroll(String, String, String)}.
 */
public interface EnrollmentsService {
    /**
     * Deletes an enrollment, based on a course and user ID pair. <br/>
     * This also has the effect that a user loses access to a course.
     *
     * @param courseId ID of the related course
     * @param userId ID of the related user
     */
    void remove(String courseId, String userId);

    /**
     * Deletes an enrollment, based on a enrollment ID. <br/>
     * @param id ID of the enrollment to delete
     */
    void remove(String id);

    /**
     * Check if a user owns an enrollment.
     * @param enrollmentId ID of the enrollment to check
     * @param userId ID of the user to check
     * @return {@code true} if the enrollment's user ID equals {@code userId}, {@code false} otherwise
     */
    boolean owns(String enrollmentId, String userId);

    /**
     * Find an enrollment. <br/>
     * Throws if an enrollment is not found.
     *
     * @param id ID of the enrollment to find
     * @return Found enrollment
     */
    EnrollmentDto find(String id);

    /**
     * Updates an enrollment. <br/>
     * Only {@code status} can be changed, everything else is ignored.
     * Passing a {@code null} means a value will not change.
     *
     * @param id ID of the enrollment to update
     * @param enrollment enrollment object to update values from
     * @return Updated enrollment
     */
    EnrollmentDto update(String id, EnrollmentDto enrollment);

    /**
     * Checks if a user is enrolled into a course. <br/>
     * In other words this checks if an enrollment with given user and course ID exists.
     *
     * @param courseId ID of the course to check
     * @param userId ID of the user to check
     * @return {@code true} if such enrollment exists and user is enrolled, {@code false} otherwise
     */
    boolean isEnrolled(String courseId, String userId);
}
