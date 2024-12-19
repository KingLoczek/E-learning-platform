package edu.sigmaportal.platform.service;

import edu.sigmaportal.platform.dto.MaterialDto;

import java.util.Collection;

/**
 * Service for managing materials
 */
public interface MaterialService {
    /**
     * Creates a material. <br/>
     * Ignores {@code authorId} and {@code id} passed inside {@code grade}.
     * Instead {@code authorId} parameter will be used and for {@code id} a fresh one will be created.
     *
     * @param material material object to create
     * @param authorId ID of the user that added the material
     * @return Created material
     */
    MaterialDto create(MaterialDto material, String authorId);

    /**
     * Updates a material. <br/>
     * Ignores {@code id} passed inside {@code material}, {@code materialId} is used instead.
     * ID will not and cannot change as a result of this operation.
     * {@code authorId} cannot be changed and if passed will be ignored.
     * If topic is changed it must be within the course that the old one is.
     * To retain the old value pass a {@code null} in the respective field in {@code assignment}.
     *
     * @param materialId ID of the material to update
     * @param material material object to update values from
     * @return Updated material
     */
    MaterialDto update(String materialId, MaterialDto material);

    /**
     * Finds a material. <br/>
     * Throws if a material is not found.
     * @param materialId ID of the material to find
     * @return Found material
     */
    MaterialDto find(String materialId);

    /**
     * Find the ID of a course that, though a topic, is related to a material.
     * @param materialId ID of the material
     * @return ID of the course
     */
    String owningCourseId(String materialId);

    /**
     * Deletes a material
     * @param materialId ID of the material
     */
    void delete(String materialId);

    /**
     * Find files attached to a material.
     * @param materialId ID of the material to find
     * @return collection of attached file IDs
     */
    Collection<String> files(String materialId);

    /**
     * Find materials owned by a course.
     * @param id ID of the course to search
     * @return collection of related material IDs
     */
    Collection<String> findOwnedBy(String id);
}
