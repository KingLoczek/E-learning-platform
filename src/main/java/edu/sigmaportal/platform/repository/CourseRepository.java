package edu.sigmaportal.platform.repository;

import edu.sigmaportal.platform.model.CourseModel;
import org.springframework.data.repository.ListCrudRepository;

public interface CourseRepository extends ListCrudRepository<CourseModel, Integer> {
}
