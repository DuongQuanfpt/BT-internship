package finalproject.group1.BE.domain.repository;

import finalproject.group1.BE.domain.entities.Image;
import finalproject.group1.BE.domain.entities.ProductImg;
import finalproject.group1.BE.web.dto.data.image.ImageData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image,Integer> {
    @Query("Select i.path as path ," +
            "      i.name as name " +
            "from Image i  " +
            "where i.thumbnailFlag = finalproject.group1.BE.domain.enums.ThumbnailFlag.YES and " +
            "      i.productImg.product.id = :productId")
    ImageData findProductThumbnail(@Param("productId") int productId);

    @Query("Select i.path as path ," +
            "      i.name as name " +
            "from Image i  " +
            "where i.thumbnailFlag = finalproject.group1.BE.domain.enums.ThumbnailFlag.NO and " +
            "      i.productImg.product.id = :productId")
    List<ImageData> findDetailImages(@Param("productId") int productId);
}
