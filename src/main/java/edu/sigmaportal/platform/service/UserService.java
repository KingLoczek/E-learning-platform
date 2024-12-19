package edu.sigmaportal.platform.service;

import edu.sigmaportal.platform.dto.UserDto;

import java.util.Collection;

/**
 * Service for managing users.
 */
public interface UserService {
    /**
     * Creates a user. <br/>
     * Ignores {@code id} passed inside {@code user}, a fresh ID is generated.
     * Type of the user cannot be assigned on creation, created user will always have {@link UserTypeService#getFreshlyRegisteredUserType()}
     *
     * @param user user object to create
     * @return Created user
     */
    UserDto createUser(UserDto user);

    /**
     * Updates a user. <br/>
     * Ignores {@code id} passed inside {@code user}, {@code id} parameter is used instead.
     * To retain the old value pass a {@code null} in the respective field in {@code user}.
     * This method can change user type.
     *
     * @param id ID of the user to update
     * @param user user object to update values from
     * @return Updated user
     */
    UserDto updateUser(String id, UserDto user);

    /**
     * Deletes a user.
     * @param id ID of the user to delete
     */
    void removeUser(String id);

    /**
     * Finds a user. <br/>
     * Throws if a user is not found.
     * @param id ID of the user to find
     * @return Found user
     */
    UserDto fetchUser(String id);

    /**
     * Finds all users
     * @return collection of all registered users
     */
    Collection<UserDto> users();

    /**
     * Checks if a user exists.
     * @param userId ID of the user to check
     * @return {@code true} if there exists a user with ID equal to {@code topicId}, {@code false} otherwise
     */
    boolean exists(String userId);
}
