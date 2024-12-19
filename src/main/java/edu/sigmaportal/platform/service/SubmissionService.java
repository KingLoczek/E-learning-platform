package edu.sigmaportal.platform.service;

import edu.sigmaportal.platform.dto.SubmissionDto;

/**
 * Service for managing submissions
 */
public interface SubmissionService {
    /**
     * Creates a submission. <br/>
     * Ignores {@code studentId} and {@code id} passed inside {@code grade}.
     * Instead {@code studentId} parameter will be used and for {@code id} a fresh one will be created.
     * The submission will not be {@code submitted} when created (value of the filed is ignored),
     * use {@link SubmissionService#submit(String)} to mark submission as submitted.
     *
     * @param studentId ID of the submitting student
     * @param submission submission object to create
     * @return Created submission
     */
    SubmissionDto create(String studentId, SubmissionDto submission);

    /**
     * Checks if a user owns a submission.
     * @param id ID of the submission to check
     * @param studentId ID of the student to check
     * @return {@code true} if the student ID of the submission equals {@code studentId}, {@code false} otherwise
     */
    boolean owns(String id, String studentId);

    /**
     * Updates a submission. <br/>
     * Can only be used to change attached files.
     * <p>
     * <b>Submission cannot be updated after it has been submitted.</b>
     *
     * @param id ID of the submission to update
     * @param submission submission object to update values from
     * @return Updated submission
     */
    SubmissionDto update(String id, SubmissionDto submission);

    /**
     * Submits a submission. <br/>
     * The submission must not be already submitted and the related assignment must not be closed.
     * @param id ID of the submission
     * @return {@code true} if the submission was marked as submitted, {@code false} if it was already marked as such
     */
    boolean submit(String id);

    /**
     * Finds the ID of a course that a submission, through assignment is related to.
     * @param id ID of the submission
     * @return ID of the related course
     */
    String owningCourseId(String id);

    /**
     * Deletes a submission.
     * @param id ID of the submission
     */
    void delete(String id);

    /**
     * Finds a submission. <br>
     * Throws if a submission is not found.
     * @param id ID of the submission to find
     * @return Found submission
     */
    SubmissionDto find(String id);

    /**
     * Checks if a submission exists.
     * @param submissionId ID of the submission to check
     * @return {@code true} if there exists a submission with ID equal to {@code submission}, {@code false} otherwise
     */
    boolean exists(String submissionId);

    /**
     * Checks if a submission is submitted
     * @param sid ID of the submission
     * @return {@code true} if a submission is submitted, {@code false} otherwise
     */
    boolean isSubmitted(int sid);
}
