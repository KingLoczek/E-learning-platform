package edu.sigmaportal.platform.repository;

import edu.sigmaportal.platform.model.TopicModel;
import org.springframework.data.repository.CrudRepository;

import java.util.stream.Stream;

public interface TopicsRepository extends CrudRepository<TopicModel, Integer> {
    Stream<TopicModel> findAllByCourseId(Integer courseId);
}
