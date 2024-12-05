package edu.sigmaportal.platform.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("UserType")
public record UserTypeModel(
        @Id @Column("user_type_id")
        long id,
        @Column("type_name")
        String name
) {
}
