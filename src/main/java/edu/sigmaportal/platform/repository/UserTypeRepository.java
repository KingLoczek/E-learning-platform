package edu.sigmaportal.platform.repository;

import edu.sigmaportal.platform.model.UserTypeModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTypeRepository extends CrudRepository<UserTypeModel, Long> {

    UserTypeModel findByName(String typeName);
}
