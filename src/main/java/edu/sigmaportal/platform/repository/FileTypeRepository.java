package edu.sigmaportal.platform.repository;

import edu.sigmaportal.platform.model.FileTypeModel;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface FileTypeRepository extends ListCrudRepository<FileTypeModel, Integer> {
    List<FileTypeModel> findAllByMimeTypeOrExtension(String mimeType, String extension);
    List<FileTypeModel> findAllByMimeType(String mimeType);
}
