package edu.sigmaportal.platform.repository;

import edu.sigmaportal.platform.model.EnrollmentModel;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface EnrollmentsRepository extends CrudRepository<EnrollmentModel, Integer> {
    Optional<EnrollmentModel> findByStudentIdAndCourseId(int studentId, int courseId);
    boolean existsByEnrollmentIdAndStudentId(int enrollmentId, int studentId);
    boolean existsByCourseIdAndStudentId(int cid, int uid);
}
