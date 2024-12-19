package edu.sigmaportal.platform.service;

import edu.sigmaportal.platform.dto.FileDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Service for managing files.
 * <p>
 * <b>NOTE:</b> Methods in this service do not send file content by default.
 */
public interface FileService {
    /**
     * Upload a file using {@code MultipartFile}. <br/>
     * File type is determined based on content type and the extension of filename.
     * This method can use the streaming interface provided by {@code MultipartFile}.
     *
     * @param userId ID of the user that uploads a file
     * @param file multipart file that is being uploaded
     * @return Uploaded file
     */
    FileDto upload(String userId, MultipartFile file);

    /**
     * Upload a file using {@code FileDto}. <br/>
     * File type is determined based on content type and the extension of filename.
     * This method can be less efficient, because it has to decode the Base64 encoded contents to bytes.
     *
     * @param userId ID of the user that uploads a file
     * @param file multipart file that is being uploaded
     * @return Uploaded file
     */
    FileDto upload(String userId, FileDto file);

    /**
     * Find a file and stream its contents.
     *
     * @param id ID of the file to find
     * @return file stream
     */
    FileStream stream(String id);

    /**
     * Finds a file. <br>
     *
     * Throws if a file is not found.
     * @param id ID of the file to find
     * @return Found file
     */
    FileDto find(String id);

    /**
     * Updates a file's metadata. <br>
     * Ignores {@code id} passed inside {@code file}, {@code id} parameter is used instead.
     * ID will not and cannot change as a result of this operation.
     * To retain the old value pass a {@code null} in the respective field in {@code file}.
     * <p>
     * <b>Cannot be used to update file contents!</b>
     *
     * @param id ID of the file to update
     * @param file file object to update values from
     * @return Updated file metadata
     */
    FileDto update(String id, FileDto file);

    /**
     * Deletes a file.
     * @param id ID of the file to delete.
     */
    void delete(String id);

    /**
     * Checks existence of many files.
     * @param fileIds list of file IDs to check
     * @return {@code true} if all files passed in {@code fileIds} exist, {@code false} otherwise
     */
    boolean allExist(List<String> fileIds);

    /**
     * Checks if a user owns a file.
     * @param userId ID of the user to check
     * @param fileId ID of the file to check
     * @return {@code true} if file's owner ID equals {@code userId}, {@code false} otherwise
     */
    boolean owns(String userId, String fileId);

    /**
     * Checks if a user owns many files.
     * @param userId ID of the user to check
     * @param fileIds list of file IDs to check
     * @return {@code true} if the owner ID of all files in the list equals {@code userId}, {@code false} otherwise
     */
    boolean ownsAll(String userId, List<String> fileIds);

    /**
     * Finds the ID of a course that has a connection to this file, be it through assignment, material or submission.
     * @param id ID of the file to check
     * @return Course ID if a connection exists
     */
    Optional<String> connectedCourseId(String id);

    /**
     * File {@link InputStream} along with some metadata about it.
     */
    interface FileStream {
        /**
         * File stream getter.
         * @return stream of file contents
         */
        InputStream handle();

        /**
         * mime-type getter.
         * @return file's mime-type
         */
        String mimeType();

        /**
         * Filename getter.
         * @return file's filename
         */
        String filename();
    }
}
