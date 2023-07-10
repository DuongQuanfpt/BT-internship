package finalproject.group1.BE.domain.services;

import finalproject.group1.BE.constant.Constants;
import finalproject.group1.BE.domain.entities.Category;
import finalproject.group1.BE.domain.entities.Image;
import finalproject.group1.BE.domain.entities.Product;
import finalproject.group1.BE.domain.entities.ProductImg;
import finalproject.group1.BE.domain.enums.DeleteFlag;
import finalproject.group1.BE.domain.enums.ThumbnailFlag;
import finalproject.group1.BE.domain.repository.CategoryRepository;
import finalproject.group1.BE.domain.repository.ProductRepository;
import finalproject.group1.BE.web.dto.request.AddProductRequest;
import finalproject.group1.BE.web.exception.ExistException;
import finalproject.group1.BE.web.exception.NotFoundException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductService {

    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;
    private ModelMapper modelMapper;

    public void addUser(AddProductRequest request) {
        if(productRepository.findBySku(request.getSku()).isPresent()){
            throw new ExistException();
        }
        Category category = categoryRepository.findById(request.getCategory_id())
                .orElseThrow(() -> new NotFoundException());

        Product product = modelMapper.map(request, Product.class);
        product.setCategory(category);
        product.setDeleteFlag(DeleteFlag.NORMAL);

        List<ProductImg> productImgs;
        //add detail images to product
        productImgs = request.getDetailImage().stream().map(multipartFile -> {
            ProductImg productImg = new ProductImg();

            Image image = createImageFromMultipartFile(multipartFile);
            image.setThumbnailFlag(ThumbnailFlag.NO);

            productImg.setProduct(product);
            productImg.setImage(image);

            return productImg;
        }).collect(Collectors.toList());

        //add thumbnail image to product
        ProductImg thumbnailImg = new ProductImg();
        Image image = createImageFromMultipartFile(request.getThumbnailImage());
        image.setThumbnailFlag(ThumbnailFlag.YES);
        thumbnailImg.setProduct(product);
        thumbnailImg.setImage(image);

        productImgs.add(thumbnailImg);
        product.setProductImgs(productImgs);

        productRepository.save(product);
    }

    /**
     * save the multipart file at the image folder(define in Constants file) and
     * create an image entity
     * @param imgfile - the multipart file
     * @return an image entity
     * @throws IOException
     */
    private Image createImageFromMultipartFile(MultipartFile imgfile){
        File imgFile = new File(Constants.IMAGE_FOLDER_PATH +imgfile.getOriginalFilename());
        try(OutputStream os = new FileOutputStream(imgFile)){
            os.write(imgfile.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Image image = new Image();
        image.setName(imgFile.getName());
        image.setPath(imgFile.getPath());

        return image;
    }
}
