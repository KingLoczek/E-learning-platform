package edu.sigmaportal.platform.service;

import edu.sigmaportal.platform.dto.UserDto;

import java.util.Collection;

public interface UserService {
    UserDto createUser(UserDto userDto);
    UserDto updateUser(String id, UserDto userDto);
    void removeUser(String id);
    UserDto fetchUser(String id);
    Collection<UserDto> users();
}
