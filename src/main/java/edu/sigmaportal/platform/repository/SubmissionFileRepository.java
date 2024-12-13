package edu.sigmaportal.platform.repository;

import edu.sigmaportal.platform.model.SubmissionFileModel;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.stream.Stream;

public interface SubmissionFileRepository extends ListCrudRepository<SubmissionFileModel, Integer> {
    Stream<SubmissionFileModel> findAllBySubmissionId(int sid);

    @Query("DELETE FROM \"SubmissionFiles\" WHERE submission_id = :id AND file_id IN (:unlinkedFiles)")
    @Modifying
    void deleteUnlinkedFiles(int id, List<Integer> unlinkedFiles);

    Stream<Void> deleteAllBySubmissionId(int i);
}
