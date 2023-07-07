package finalproject.group1.BE.domain.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ProductImg_tbl")
@Data
public class ProductImg {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

//    @Column(name = "product_id", nullable = false)
//    private int productId;
//
//    @Column(name = "img_id", nullable = false)
//    private int imgId;
}
