package edu.sigmaportal.platform.service;

import edu.sigmaportal.platform.dto.TopicDto;
import jakarta.validation.constraints.NotEmpty;

import java.util.Collection;

public interface TopicService {
    TopicDto create(TopicDto topic);
    TopicDto update(String id, TopicDto topic);
    TopicDto find(String id);
    String owningCourseId(String id);
    void delete(String id);
    Collection<String> findOwnedBy(String id);
    boolean exists(String topicId);
}
