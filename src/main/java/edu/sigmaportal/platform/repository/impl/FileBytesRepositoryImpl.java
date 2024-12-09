package edu.sigmaportal.platform.repository.impl;

import edu.sigmaportal.platform.exception.RepositoryException;
import edu.sigmaportal.platform.model.FileModel;
import edu.sigmaportal.platform.repository.FileBytesRepository;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class FileBytesRepositoryImpl implements FileBytesRepository {
    private final DataSource source;

    public FileBytesRepositoryImpl(DataSource source) {
        this.source = source;
    }

    @Override
    public FileModel saveWithContents(FileModel file, InputStream contents, long length) {
        try {
            Connection conn = source.getConnection();
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO \"FileStorage\" (file_path, file_type_id, bytes) VALUES (?, ?, ?) RETURNING file_id");
            stmt.setString(1, file.filePath());
            stmt.setInt(2, file.fileTypeId());
            if (length >= 0) stmt.setBinaryStream(3, contents, length);
            else stmt.setBinaryStream(3, contents);

            try (ResultSet results = stmt.executeQuery()) {
                if (!results.next()) throw new RepositoryException("File not inserted");

                int fileId = results.getInt(1);
                return new FileModel(fileId, file.filePath(), file.fileTypeId(), file.bytes());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<FileModel> findByIdWithContents(Integer id) {
        try {
            Connection conn = source.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT file_id, file_path, file_type_id, bytes FROM \"FileStorage\" WHERE file_id = ?");
            stmt.setInt(1, id);

            try (ResultSet results = stmt.executeQuery()) {
                if (!results.next()) return Optional.empty();

                int fileId = results.getInt(1);
                String filePath = results.getString(2);
                int fileTypeId = results.getInt(3);
                InputStream stream = results.getBinaryStream(4);

                if (results.next()) throw new RepositoryException("Multiple rows found, where only one was expected");

                return Optional.of(new FileModel(fileId, filePath, fileTypeId, stream));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
