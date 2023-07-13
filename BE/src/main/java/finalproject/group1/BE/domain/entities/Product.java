package finalproject.group1.BE.domain.entities;

import finalproject.group1.BE.domain.enums.DeleteFlag;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Product_tbl")
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "sku", nullable = false, unique = true,length = 50)
    private String sku;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "detail_info", nullable = false,length = 1001)
    private String detailInfo;

    @Column(name = "delete_flag", nullable = false)
    private DeleteFlag deleteFlag;

    @Column(name = "price", nullable = false)
    private Float price;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

//    @OneToMany(mappedBy = "product")
//    private List<CartDetail> cartDetails = new ArrayList<>();
//
//    @OneToMany(mappedBy = "product")
//    private List<OrderDetail> orderDetails = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImg> productImgs = new ArrayList<>();

}
