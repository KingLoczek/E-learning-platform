package edu.sigmaportal.platform.service;

import edu.sigmaportal.platform.model.UserModel;
import edu.sigmaportal.platform.model.UserTypeModel;

public interface UserTypeService {

    UserTypeModel getFreshlyRegisteredUserType();
    UserTypeModel getStudentUserType();
    UserTypeModel getInstructorUserType();

    UserTypeModel getUsersType(UserModel user);

    UserTypeModel getUserType(String user);
}
