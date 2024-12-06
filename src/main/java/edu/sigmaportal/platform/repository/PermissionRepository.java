package edu.sigmaportal.platform.repository;

import edu.sigmaportal.platform.model.PermissionModel;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface PermissionRepository extends Repository<PermissionModel, Long> {
    @Query("SELECT * FROM \"Permissions\" AS p INNER JOIN \"UserTypePermissions\" AS utp USING (permission_id) WHERE utp.user_type_id = :userTypeId")
    List<PermissionModel> findAllByUserTypeId(long userTypeId);
}
