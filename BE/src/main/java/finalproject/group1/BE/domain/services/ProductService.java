package finalproject.group1.BE.domain.services;

import com.google.common.io.Files;
import finalproject.group1.BE.constant.Constants;
import finalproject.group1.BE.domain.entities.Category;
import finalproject.group1.BE.domain.entities.Image;
import finalproject.group1.BE.domain.entities.Product;
import finalproject.group1.BE.domain.entities.ProductImg;
import finalproject.group1.BE.domain.enums.DeleteFlag;
import finalproject.group1.BE.domain.enums.ThumbnailFlag;
import finalproject.group1.BE.domain.repository.CategoryRepository;
import finalproject.group1.BE.domain.repository.ProductRepository;
import finalproject.group1.BE.web.dto.request.product.ProductListRequest;
import finalproject.group1.BE.web.dto.request.product.ProductRequest;
import finalproject.group1.BE.web.dto.response.product.ProductListResponse;
import finalproject.group1.BE.web.exception.ExistException;
import finalproject.group1.BE.web.exception.NotFoundException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductService {

    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;
    private ModelMapper modelMapper;


    public List<ProductListResponse> getProductList(ProductListRequest listRequest, Pageable pageable) {

        Integer categoryId = null;
        String sku = null;
        String name = null;

        if (listRequest.getCategory() != null) {
            categoryId = listRequest.getCategory().getId();
        }

        if (listRequest.getSku() != null && !listRequest.getSku().isEmpty()) {
            sku = listRequest.getSku();
        }

        if (listRequest.getName() != null && !listRequest.getName().isEmpty()) {
            name = listRequest.getName();
        }

        List<ProductListResponse> productListResponses =  productRepository.searchProductByConditions(categoryId, sku, name, pageable);
        // Print the returned data
        for (ProductListResponse response : productListResponses) {
            System.out.println(response);
        }

        return productListResponses;
    }


    public ProductListResponse getProductDetails(String sku) {
        Optional<Product> productDetail = Optional.ofNullable(productRepository.findBySku(sku).orElseThrow(() -> {
            throw new NotFoundException("Product not found with SKU: " + sku);
        }));
        ProductListResponse productDetailsDTO = new ProductListResponse();
        productDetailsDTO.setId(productDetail.get().getId());
        productDetailsDTO.setSku(productDetail.get().getSku());
        productDetailsDTO.setName(productDetail.get().getName());
        productDetailsDTO.setDetailInfo(productDetail.get().getDetailInfo());
        productDetailsDTO.setPrice(productDetail.get().getPrice());

        List<String> imageNames = productDetail.get().getProductImgs().stream()
                .map(productImg -> productImg.getImage().getName())
                .collect(Collectors.toList());
        productDetailsDTO.setImageName(imageNames);

        List<String> imagePaths = productDetail.get().getProductImgs().stream()
                .map(productImg -> productImg.getImage().getPath())
                .collect(Collectors.toList());
        productDetailsDTO.setImagePath(imagePaths);

        modelMapper.map(productDetail, ProductListResponse.class);
        return productDetailsDTO;
    }


    public void update(int id, ProductRequest updateRequest) {
        Product product = productRepository.findById(id).orElseThrow(() -> {
            throw new NotFoundException("Product Not Found");
        });
        save(updateRequest, product);
    }

    public void save(ProductRequest request, Product product) {
        Product existProduct = productRepository.findBySku(request.getSku()).orElseThrow(() -> {
            throw new NotFoundException("Product doesn't exist");
        });
        if (existProduct.getId() != product.getId()) {
            System.out.println("product sku exist");
            throw new ExistException();
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
            //delete existing image
            deleteFile(productImgs);
            productImgs.clear();
        }

        //add detail images to product
        Product finalProduct = product;
        AtomicInteger count = new AtomicInteger(1);
        productImgs.addAll(request.getDetailImage().stream().map(multipartFile -> {
            ProductImg productImg = new ProductImg();

            Image image = createImageFromMultipartFile(multipartFile
                    , false, product.getSku() + "_detail_" + count);
            productImg.setProduct(finalProduct);
            productImg.setImage(image);

            count.getAndIncrement();
            return productImg;
        }).collect(Collectors.toList()));

        //add thumbnail image to product
        ProductImg thumbnailImg = new ProductImg();

        Image image = createImageFromMultipartFile(request.getThumbnailImage()
                , true, product.getSku() + "_thumbnail");
        thumbnailImg.setProduct(product);
        thumbnailImg.setImage(image);

        productImgs.add(thumbnailImg);
        product.setProductImgs(productImgs);

        productRepository.save(product);
    }

    /**
     * save the multipart file at the image folder(define in Constants file) and
     * create an image entity
     *
     * @param multipartFile - the multipart file
     * @param isThumbnail   - whether image is thumbnail
     * @return an image entity
     * @throws IOException
     */
    private Image createImageFromMultipartFile(MultipartFile multipartFile, boolean isThumbnail, String fileName) {
        String extension = Files.getFileExtension(multipartFile.getOriginalFilename());
        File imgFile = new File(Constants.IMAGE_FOLDER_PATH + fileName + "." + extension);

        try (OutputStream os = new FileOutputStream(imgFile)) {
            os.write(multipartFile.getBytes());
            Image image = new Image();
            image.setName(multipartFile.getOriginalFilename());
            image.setPath(imgFile.getPath());
            image.setThumbnailFlag(ThumbnailFlag.getThumbnailFlag(isThumbnail));

            return image;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteFile(List<ProductImg> productImgs) {
        productImgs.stream().forEach(productImg -> {
            File file = new File(productImg.getImage().getPath());
            file.delete();
        });
    }
}
