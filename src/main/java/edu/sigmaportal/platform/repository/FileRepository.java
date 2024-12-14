package edu.sigmaportal.platform.repository;

import edu.sigmaportal.platform.model.FileModel;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface FileRepository extends CrudRepository<FileModel, Integer>, FileBytesRepository {
    int countAllByFileIdIsIn(List<Integer> ids);
    boolean existsByOwnerIdAndFileId(Integer uid, Integer fid);
    int countAllByFileIdIsInAndOwnerId(List<Integer> fids, int uid);

    @Query("(SELECT course_id FROM \"Topics\" t INNER JOIN \"Assignments\" a USING (topic_id) INNER JOIN \"Submissions\" USING (assignment_id) INNER JOIN \"SubmissionFiles\" USING (submission_id) WHERE file_id=:id) UNION DISTINCT (SELECT course_id FROM \"Topics\" t INNER JOIN \"Assignments\" a USING (topic_id) INNER JOIN \"AssignmentFiles\" USING (assignment_id) WHERE file_id=:id) UNION DISTINCT (SELECT course_id FROM \"Topics\" t INNER JOIN \"Materials\" a USING (topic_id) INNER JOIN \"MaterialFiles\" USING (material_id) WHERE file_id=:id)")
    Optional<String> findConnectedCourseId(int id);
}
