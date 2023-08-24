package finalproject.group1.BE.domain.services;

import finalproject.group1.BE.domain.entities.ProductView;
import finalproject.group1.BE.domain.repository.ProductViewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ProductViewService {
    private final ProductViewRepository productViewRepository;

    /**
     * save product view  to db
     * @param productId - product id
     * @param userId - id of user , can be null
     */
    @Transactional
    public void save(int productId, Integer userId) {
        ProductView productView = new ProductView();
        productView.setProductId(productId);
        if(userId != null){
            productView.setUserId(userId);
        }
        productView.setDate(LocalDateTime.now());

        productViewRepository.save(productView);
    }
}
