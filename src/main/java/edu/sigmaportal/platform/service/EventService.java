package edu.sigmaportal.platform.service;

import edu.sigmaportal.platform.dto.EventDto;

import java.util.Collection;

/**
 * Service for managing events.
 */
public interface EventService {
    /**
     * Create an event. <br/>
     * Ignores {@code id} passed inside {@code event}, fresh ID is generated.
     *
     * @param event event object to create
     * @return Created event
     */
    EventDto create(EventDto event);

    /**
     * Update an event.
     * Ignores {@code id} passed inside {@code event}, {@code id} parameter is used instead.
     * ID will not and cannot change as a result of this operation.
     * {@code courseId} cannot be changed and if passed will be ignored.
     * To retain the old value pass a {@code null} in the respective field in {@code event}.
     *
     * @param id ID of the event to update
     * @param event event object to update values from
     * @return Updated event
     */
    EventDto update(String id, EventDto event);

    /**
     * Finds the course ID that the given event is tied to.
     *
     * @param id ID of the event to find the owning course ID
     * @return ID of the course
     */
    String owningCourseId(String id);

    /**
     * Deletes an event.
     * @param id ID of the event to delete
     */
    void delete(String id);

    /**
     * Finds an event. <br/>
     *
     * Throws if an event is not found.
     * @param id ID of the event
     * @return Found event
     */
    EventDto find(String id);

    /**
     * Finds all event owned by a course.
     * @param id ID of the course to search
     * @return collection of events
     */
    Collection<String> findOwnedBy(String id);
}
