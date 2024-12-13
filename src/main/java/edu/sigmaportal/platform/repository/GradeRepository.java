package edu.sigmaportal.platform.repository;

import edu.sigmaportal.platform.model.GradeModel;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

public interface GradeRepository extends CrudRepository<GradeModel, Integer> {
    @Query("SELECT c.course_id FROM \"Courses\" c INNER JOIN \"Topics\" t USING (course_id) INNER JOIN \"Assignments\" a USING (topic_id) INNER JOIN \"Submissions\" s USING (assignment_id) INNER JOIN \"Grades\" g USING (submission_id) WHERE g.grade_id = :id")
    int findOwningCourseIdByGradeId(int id);

    boolean existsBySubmissionId(int sid);

    boolean existsByGradeIdAndInstructorId(int id, int instructorId);
}
