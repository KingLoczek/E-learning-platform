package edu.sigmaportal.platform.service;

import edu.sigmaportal.platform.model.UserModel;
import edu.sigmaportal.platform.model.UserTypeModel;

/**
 * Helper service to manage user types
 */
public interface UserTypeService {
    /**
     * Getter for a user type assigned to freshly created users.
     * @return User type for freshly created users
     */
    UserTypeModel getFreshlyRegisteredUserType();

    /**
     * Getter for a user type assigned to students.
     * @return User type for students
     */
    UserTypeModel getStudentUserType();

    /**
     * Getter for a user type assigned to instructors.
     * @return User type for instructors
     */
    UserTypeModel getInstructorUserType();

    /**
     * Finds user's type.
     * @param user user object to find type for
     * @return User's user type
     */
    UserTypeModel getUsersType(UserModel user);

    /**
     * Finds user type based on name. <br/>
     *
     * Throws if user type not found.
     * @param user name of the user type
     * @return Found user type
     */
    UserTypeModel getUserType(String user);
}
