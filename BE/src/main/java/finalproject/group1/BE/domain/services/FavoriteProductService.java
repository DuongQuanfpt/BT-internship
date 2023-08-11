package finalproject.group1.BE.domain.services;

import finalproject.group1.BE.domain.embedded.FavoriteProductId;
import finalproject.group1.BE.domain.entities.FavoriteProduct;
import finalproject.group1.BE.domain.repository.FavoriteProductRepository;
import finalproject.group1.BE.domain.repository.ProductRepository;
import finalproject.group1.BE.web.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FavoriteProductService {

    private final FavoriteProductRepository favoriteProductRepository;

    private final ProductRepository productRepository;

    /**
     * save favorite product to database
     * @param userId - login user id
     * @param productId - id of product that user flag as favorite
     */
    public void save(int userId, int productId) {
        productRepository.findByIdNotDeleted(productId)
                .orElseThrow(() -> new NotFoundException("Product not found"));

        FavoriteProductId id = new FavoriteProductId();
        id.setUserId(userId);
        id.setProductId(productId);

        FavoriteProduct favoriteProduct = new FavoriteProduct(id);
        favoriteProductRepository.save(favoriteProduct);
    }
}
