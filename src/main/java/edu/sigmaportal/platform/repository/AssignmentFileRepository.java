package edu.sigmaportal.platform.repository;

import edu.sigmaportal.platform.model.AssignmentFileModel;
import edu.sigmaportal.platform.model.AssignmentModel;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.stream.Stream;

public interface AssignmentFileRepository extends ListCrudRepository<AssignmentFileModel, Integer> {
    @Query("DELETE FROM \"AssignmentFiles\" WHERE assignment_id = :id AND file_id in (:fileIds)")
    @Modifying
    void deleteUnlikedFiles(Integer id, List<Integer> fileIds);

    Stream<AssignmentFileModel> findAllByAssignmentId(int assignmentId);

    Stream<Void> removeAllByAssignmentId(int id);
}
