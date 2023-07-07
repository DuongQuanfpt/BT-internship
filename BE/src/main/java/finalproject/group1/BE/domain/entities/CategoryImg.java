package finalproject.group1.BE.domain.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CategoryImg_tbl")
@Data
public class CategoryImg {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

//    @Column(name = "category_id", nullable = false)
//    private int categoryId;
//
//    @Column(name = "img_id", nullable = false)
//    private int imgId;
}
