package finalproject.group1.BE.domain.services;

import finalproject.group1.BE.domain.entities.ProductFavoriteHistory;
import finalproject.group1.BE.domain.repository.ProductFavoriteHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ProductFavoriteHistoryService {
    private final ProductFavoriteHistoryRepository favoriteHistoryRepository;

    /**
     * save the addition or removal of product from user favorite to db
     * @param productId - product id
     * @param userId - user id
     * @param isFavorite - true if product was added to favorite , otherwise false
     */
    @Transactional
    public void save(int productId , int userId , boolean isFavorite){
        ProductFavoriteHistory favoriteHistory = new ProductFavoriteHistory();
        favoriteHistory.setProductId(productId);
        favoriteHistory.setUserId(userId);
        favoriteHistory.setDate(LocalDateTime.now());
        favoriteHistory.setFavorite(isFavorite);

        favoriteHistoryRepository.save(favoriteHistory);
    }
}
