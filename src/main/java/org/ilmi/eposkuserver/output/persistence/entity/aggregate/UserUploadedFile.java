package org.ilmi.eposkuserver.output.persistence.entity.aggregate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("user_uploaded_file")
public class UserUploadedFile {
    @Id
    private Long id;

    @Column("image_provider")
    private String provider;

    @Column("image_url")
    private String url;
}
