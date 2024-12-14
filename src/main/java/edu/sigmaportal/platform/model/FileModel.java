package edu.sigmaportal.platform.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Objects;

@Table("FileStorage")
public final class FileModel {
    private final int fileId;
    private final String filePath;
    private final int fileTypeId;
    private final int ownerId;
    @Transient
    private final byte[] bytes;
    @Transient
    private final InputStream stream;

    @PersistenceCreator
    public FileModel(int fileId, String filePath, int fileTypeId, int ownerId) {
        this(fileId, filePath, fileTypeId, ownerId, null, InputStream.nullInputStream());
    }

    public FileModel(int fileId, String filePath, int fileTypeId, int ownerId, byte[] bytes) {
        this(fileId, filePath, fileTypeId, ownerId, bytes, Objects.isNull(bytes) ? InputStream.nullInputStream() : new ByteArrayInputStream(bytes));
    }

    public FileModel(int fileId, String filePath, int fileTypeId, int ownerId, InputStream stream) {
        this(fileId, filePath, fileTypeId, ownerId, null, stream);
    }

    private FileModel(int fileId, String filePath, int fileTypeId, int ownerId, byte[] bytes, InputStream stream) {
        this.fileId = fileId;
        this.filePath = filePath;
        this.fileTypeId = fileTypeId;
        this.ownerId = ownerId;
        this.bytes = bytes;
        this.stream = stream;
    }

    @Id
    public int fileId() {
        return fileId;
    }

    public String filePath() {
        return filePath;
    }

    public int fileTypeId() {
        return fileTypeId;
    }

    public int ownerId() {
        return ownerId;
    }

    @Transient
    public byte[] bytes() {
        return bytes;
    }

    @Transient
    public InputStream stream() {
        return stream;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (FileModel) obj;
        return this.fileId == that.fileId &&
                Objects.equals(this.filePath, that.filePath) &&
                this.fileTypeId == that.fileTypeId &&
                Objects.equals(this.bytes, that.bytes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileId, filePath, fileTypeId, bytes);
    }

    @Override
    public String toString() {
        return "FileModel[" +
                "fileId=" + fileId + ", " +
                "filePath=" + filePath + ", " +
                "fileTypeId=" + fileTypeId + ", " +
                "bytes=" + bytes + ']';
    }
}
