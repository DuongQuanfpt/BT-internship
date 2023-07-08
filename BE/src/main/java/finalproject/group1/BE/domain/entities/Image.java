package finalproject.group1.BE.domain.entities;

import finalproject.group1.BE.domain.enums.ThumbnailFlag;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Image_tbl")
@Data
public class Image {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    @Column(name = "path", nullable = false)
    private String path;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "thumbnail_flag", nullable = false)
    private ThumbnailFlag thumbnailFlag;

    @OneToOne(mappedBy = "image")
    private CategoryImg categoryImg;

    @OneToOne(mappedBy = "image")
    private ProductImg productImg;
}
