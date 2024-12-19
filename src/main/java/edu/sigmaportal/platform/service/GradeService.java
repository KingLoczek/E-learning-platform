package edu.sigmaportal.platform.service;

import edu.sigmaportal.platform.dto.GradeDto;

/**
 * Service for managing grades
 */
public interface GradeService {
    /**
     * Creates a grade. <br/>
     * Ignores {@code instructorId} and {@code id} passed inside {@code grade}.
     * Instead {@code instructorId} parameter will be used and for {@code id} a fresh one will be created.
     *
     * @param instructorId ID of the user that created the grade
     * @param grade grade object to create
     * @return Created grade
     */
    GradeDto create(String instructorId, GradeDto grade);

    /**
     * Updates a grade. <br/>
     * Ignores {@code id} passed inside {@code grade}, {@code id} parameter is used instead.
     * ID will not and cannot change as a result of this operation.
     * Only the values of {@code feedback} and {@code grade} can change, rest will be ignored if passed.
     * To retain the old value pass a {@code null} in the respective field in {@code grade}.
     *
     * @param id ID of the grade to update
     * @param grade grade object to update values from
     * @return Updated grade
     */
    GradeDto update(String id, GradeDto grade);

    /**
     * Finds a grade. <br/>
     * Throws if a grade is not found.
     * @param id ID of the grade to find
     * @return Found grade
     */
    GradeDto find(String id);

    /**
     * Deletes a grade.
     * @param id ID of the grade to delete
     */
    void delete(String id);

    /**
     * Finds the ID of a course that a grade, through submission, is related to.
     * @param id ID of the grade
     * @return ID of the related course
     */
    String owningCourseId(String id);

    /**
     * Check if the user can access a grade. <br/>
     * To access a grade the user has either be the related course owner or
     * have created the submission that a grade is for.
     * @param id ID of the grade
     * @param userId ID of the user
     * @return {@code true} if above conditions are met and a user can see the grade, {@code false} otherwise
     */
    boolean hasAccess(String id, String userId);
}
