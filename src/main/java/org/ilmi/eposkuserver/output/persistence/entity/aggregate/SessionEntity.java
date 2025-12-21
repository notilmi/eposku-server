package org.ilmi.eposkuserver.output.persistence.entity.aggregate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jspecify.annotations.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table("sessions")
public class SessionEntity implements Persistable<@NonNull Long> {
    @Id @Column("id")
    private Long id;

    @Column("user_id")
    private Long userId;

    @Column("token")
    private String token;

    @Column("valid_through")
    private LocalDate validThrough = LocalDate.now().plusDays(1);

     @Override
    public boolean isNew() {
        return this.id == null;
    }
}
