package org.ilmi.eposkuserver.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ilmi.eposkuserver.domain.base.Aggregate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User extends Aggregate {
    private String username;
    private String fullName;

    private String password;
}
