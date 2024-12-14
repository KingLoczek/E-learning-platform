package edu.sigmaportal.platform.service;

import edu.sigmaportal.platform.dto.MaterialDto;

import java.util.Collection;

public interface MaterialService {
    MaterialDto create(MaterialDto material, String authorId);
    MaterialDto update(String materialId, MaterialDto material);
    MaterialDto find(String materialId);
    String owningCourseId(String materialId);
    void delete(String materialId);
    Collection<String> files(String materialId);
    Collection<String> findOwnedBy(String id);
}
