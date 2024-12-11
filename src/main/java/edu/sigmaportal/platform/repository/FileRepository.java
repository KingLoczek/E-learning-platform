package edu.sigmaportal.platform.repository;

import edu.sigmaportal.platform.model.FileModel;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FileRepository extends CrudRepository<FileModel, Integer>, FileBytesRepository {
    int countAllByFileIdIsIn(List<Integer> ids);
}
