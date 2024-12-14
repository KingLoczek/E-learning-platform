package edu.sigmaportal.platform.repository;

import edu.sigmaportal.platform.model.AssignmentModel;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.stream.Stream;

public interface AssignmentRepository extends CrudRepository<AssignmentModel, Integer> {
    @Query("SELECT c.course_id FROM \"Courses\" c INNER JOIN \"Topics\" t USING (course_id) INNER JOIN \"Assignments\" m USING (topic_id) WHERE m.assignment_id = :id")
    Optional<Integer> findOwningCourseIdByAssignmentId(int id);

    @Query("SELECT c.instructor_id FROM \"Courses\" c INNER JOIN \"Topics\" t USING (course_id) INNER JOIN \"Assignments\" m USING (topic_id) WHERE m.assignment_id = :id")
    Optional<Integer> findOwnerIdByAssignmentId(int id);

    Stream<AssignmentModel> findByTopicId(int tid);
}
