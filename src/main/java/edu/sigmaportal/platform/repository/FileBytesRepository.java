package edu.sigmaportal.platform.repository;

import edu.sigmaportal.platform.model.FileModel;

import java.io.InputStream;
import java.util.Optional;

public interface FileBytesRepository {
    FileModel saveWithContents(FileModel file, InputStream contents, long length);
    Optional<FileModel> findByIdWithContents(Integer id);
}
