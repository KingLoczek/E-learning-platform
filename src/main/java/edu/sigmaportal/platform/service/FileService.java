package edu.sigmaportal.platform.service;

import edu.sigmaportal.platform.dto.FileDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface FileService {
    FileDto upload(String userId, MultipartFile file);
    FileDto upload(String userId, FileDto file);
    FileStream stream(String id);
    FileDto find(String id);
    FileDto update(String id, FileDto file);
    void delete(String id);
    boolean allExist(List<String> fileIds);
    boolean owns(String userid, String fileId);
    boolean ownsAll(String authorId, List<String> fileIds);
    Optional<String> connectedCourseId(String id);

    interface FileStream {
        InputStream handle();
        String mimeType();
        String filename();
    }
}
