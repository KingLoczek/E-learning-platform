package edu.sigmaportal.platform.repository;

import edu.sigmaportal.platform.model.MaterialModel;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface MaterialRepository extends CrudRepository<MaterialModel, Integer> {
    @Query("SELECT c.course_id FROM \"Courses\" c INNER JOIN \"Topics\" t USING (course_id) INNER JOIN \"Materials\" m USING (topic_id) WHERE m.material_id = :id")
    Optional<Integer> findOwningCourseIdByMaterialId(int id);
}
