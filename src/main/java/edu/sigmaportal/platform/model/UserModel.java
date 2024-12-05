package edu.sigmaportal.platform.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("Users")
public record UserModel(
        @Id
        long userId,
        String firstName,
        String lastName,
        String email,
        String password,
        long userTypeId
) {
}
