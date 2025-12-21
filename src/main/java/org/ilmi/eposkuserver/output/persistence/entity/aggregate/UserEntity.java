package org.ilmi.eposkuserver.output.persistence.entity.aggregate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table("users")
public class UserEntity {
    @Id
    @Column("id")
    private Long id;

    @Column("username")
    private String username;

    @Column("full_name")
    private String fullName;

    @Column("password")
    private String password;
}
