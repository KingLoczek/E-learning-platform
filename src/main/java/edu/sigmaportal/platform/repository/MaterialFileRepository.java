package edu.sigmaportal.platform.repository;

import edu.sigmaportal.platform.model.MaterialFileModel;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.stream.Stream;

public interface MaterialFileRepository extends ListCrudRepository<MaterialFileModel, Integer> {
    Stream<MaterialFileModel> findAllByMaterialId(int materialId);

    @Query("DELETE FROM \"MaterialFiles\" WHERE material_id = :id AND file_id IN (:fileIds)")
    @Modifying
    void deleteStaleFiles(Integer id, List<Integer> fileIds);

    Stream<Void> removeAllByMaterialId(int mid);
}
