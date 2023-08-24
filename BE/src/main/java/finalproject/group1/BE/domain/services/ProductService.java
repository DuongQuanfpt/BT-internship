package finalproject.group1.BE.domain.services;

import com.google.api.services.drive.model.File;
import finalproject.group1.BE.commons.Constants;
import finalproject.group1.BE.commons.EmailCommons;
import finalproject.group1.BE.commons.GoogleDriveCommons;
import finalproject.group1.BE.domain.entities.*;
import finalproject.group1.BE.domain.enums.DeleteFlag;
import finalproject.group1.BE.domain.enums.ThumbnailFlag;
import finalproject.group1.BE.domain.repository.*;
import finalproject.group1.BE.web.dto.data.image.ImageData;
import finalproject.group1.BE.web.dto.request.product.ProductListRequest;
import finalproject.group1.BE.web.dto.request.product.ProductRequest;
import finalproject.group1.BE.web.dto.response.PageableDTO;
import finalproject.group1.BE.web.dto.response.product.*;
import finalproject.group1.BE.web.exception.ExistException;
import finalproject.group1.BE.web.exception.IllegalArgumentException;
import finalproject.group1.BE.web.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.input.BOMInputStream;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductViewRepository productViewRepository;
    private final ProductToCartRepository toCartRepository;
    private final ProductFavoriteHistoryRepository favoriteHistoryRepository;
    private final CategoryRepository categoryRepository;
    private final ImageRepository imageRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ModelMapper modelMapper;
    private final GoogleDriveCommons googleDriveCommons;
    private final ProductFavoriteRepository favoriteProductRepository;
    private final UserRepository userRepository;
    private final EmailCommons emailCommons;
    private final ProductViewService productViewService;

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


    public ProductDetailResponse getProductDetails(String sku, Integer loginUserId) {
        Optional<Product> productDetail = Optional.ofNullable(productRepository.findBySku(sku).
                orElseThrow(() -> new NotFoundException("Product not found with SKU: " + sku)));

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

        productViewService.save(productDetail.get().getId(), loginUserId);

        return productDetailsDTO;
    }

    public void update(int id, ProductRequest updateRequest) {
        long start = System.currentTimeMillis();
        Product product = productRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Product Not Found"));
        Product updatedProduct = save(updateRequest, product);

        long end = System.currentTimeMillis();
        System.out.println("update product : " + (end - start));
        //send email to users that flag this product as favorite
        List<ProductFavorite> favoriteProducts = favoriteProductRepository.findByIdProductId(id);
        String[] userEmails = favoriteProducts.stream().map(favoriteProduct -> {
            User user = userRepository.findById(favoriteProduct.getId().getUserId())
                    .orElseThrow(() -> new NotFoundException("user not found"));
            return user.getEmail();
        }).toArray(String[]::new);

        if (userEmails.length == 0) {
            return;
        }
        String content = String.format(Constants.FAVORITE_PRODUCT_UPDATE_CONTENT, updatedProduct.getSku());
        emailCommons.sendSimpleMessage(userEmails, Constants.FAVORITE_PRODUCT_UPDATE_SUBJECT, content);
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
            productImgs.forEach(productImg -> new Thread(() -> googleDriveCommons.
                    deleteFileOrFolder(productImg.getImage().getPath())).start());
            productImgs.clear();
        }

        //add thumbnail image to product
        Thread addThumbnail = new Thread(() -> {
            ProductImg thumbnailImg = new ProductImg();

            Image image = new Image();
            image.setName(request.getThumbnailImage().getOriginalFilename());
            image.setPath(googleDriveCommons.uploadFile(request.getThumbnailImage(), driveProductDirectory));
            image.setThumbnailFlag(ThumbnailFlag.YES);

            thumbnailImg.setProduct(product);
            thumbnailImg.setImage(image);

            productImgs.add(thumbnailImg);
        });
        addThumbnail.start();

        //add detail images to product
        productImgs.addAll(request.getDetailImage().parallelStream().map(multipartFile -> {
            ProductImg productImg = new ProductImg();

            Image image = new Image();
            image.setName(multipartFile.getOriginalFilename());
            image.setPath(googleDriveCommons.uploadFile(multipartFile, driveProductDirectory));
            image.setThumbnailFlag(ThumbnailFlag.NO);

            productImg.setProduct(product);
            productImg.setImage(image);
            return productImg;
        }).toList());

        try {
            addThumbnail.join();
            product.setProductImgs(productImgs);
            // save changes to db
            return productRepository.save(product);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
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
            //delete product in server
            productImgs.stream().forEach(productImg -> new Thread(() -> googleDriveCommons.
                    deleteFileOrFolder(productImg.getImage().getPath())).start());
            product.getProductImgs().clear();

            //save changes to db
            Product deletedProduct = productRepository.save(product);

            //delete data in favorite product table
            List<ProductFavorite> favoriteProducts = favoriteProductRepository.
                    findByIdProductId(deletedProduct.getId());
            String[] userEmails = favoriteProducts.stream().map(favoriteProduct -> {
                User user = userRepository.findById(favoriteProduct.getId().getUserId())
                        .orElseThrow(() -> new NotFoundException("user not found"));
                return user.getEmail();
            }).toArray(String[]::new);

            favoriteProductRepository.deleteAll(favoriteProducts);
            //send mail to user
            if (userEmails.length == 0) {
                return;
            }

            String content = String.format(Constants.FAVORITE_PRODUCT_DELETE_CONTENT, deletedProduct.getOldSku());
            emailCommons.sendSimpleMessage(userEmails, Constants.FAVORITE_PRODUCT_DElETE_SUBJECT, content);
        }
    }

    @Transactional
    public void importProducts(MultipartFile csvFile) {
        try {
            List<Product> productList = csvProducts(csvFile.getInputStream());
            productRepository.saveAll(productList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * get list of user from csv file
     *
     * @param is - csv file content
     * @return list of user
     */
    public List<Product> csvProducts(InputStream is) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(new BOMInputStream(is), StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.builder()
                             .setHeader("sku", "name", "price", "detail_info", "category_id", "thumbnail_img", "detail_img")
                             .setSkipHeaderRecord(true)
                             .setIgnoreHeaderCase(true)
                             .setTrim(true).build())) {

            List<Product> productList = new ArrayList<>();
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords) {
                Product product = getProductFromCSVRecord(csvRecord);
                productList.add(product);
            }

            return productList;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
        }
    }

    private Product getProductFromCSVRecord(CSVRecord csvRecord) {
        String sku;
        String name;
        BigDecimal price;
        String detailInfo;
        Category category;
        File thumbnailFile;
        List<File> detailFiles;

        //get data from csv file
        try {
            sku = csvRecord.get("sku");
            name = csvRecord.get("name");
            price = BigDecimal.valueOf(Double.parseDouble(csvRecord.get("price")));
            detailInfo = csvRecord.get("detail_info");
            category = categoryRepository.findById(Integer.parseInt(csvRecord.get("category_id")))
                    .orElseThrow(() -> new NotFoundException("Category not found"));
            thumbnailFile = googleDriveCommons.getFileByUrl(csvRecord.get("thumbnail_img"));
            detailFiles = Arrays.stream(csvRecord.get("detail_img").split(","))
                    .map(url -> googleDriveCommons.getFileByUrl(url))
                    .collect(Collectors.toList());
        } catch (java.lang.IllegalArgumentException exception) {
            throw new IllegalArgumentException("invalid csv file for product import");
        }
        //validate data in csv
        validateProduct(sku, name, price, detailInfo, thumbnailFile, detailFiles);

        //Create new product
        Product product = new Product();
        product.setSku(sku);
        product.setName(name);
        product.setPrice(price.floatValue());
        product.setDetailInfo(detailInfo);
        product.setCategory(category);
        product.setDeleteFlag(DeleteFlag.NORMAL);

        List<ProductImg> productImgs = new ArrayList<>();
        //create thumbnail image
        ProductImg thumbnailProductImg = new ProductImg();
        Image thumbnailImage = new Image();
        thumbnailImage.setName(thumbnailFile.getName());
        thumbnailImage.setPath(thumbnailFile.getId());
        thumbnailImage.setThumbnailFlag(ThumbnailFlag.YES);
        thumbnailProductImg.setProduct(product);
        thumbnailProductImg.setImage(thumbnailImage);
        productImgs.add(thumbnailProductImg);

        //create detail image
        productImgs.addAll(detailFiles.stream().map(file -> {
            ProductImg detailProductImg = new ProductImg();
            Image detailImage = new Image();
            detailImage.setName(file.getName());
            detailImage.setPath(file.getId());
            detailImage.setThumbnailFlag(ThumbnailFlag.NO);
            detailProductImg.setProduct(product);
            detailProductImg.setImage(detailImage);

            return detailProductImg;
        }).toList());
        product.setProductImgs(productImgs);
        return product;
    }

    private void validateProduct(String sku, String name, BigDecimal price
            , String detailInfo, File thumbnailFile, List<File> detailFiles) {
        if (!Pattern.compile(Constants.VALID_SKU_PATERN).matcher(sku).matches()) {
            throw new IllegalArgumentException(sku + " not a valid sku");
        } else if (productRepository.findBySku(sku).isPresent()) {
            throw new IllegalArgumentException("product with sku " + sku + " already exist");
        }

        if (name.isEmpty()) {
            throw new IllegalArgumentException(" must have a product name");
        }

        if (detailInfo.length() > 1000) {
            throw new IllegalArgumentException(" detail length exceed 1000 character");
        }

        int intergerLength = price.precision() - price.scale();
        int fractionalLength = price.scale();
        if (intergerLength > 15) {
            throw new IllegalArgumentException(" price integer length cannot exceed 15 : " + price);
        }
        if (fractionalLength > 3) {
            throw new IllegalArgumentException(" price fractional length cannot exceed 3 : " + price);
        }

        if (!thumbnailFile.getMimeType().equals(Constants.VALID_MIMETYPE)) {
            throw new IllegalArgumentException("thumbnail file is not of image type");
        } else if (imageRepository.findByPath(thumbnailFile.getId()).isPresent()) {
            throw new IllegalArgumentException("image already have a product");
        }

        detailFiles.forEach(file -> {
            if (!file.getMimeType().equals(Constants.VALID_MIMETYPE)) {
                throw new IllegalArgumentException("detail file is not of image type");
            } else if (imageRepository.findByPath(file.getId()).isPresent()) {
                throw new IllegalArgumentException("image already have a product");
            }
        });
    }

    /**
     * get all product statistic
     *
     * @param pageable - paging
     * @param date     - from date
     * @return list of product statistic
     */
    public ProductStatisticResponse getStatistic(Pageable pageable, String date) {
        Page<Product> productPage = productRepository.findAll(pageable);
        List<ProductStatisticDetailResponse> statisticDetailResponses;
        statisticDetailResponses = productPage.getContent().stream().map(product -> {
            ProductStatisticDetailResponse detailResponse;
            if (date != null && !date.isBlank()) {
                detailResponse = getStatisticDetail(product.getId(),
                        product.getName(), product.getSku(), LocalDate.parse(date));
            } else {
                detailResponse = getStatisticDetail(product.getId(), product.getName(), product.getSku());
            }

            return detailResponse;
        }).collect(Collectors.toList());

        PageableDTO pageableDTO = new PageableDTO();
        pageableDTO.setPageNumber(productPage.getNumber());
        pageableDTO.setPageSize(productPage.getSize());
        pageableDTO.setTotalPages(productPage.getTotalPages());

        ProductStatisticResponse response = new ProductStatisticResponse();
        response.setPageableDTO(pageableDTO);
        response.setStatisticResponse(statisticDetailResponses);
        return response;
    }

    /**
     * get product statistic
     *
     * @param productId - product id
     * @param name      - product name
     * @param sku       - product sku
     * @return product statistic
     */
    private ProductStatisticDetailResponse getStatisticDetail(int productId, String name, String sku) {
        ProductStatisticDetailResponse detailResponse = new ProductStatisticDetailResponse();

        long viewCount = productViewRepository.countByProductId(productId);
        long orderCount = orderDetailRepository.countByProductId(productId);
        Float orderViewPercentage = null;
        if (viewCount != 0) {
            orderViewPercentage = ((float) orderCount / viewCount) * 100;
        }
        detailResponse.setId(productId);
        detailResponse.setSku(sku);
        detailResponse.setProductName(name);
        detailResponse.setViewCount(viewCount);
        detailResponse.setFavoriteCount(favoriteHistoryRepository
                .countByProductIdAndIsFavorite(productId, true));
        detailResponse.setFavoriteRemovalCount(favoriteHistoryRepository
                .countByProductIdAndIsFavorite(productId, false));
        detailResponse.setAddedToCartCount(toCartRepository.countByProductId(productId));
        detailResponse.setOrderCount(orderCount);
        detailResponse.setOrderQuantity(orderDetailRepository.sumQuantityByProductId(productId));
        detailResponse.setOrderViewPercentage(orderViewPercentage);

        return detailResponse;
    }

    /**
     * get product statistic
     *
     * @param date      - from date
     * @param productId - product id
     * @param name      - product name
     * @param sku       - product sku
     * @return product statistic
     */
    private ProductStatisticDetailResponse getStatisticDetail
    (int productId, String name, String sku, LocalDate date) {
        ProductStatisticDetailResponse detailResponse = new ProductStatisticDetailResponse();

        long viewCount = productViewRepository.countByProductIdAndDateAfter(productId, date.atStartOfDay());
        long orderCount = orderDetailRepository.countByProductIdAndOrderOrderDateAfter(productId, date);
        Float orderViewPercentage = null;
        if (viewCount != 0) {
            orderViewPercentage = ((float) orderCount / viewCount) * 100;
        }

        detailResponse.setId(productId);
        detailResponse.setSku(sku);
        detailResponse.setProductName(name);
        detailResponse.setViewCount(viewCount);
        detailResponse.setFavoriteCount(favoriteHistoryRepository
                .countByProductIdAndIsFavoriteAndDateAfter(productId, true, date.atStartOfDay()));
        detailResponse.setFavoriteRemovalCount(favoriteHistoryRepository
                .countByProductIdAndIsFavoriteAndDateAfter(productId, false, date.atStartOfDay()));
        detailResponse.setAddedToCartCount(toCartRepository.countByProductIdAndDateAfter(productId, date.atStartOfDay()));
        detailResponse.setOrderCount(orderCount);
        detailResponse.setOrderQuantity(orderDetailRepository.sumQuantityByProductIdAndAfterDate(productId, date));
        detailResponse.setOrderViewPercentage(orderViewPercentage);

        return detailResponse;
    }

    /**
     * export and download statistic data csv file
     *
     * @param writer        - HttpServletResponse writer
     * @param statisticData - statistic data to download
     */
    public void statisticToCSV(PrintWriter writer, List<ProductStatisticDetailResponse> statisticData) {
        try (
                CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                        .withHeader("ID", "Sku", "Product name", "View", "Favorite", "Favorite removal"
                                , "Added to cart", "Order", "Order quantity", "Order view percentage"));
        ) {
            for (ProductStatisticDetailResponse detail : statisticData) {
                List<String> data = Arrays.asList(
                        String.valueOf(detail.getId()),
                        detail.getSku(),
                        detail.getProductName(),
                        String.valueOf(detail.getViewCount()),
                        String.valueOf(detail.getFavoriteCount()),
                        String.valueOf(detail.getFavoriteRemovalCount()),
                        String.valueOf(detail.getAddedToCartCount()),
                        String.valueOf(detail.getOrderCount()),
                        String.valueOf(detail.getOrderQuantity()),
                        (detail.getOrderViewPercentage() != null) ?
                                detail.getOrderViewPercentage().toString() : "N/a"

                );
                csvPrinter.printRecord(data);
            }
            csvPrinter.flush();
        } catch (Exception e) {
            System.out.println("Writing CSV error!");
           throw new RuntimeException(e);
        }
    }
}