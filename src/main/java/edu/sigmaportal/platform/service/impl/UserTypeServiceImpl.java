package edu.sigmaportal.platform.service.impl;

import edu.sigmaportal.platform.exception.SetupNotFinishedException;
import edu.sigmaportal.platform.model.UserTypeModel;
import edu.sigmaportal.platform.repository.UserTypeRepository;
import edu.sigmaportal.platform.service.UserTypeService;
import org.springframework.stereotype.Service;

@Service
public class UserTypeServiceImpl implements UserTypeService {

    private final UserTypeRepository repo;

    public UserTypeServiceImpl(UserTypeRepository repo) {
        this.repo = repo;
    }

    private UserTypeModel getUserType(String name) {
        UserTypeModel model = repo.findByName(name);
        if (model == null) throw new SetupNotFinishedException("Missing required UserType");
        return model;    }

    @Override
    public UserTypeModel getFreshlyRegisteredUserType() {
        return getUserType("user");
    }

    @Override
    public UserTypeModel getStudentUserType() {
        return getUserType("student");
    }

    @Override
    public UserTypeModel getInstructorUserType() {
        return getUserType("instructor");
    }
}
