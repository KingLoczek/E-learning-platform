package edu.sigmaportal.platform.repository;

import edu.sigmaportal.platform.model.UserModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserModel, Long> {
}
