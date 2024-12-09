package edu.sigmaportal.platform.service;

import edu.sigmaportal.platform.dto.FileDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public interface FileService {
    FileDto upload(MultipartFile file);
    FileDto upload(FileDto file);
    FileStream stream(String id);
    FileDto find(String id);
    FileDto update(String id, FileDto file);
    void delete(String id);

    interface FileStream {
        InputStream handle();
        String mimeType();
        String filename();
    }
}
