package edu.sigmaportal.platform.service.impl;

import edu.sigmaportal.platform.dto.UserDto;
import edu.sigmaportal.platform.exception.EntityNotFoundException;
import edu.sigmaportal.platform.exception.NonUniqueKeyException;
import edu.sigmaportal.platform.model.UserModel;
import edu.sigmaportal.platform.model.UserTypeModel;
import edu.sigmaportal.platform.repository.UserRepository;
import edu.sigmaportal.platform.service.UserService;
import edu.sigmaportal.platform.service.UserTypeService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository users;
    private final UserTypeService userTypes;
    private final PasswordEncoder encoder;

    public UserServiceImpl(UserRepository users, UserTypeService userTypes, PasswordEncoder encoder) {
        this.users = users;
        this.userTypes = userTypes;
        this.encoder = encoder;
    }

    private String idToStr(UserModel user) {
        return Long.toString(user.userId());
    }

    private long strToId(String id) {
        try {
            return Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new EntityNotFoundException("User not found");
        }
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        if (users.existsByEmail(userDto.email))
            throw new NonUniqueKeyException("\"email\" must be unique");

        UserTypeModel type = userTypes.getFreshlyRegisteredUserType();
        String hashedPassword = encoder.encode(userDto.password);
        userDto.password = null;
        UserModel model = new UserModel(0, userDto.firstName, userDto.lastName, userDto.email, hashedPassword, type.id());
        UserModel saved = users.save(model);

        String pubId = Long.toString(saved.userId());

        return new UserDto(pubId, saved.firstName(), saved.lastName(), saved.email(), null, type.name());
    }

    @Override
    public UserDto updateUser(String id, UserDto user) {
        long userId = strToId(id);

        UserModel old = users.findById(userId).orElseThrow(() -> new EntityNotFoundException("User with id '" + id + "' does not exist"));

        String firstName = Objects.isNull(user.firstName) ? old.firstName() : user.firstName;
        String lastName = Objects.isNull(user.lastName) ? old.lastName() : user.lastName;
        String email = Objects.isNull(user.email) ? old.email() : user.email;
        long userTypeId = Objects.isNull(user.userType) ? old.userTypeId() : userTypes.getUserType(user.userType).id();

        UserModel merged = new UserModel(old.userId(), firstName, lastName, email, old.password(), userTypeId);
        UserModel saved = users.save(merged);
        return new UserDto(idToStr(saved), saved.firstName(), saved.lastName(), saved.email(), null, userTypes.getUsersType(saved).name());
    }

    @Override
    public void removeUser(String id) {
        long userId = strToId(id);
        if (!users.existsById(userId))
            throw new EntityNotFoundException("User with id '" + id + "' does not exist");
        users.deleteById(userId);
    }

    @Override
    public UserDto fetchUser(String id) {
        long userId = strToId(id);
        Optional<UserModel> model = users.findById(userId);
        UserModel fetched = model.orElseThrow(() -> new EntityNotFoundException("User with id '" + id + "' does not exist"));
        return new UserDto(idToStr(fetched), fetched.firstName(), fetched.lastName(), fetched.email(), null, userTypes.getUsersType(fetched).name());
    }

    @Override
    public Collection<UserDto> users() {
        List<UserDto> list = new ArrayList<>();
        for (UserModel user : users.findAll()) {
            list.add(new UserDto(idToStr(user), user.firstName(), user.lastName(), user.email(), null, userTypes.getUsersType(user).name()));
        }
        return list;
    }
}
