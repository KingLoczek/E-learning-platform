package edu.sigmaportal.platform.service;

import edu.sigmaportal.platform.model.UserTypeModel;

public interface UserTypeService {

    UserTypeModel getFreshlyRegisteredUserType();
    UserTypeModel getStudentUserType();
    UserTypeModel getInstructorUserType();
}
