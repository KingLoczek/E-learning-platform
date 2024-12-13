package edu.sigmaportal.platform.repository;

import edu.sigmaportal.platform.model.SubmissionModel;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SubmissionRepository extends CrudRepository<SubmissionModel, Integer> {
    boolean existsByStudentIdAndSubmissionId(int studentId, int submissionId);
    boolean existsByStudentIdAndAssignmentId(int sid, int aid);

    @Query("SELECT c.course_id FROM \"Courses\" c INNER JOIN \"Topics\" t USING (course_id) INNER JOIN \"Assignments\" a USING (topic_id) INNER JOIN \"Submissions\" s USING (assignment_id) WHERE s.submission_id = :id")
    Optional<Integer> findOwningCourseId(int id);
}
