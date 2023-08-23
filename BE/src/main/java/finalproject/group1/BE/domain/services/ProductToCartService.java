package finalproject.group1.BE.domain.services;

import finalproject.group1.BE.domain.entities.ProductToCart;
import finalproject.group1.BE.domain.repository.ProductToCartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ProductToCartService {
    private final ProductToCartRepository toCartRepository;

    /**
     * save the addition of product to cart to db
     * @param productId - id of the product
     * @param quantity - product quantity
     */
    @Transactional
    public void save(int productId, int quantity) {
        ProductToCart toCart = new ProductToCart();
        toCart.setProductId(productId);
        toCart.setQuantity(quantity);
        toCart.setDate(LocalDateTime.now());

        toCartRepository.save(toCart);
    }
}
