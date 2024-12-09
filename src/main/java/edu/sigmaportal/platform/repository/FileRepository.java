package edu.sigmaportal.platform.repository;

import edu.sigmaportal.platform.model.FileModel;
import org.springframework.data.repository.CrudRepository;

public interface FileRepository extends CrudRepository<FileModel, Integer>, FileBytesRepository {
}
