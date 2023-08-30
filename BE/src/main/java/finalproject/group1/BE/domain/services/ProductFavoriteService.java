package finalproject.group1.BE.domain.services;

import finalproject.group1.BE.domain.embedded.FavoriteProductId;
import finalproject.group1.BE.domain.entities.ProductFavorite;
import finalproject.group1.BE.domain.repository.ProductFavoriteRepository;
import finalproject.group1.BE.domain.repository.ProductRepository;
import finalproject.group1.BE.web.exception.IllegalArgumentException;
import finalproject.group1.BE.web.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductFavoriteService {

    private final ProductFavoriteRepository favoriteProductRepository;

    private final ProductRepository productRepository;

    private final ProductFavoriteHistoryService favoriteProductHistoryService;

    /**
     * save favorite product to database
     * @param userId - login user id
     * @param productId - id of product that user flag as favorite
     */
    @Transactional
    public void save(int userId, int productId) {
        if(!productRepository.findByIdNotDeleted(productId).isPresent()){
            throw new NotFoundException("Product not found");
        }

        FavoriteProductId id = new FavoriteProductId();
        id.setUserId(userId);
        id.setProductId(productId);

        if(favoriteProductRepository.existsById(id)){
            throw new IllegalArgumentException("product is already added to favorite");
        }
        ProductFavorite favoriteProduct = new ProductFavorite(id);
        favoriteProductRepository.save(favoriteProduct);
        favoriteProductHistoryService.save(productId,userId,true);

    }

    /**
     * remove favorite product from database
     * @param userId
     * @param productId
     */
    @Transactional
    public void delete(int userId, int productId){
        if(!productRepository.findByIdNotDeleted(productId).isPresent()){
            throw new NotFoundException("Product not found");
        }

        FavoriteProductId id = new FavoriteProductId();
        id.setUserId(userId);
        id.setProductId(productId);
        ProductFavorite productFavorite = favoriteProductRepository.findById(id).
                orElseThrow(() -> new NotFoundException("product not in favorite"));
        favoriteProductRepository.delete(productFavorite);
        favoriteProductHistoryService.save(productId,userId,false);
    }
}
