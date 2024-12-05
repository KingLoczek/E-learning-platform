package edu.sigmaportal.platform.service.impl;

import edu.sigmaportal.platform.dto.UserDto;
import edu.sigmaportal.platform.model.UserModel;
import edu.sigmaportal.platform.model.UserTypeModel;
import edu.sigmaportal.platform.repository.UserRepository;
import edu.sigmaportal.platform.service.UserService;
import edu.sigmaportal.platform.service.UserTypeService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    @Override
    public UserDto createUser(UserDto userDto) {
        UserTypeModel type = userTypes.getFreshlyRegisteredUserType();
        String hashedPassword = encoder.encode(userDto.password);
        userDto.password = null;
        UserModel model = new UserModel(0, userDto.firstName, userDto.lastName, userDto.email, hashedPassword, type.id());
        UserModel saved = users.save(model);

        String pubId = Long.toString(saved.userId());

        return new UserDto(pubId, saved.firstName(), saved.lastName(), saved.email(), null, type.name());
    }
}
