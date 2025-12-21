package org.ilmi.eposkuserver.domain.base;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public abstract class Aggregate {
    // change id from String to Long
    private Long id;
}
