package edu.sigmaportal.platform.service;

import edu.sigmaportal.platform.dto.AssignmentDto;

import java.util.Collection;

/**
 * Service for managing assignments.
 */
public interface AssignmentService {
    /**
     * Create an assignment. <br/>
     * Ignores {@code assigner} and {@code id} passed inside {@code assignment}.
     * Instead {@code assignerId} parameter will be used and for {@code id} a fresh one will be created.
     *
     * @param assignerId ID of the user that created the assignment
     * @param assignment assignment object to create
     * @return Created assignment
     */
    AssignmentDto create(String assignerId, AssignmentDto assignment);

    /**
     * Updates an existing assignment. <br/>
     * Ignores {@code id} passed inside {@code assignment}, {@code assignmentId} is used instead.
     * ID will not and cannot change as a result of this operation.
     * {@code assigner} cannot be changed and if passed will be ignored.
     * To retain the old value pass a {@code null} in the respective field in {@code assignment}.
     *
     * @param assignmentId ID of the assignment to update
     * @param assigment assignment object to update values from
     * @return Updated assignment
     */
    AssignmentDto update(String assignmentId, AssignmentDto assigment);

    /**
     * Finds an assignment based on ID. <br/>
     *
     * Throws if no assigment is found.
     * @param assignmentId ID of the assignment to find
     * @return Found assignment
     */
    AssignmentDto find(String assignmentId);

    /**
     * Finds the course ID that the given assignment, through Topic, is tied to.
     *
     * @param assignmentId ID of the assignment to find the tied course ID
     * @return ID of the course
     */
    String owningCourseId(String assignmentId);

    /**
     * Deletes an assigment.
     *
     * @param assignmentId ID of the assignment to delete
     */
    void delete(String assignmentId);

    /**
     * Checks assignment existence.
     * @param assignmentId ID of the assignment to check
     * @return {@code true} if an assignment with {@code assignmentId} exists, {@code false} otherwise
     */
    boolean exists(String assignmentId);

    /**
     * Check if the assignment is closed. <br/>
     *
     * Assignment is considered closed if now is after its {@code closeDate}.
     *
     * @param assignmentId ID of the assignment to check
     * @return {@code true} if the assignment is closed (as described above), {@code false} otherwise
     */
    boolean isClosed(int assignmentId);

    /**
     * Find all files attached to an assignment.
     *
     * @param id ID of the assignment
     * @return collection of attached file IDs
     */
    Collection<String> files(String id);

    /**
     * Find all assignments owned (tied to) a topic.
     * @param id ID of the owning topic
     * @return collection of assignment IDs
     */
    Collection<String> findOwnedBy(String id);
}
