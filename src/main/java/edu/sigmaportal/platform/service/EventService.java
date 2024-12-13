package edu.sigmaportal.platform.service;

import edu.sigmaportal.platform.dto.EventDto;

import java.util.Collection;

public interface EventService {
    EventDto create(EventDto event);
    EventDto update(String id, EventDto event);
    String owningCourseId(String id);
    void delete(String id);
    EventDto find(String id);
    Collection<String> findOwnedBy(String id);
}
