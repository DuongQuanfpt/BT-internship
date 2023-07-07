package finalproject.group1.BE.domain.entities;

import finalproject.group1.BE.domain.enums.DeleteFlag;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Product_tbl")
@Data
public class Product {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    @Column(name = "sku", nullable = false, unique = true)
    private String sku;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "detail_info", nullable = false)
    private String detailInfo;

//    @Column(name = "category_id", nullable = false)
//    private int categoryId;

    @Column(name = "delete_flag", nullable = false)
    private DeleteFlag deleteFlag;
}
