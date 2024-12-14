package edu.sigmaportal.platform.service.impl;

import edu.sigmaportal.platform.dto.FileDto;
import edu.sigmaportal.platform.exception.BadFileException;
import edu.sigmaportal.platform.exception.DependentEntityNotFoundException;
import edu.sigmaportal.platform.exception.EntityNotFoundException;
import edu.sigmaportal.platform.exception.SetupNotFinishedException;
import edu.sigmaportal.platform.model.FileModel;
import edu.sigmaportal.platform.model.FileTypeModel;
import edu.sigmaportal.platform.repository.FileRepository;
import edu.sigmaportal.platform.repository.FileTypeRepository;
import edu.sigmaportal.platform.service.FileService;
import edu.sigmaportal.platform.service.UserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class FileServiceImpl implements FileService {

    private final FileRepository files;
    private final FileTypeRepository fileTypes;
    private final UserService users;

    public FileServiceImpl(FileRepository files, FileTypeRepository fileTypes, UserService users) {
        this.files = files;
        this.fileTypes = fileTypes;
        this.users = users;
    }

    @Override
    public FileDto upload(String userId, MultipartFile file) {
        try {
            if (!users.exists(userId))
                throw new DependentEntityNotFoundException("User does not exist");
            int uid = strToUserId(userId);
            Path path = file.getOriginalFilename() != null ? Path.of(file.getOriginalFilename()) : null;
            String extension = getExtension(path);
            FileTypeModel type = getFileType(extension, file.getContentType());
            String filename = getFilename(type, path, extension);

            FileModel model = files.saveWithContents(new FileModel(0, filename, type.fileTypeId(), uid), file.getInputStream(), file.getSize());
            return new FileDto(idToStr(model.fileId()), model.filePath(), type.mimeType(), idToStr(model.ownerId()), null);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public FileDto upload(String userId, FileDto file) {
        if (!users.exists(userId))
            throw new DependentEntityNotFoundException("User does not exist");
        int uid = strToUserId(userId);
        Path path = file.filename != null ? Path.of(file.filename) : null;
        String extension = getExtension(path);
        FileTypeModel type = getFileType(extension, file.type);
        String filename = getFilename(type, path, extension);

        byte[] bytes = Base64.getDecoder().decode(file.content);
        FileModel saved = files.saveWithContents(new FileModel(0, filename, type.fileTypeId(), uid), new ByteArrayInputStream(bytes), bytes.length);
        return new FileDto(idToStr(saved.fileId()), saved.filePath(), type.mimeType(), idToStr(saved.ownerId()), null);
    }

    @Override
    public FileStream stream(String id) {
        FileModel file = files.findByIdWithContents(strToFileId(id)).orElseThrow(() -> new EntityNotFoundException("File not found"));
        FileTypeModel type = fileTypes.findById(file.fileTypeId()).orElseThrow(() -> new SetupNotFinishedException("Missing file type"));

        return new FileStreamImpl(file.stream(), type.mimeType(), file.filePath());
    }

    @Override
    public FileDto find(String id) {
        FileModel file = files.findById(strToFileId(id)).orElseThrow(() -> new EntityNotFoundException("File not found"));
        FileTypeModel type = fileTypes.findById(file.fileTypeId()).orElseThrow(() -> new SetupNotFinishedException("Missing file type"));
        return new FileDto(idToStr(file.fileId()), file.filePath(), type.mimeType(), idToStr(file.ownerId()), null);
    }

    @Override
    public FileDto update(String id, FileDto file) {
        int fid = strToFileId(id);
        FileModel existing = files.findById(fid).orElseThrow(() -> new EntityNotFoundException("File not found"));
        FileTypeModel fileType = updateFileType(existing, file.type);
        String filename = updateFilename(existing, file.filename);
        FileModel merged = new FileModel(existing.fileId(), filename, fileType.fileTypeId(), existing.ownerId());
        FileModel saved = files.save(merged);
        return new FileDto(idToStr(saved.fileId()), saved.filePath(), fileType.mimeType(), idToStr(saved.ownerId()), null);
    }

    @Override
    public void delete(String id) {
        int fid = strToFileId(id);
        FileModel model = files.findById(fid).orElseThrow(() -> new EntityNotFoundException("File not found"));
        files.delete(model);
    }

    @Override
    public boolean allExist(List<String> fileIds) {
        List<Integer> ids = fileIds.stream().map(this::strToFileId).toList();
        return files.countAllByFileIdIsIn(ids) == ids.size();
    }

    @Override
    public boolean owns(String userid, String fileId) {
        int uid = strToUserId(userid);
        int fid = strToFileId(fileId);
        return files.existsByOwnerIdAndFileId(uid, fid);
    }

    @Override
    public boolean ownsAll(String userId, List<String> fileIds) {
        int uid = strToUserId(userId);
        List<Integer> fids = fileIds.stream().map(this::strToFileId).toList();
        return files.countAllByFileIdIsInAndOwnerId(fids, uid) == fids.size();
    }

    @Override
    public Optional<String> connectedCourseId(String id) {
        int fid = strToFileId(id);
        return files.findConnectedCourseId(fid);
    }

    private String updateFilename(FileModel existing, String filename) {
        if (filename == null)
            return existing.filePath();

        Path path = Path.of(filename);
        return path.getFileName().toString();
    }

    private FileTypeModel updateFileType(FileModel existing, String fileType) {
        if (fileType == null)
            return fileTypes.findById(existing.fileTypeId()).orElseThrow(() -> new SetupNotFinishedException("Missing file type"));

        List<FileTypeModel> types = fileTypes.findAllByMimeType(fileType);
        if (types.isEmpty())
            throw new BadFileException("Invalid type");

        return types.get(0);
    }

    private Integer strToFileId(String id) {
        try {
            return Integer.parseInt(id);
        } catch (NumberFormatException e) {
            throw new EntityNotFoundException("Invalid fileId");
        }
    }

    private Integer strToUserId(String id) {
        try {
            return Integer.parseInt(id);
        } catch (NumberFormatException e) {
            throw new EntityNotFoundException("Invalid userId");
        }
    }

    private String getExtension(Path path) {
        if (path == null) return null;

        String[] parts = path.getFileName().toString().split("\\.");

        if (parts.length > 1) {
            return parts[1];
        }

        return null;
    }

    private FileTypeModel getFileType(String extension, String mimeType) {
        if (extension == null) {
            List<FileTypeModel> types = fileTypes.findAllByMimeType(mimeType);

            if (types.isEmpty()) throw new BadFileException("Invalid file type");
            return types.get(0);
        }

        List<FileTypeModel> types = fileTypes.findAllByMimeTypeOrExtension(mimeType, extension);

        if (types.isEmpty()) throw new BadFileException("Invalid file type");
        return types.get(0);
    }

    private String getFilename(FileTypeModel type, Path path, String extension) {
        if (path == null) {
            return RandomStringUtils.randomAlphanumeric(24) + "." + type.extension();
        }

        if (extension == null) {
            return path.getFileName().toString() + "." + type.extension();
        }

        return path.getFileName().toString();
    }

    private String idToStr(int id) {
        return Integer.toString(id);
    }

    public static class FileStreamImpl implements FileStream {
        private final InputStream stream;
        private final String mimeType;
        private final String filename;

        public FileStreamImpl(InputStream stream, String mimeType, String filename) {
            this.stream = stream;
            this.mimeType = mimeType;
            this.filename = filename;
        }

        @Override
        public InputStream handle() {
            return this.stream;
        }

        @Override
        public String mimeType() {
            return this.mimeType;
        }

        @Override
        public String filename() {
            return this.filename;
        }
    }
}
