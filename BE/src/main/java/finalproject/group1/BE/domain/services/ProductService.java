package finalproject.group1.BE.domain.services;

import finalproject.group1.BE.commons.Constants;
import finalproject.group1.BE.commons.EmailCommons;
import finalproject.group1.BE.commons.FileCommons;
import finalproject.group1.BE.commons.GoogleDriveCommons;
import finalproject.group1.BE.domain.entities.*;
import finalproject.group1.BE.domain.enums.DeleteFlag;
import finalproject.group1.BE.domain.enums.ThumbnailFlag;
import finalproject.group1.BE.domain.repository.*;
import finalproject.group1.BE.web.dto.data.image.ImageData;
import finalproject.group1.BE.web.dto.request.product.ProductListRequest;
import finalproject.group1.BE.web.dto.request.product.ProductRequest;
import finalproject.group1.BE.web.dto.response.PageableDTO;
import finalproject.group1.BE.web.dto.response.product.ProductDetailResponse;
import finalproject.group1.BE.web.dto.response.product.ProductImageResponse;
import finalproject.group1.BE.web.dto.response.product.ProductListResponse;
import finalproject.group1.BE.web.dto.response.product.ProductResponse;
import finalproject.group1.BE.web.exception.ExistException;
import finalproject.group1.BE.web.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ImageRepository imageRepository;
    private final ModelMapper modelMapper;
    private final GoogleDriveCommons googleDriveCommons;
    private final FavoriteProductRepository favoriteProductRepository;
    private final UserRepository userRepository;
    private final EmailCommons emailCommons;

    @Value("${drive.upload.product}")
    private String driveProductDirectory;

    public ProductListResponse getProductList(ProductListRequest listRequest, Pageable pageable) {

        Integer categoryId = null;
        String searchKey = null;
        ProductListResponse productListResponse = new ProductListResponse();
        PageableDTO pageableDTO = new PageableDTO();

        if (listRequest.getCategoryId() != null) {
            categoryId = listRequest.getCategoryId();
        }

        if (listRequest.getSearchKey() != null && !listRequest.getSearchKey().isEmpty()) {
            searchKey = listRequest.getSearchKey();
        }

        Page<Product> results = productRepository.searchProductByConditions(categoryId, searchKey, pageable);
        pageableDTO.setTotalPages(results.getTotalPages());
        pageableDTO.setPageSize(results.getSize());
        pageableDTO.setPageNumber(results.getNumber());

        List<ProductResponse> productResponse = results.stream()
                .map(product -> {
                    ProductResponse response = modelMapper.map(product, ProductResponse.class);

                    ImageData imageData = imageRepository.findProductThumbnail(product.getId());
                    response.setImageName(imageData.getName());
                    response.setImagePath(imageData.getPath());
                    return response;
                }).collect(Collectors.toList());

        productListResponse.setProductResponses(productResponse);
        productListResponse.setPageableDTO(pageableDTO);
        return productListResponse;

    }


    public ProductDetailResponse getProductDetails(String sku) {
        Optional<Product> productDetail = Optional.ofNullable(productRepository.findBySku(sku).orElseThrow(() -> {
            throw new NotFoundException("Product not found with SKU: " + sku);
        }));

        List<ImageData> imagesData = imageRepository.findDetailImages(productDetail.get().getId());
        ProductImageResponse productImageResponse = new ProductImageResponse();
        productImageResponse.setName(imagesData.stream().map(imageData -> imageData.getName()).collect(Collectors.toList()));
        productImageResponse.setPath(imagesData.stream().map(imageData -> imageData.getPath()).collect(Collectors.toList()));

        ProductDetailResponse productDetailsDTO = new ProductDetailResponse();
        productDetailsDTO.setId(productDetail.get().getId());
        productDetailsDTO.setSku(productDetail.get().getSku());
        productDetailsDTO.setName(productDetail.get().getName());
        productDetailsDTO.setDetailInfo(productDetail.get().getDetailInfo());
        productDetailsDTO.setPrice(productDetail.get().getPrice());
        productDetailsDTO.setImages(productImageResponse);

        return productDetailsDTO;
    }

    public void update(int id, ProductRequest updateRequest) {
        Product product = productRepository.findById(id).orElseThrow(() -> {
            throw new NotFoundException("Product Not Found");
        });
        Product updatedProduct = save(updateRequest, product);

        //send email to users that flag this product as favorite
        List<FavoriteProduct> favoriteProducts = favoriteProductRepository.findByIdProductId(id);
        String[] userEmails = favoriteProducts.stream().map(favoriteProduct -> {
            User user = userRepository.findById(favoriteProduct.getId().getUserId())
                    .orElseThrow(() -> new NotFoundException("user not found"));
            return user.getEmail();
        }).toArray(String[]::new);

        String content = String.format(Constants.FAVORITE_PRODUCT_UPDATE_CONTENT,updatedProduct.getSku());
        emailCommons.sendSimpleMessage2(userEmails, Constants.FAVORITE_PRODUCT_UPDATE_SUBJECT,content);
    }

    @Transactional
    public Product save(ProductRequest request, Product product) {
        Product existProduct = productRepository.findBySku(request.getSku()).orElse(null);
        if (existProduct != null && existProduct.getId() != product.getId()) {

            throw new ExistException("Product with that sku already exist");
        }
        Category category = categoryRepository.findById(request.getCategory_id())
                .orElseThrow(() -> new NotFoundException("Category Not Found"));

        product.setSku(request.getSku());
        product.setName(request.getName());
        product.setPrice(request.getPrice().floatValue());
        product.setDetailInfo(request.getDetail_info());
        product.setCategory(category);
        product.setDeleteFlag(DeleteFlag.NORMAL);

        List<ProductImg> productImgs = product.getProductImgs();
        //check if product has images
        if (!productImgs.isEmpty()) {
            productImgs.forEach(productImg ->
                    googleDriveCommons.deleteFileOrFolder(productImg.getImage().getPath()));
            productImgs.clear();
        }

        //add detail images to product
        Product finalProduct = product;
        AtomicInteger count = new AtomicInteger(1);
        productImgs.addAll(request.getDetailImage().stream().map(multipartFile -> {
            ProductImg productImg = new ProductImg();

            Image image = new Image();
            image.setName(multipartFile.getOriginalFilename());
            image.setPath(googleDriveCommons.uploadFile(multipartFile, driveProductDirectory));
            image.setThumbnailFlag(ThumbnailFlag.NO);

            productImg.setProduct(finalProduct);
            productImg.setImage(image);

            count.getAndIncrement();
            return productImg;
        }).collect(Collectors.toList()));

        //add thumbnail image to product
        ProductImg thumbnailImg = new ProductImg();

        Image image = new Image();
        image.setName(request.getThumbnailImage().getOriginalFilename());
        image.setPath(googleDriveCommons.uploadFile(request.getThumbnailImage(), driveProductDirectory));
        image.setThumbnailFlag(ThumbnailFlag.YES);

        thumbnailImg.setProduct(product);
        thumbnailImg.setImage(image);

        productImgs.add(thumbnailImg);
        product.setProductImgs(productImgs);
        // save changes to db
        return productRepository.save(product);
    }

    /**
     * set product delete flag to 1 (DeleteFlag.DELETED)
     *
     * @param id - product id
     */
    @Transactional
    public void delete(int id) {
        Product product = productRepository.findById(id).orElse(null);
        // if product exist and not deleted(== DeleteFlag.NORMAL)
        if (product != null && product.getDeleteFlag() != DeleteFlag.DELETED) {
            //update product data
            product.setOldSku(product.getSku());
            product.setSku(null);
            product.setDeleteFlag(DeleteFlag.DELETED);

            //delete product images
            List<ProductImg> productImgs = product.getProductImgs();
            productImgs.stream().forEach(productImg -> {
                //delete images in server
                googleDriveCommons.deleteFileOrFolder(productImg.getImage().getPath());
            });
            product.getProductImgs().clear();

            //save changes to db
            Product deletedProduct = productRepository.save(product);

            //delete data in favorite product table
            List<FavoriteProduct> favoriteProducts = favoriteProductRepository.
                    findByIdProductId(deletedProduct.getId());
            String[] userEmails = favoriteProducts.stream().map(favoriteProduct -> {
                User user = userRepository.findById(favoriteProduct.getId().getUserId())
                        .orElseThrow(() -> new NotFoundException("user not found"));
                return user.getEmail();
            }).toArray(String[]::new);

            favoriteProductRepository.deleteAll(favoriteProducts);
            //send mail to user
            String content = String.format(Constants.FAVORITE_PRODUCT_DELETE_CONTENT,deletedProduct.getOldSku());
            emailCommons.sendSimpleMessage2(userEmails,Constants.FAVORITE_PRODUCT_DElETE_SUBJECT,content);
        }
    }
}
