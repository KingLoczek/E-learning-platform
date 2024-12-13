package edu.sigmaportal.platform.repository;

import edu.sigmaportal.platform.model.EventModel;
import org.springframework.data.repository.CrudRepository;

import java.util.stream.Stream;

public interface EventRepository extends CrudRepository<EventModel, Integer> {
    Stream<EventModel> findAllByCourseId(int cid);
}
