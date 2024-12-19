package edu.sigmaportal.platform.service;

import edu.sigmaportal.platform.dto.TopicDto;
import jakarta.validation.constraints.NotEmpty;

import java.util.Collection;

/**
 * Service for managing topics
 */
public interface TopicService {
    /**
     * Creates a topic <br/>
     * Ignores {@code id} passed inside {@code grade}, a fresh ID is generated.
     *
     * @param topic topic object to create
     * @return Created topic
     */
    TopicDto create(TopicDto topic);

    /**
     * Updates a topic. <br/>
     * Ignores {@code id} passed inside {@code topic}, {@code id} parameter is used instead.
     * Course of the topic cannot be changed.
     *
     * @param id ID of the topic to update
     * @param topic topic object to update values from
     * @return Updated topic
     */
    TopicDto update(String id, TopicDto topic);

    /**
     * Finds a topic. <br/>
     * Throws if a topic is not found.
     * @param id ID of the topic
     * @return Found topic
     */
    TopicDto find(String id);

    /**
     * Finds the ID of a course that owns a topic.
     *
     * @param id ID of the topic to find
     * @return ID of the owning course
     */
    String owningCourseId(String id);

    /**
     * Delete a topic.
     * @param id ID of the topic to delete
     */
    void delete(String id);

    /**
     * Finds topics owned by a course.
     * @param id ID of the course
     * @return collection of owned topic IDs
     */
    Collection<String> findOwnedBy(String id);

    /**
     * Checks if a topic exists.
     * @param topicId ID of the topic
     * @return {@code true} if there exists a topic with ID equal to {@code topicId}, {@code false} otherwise
     */
    boolean exists(String topicId);
}
