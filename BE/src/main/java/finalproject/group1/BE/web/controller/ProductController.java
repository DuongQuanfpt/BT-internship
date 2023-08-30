package finalproject.group1.BE.web.controller;

import finalproject.group1.BE.domain.entities.Product;
import finalproject.group1.BE.domain.entities.User;
import finalproject.group1.BE.domain.services.ProductFavoriteService;
import finalproject.group1.BE.domain.services.ProductService;
import finalproject.group1.BE.web.dto.request.product.ProductListRequest;
import finalproject.group1.BE.web.dto.request.product.ProductRequest;
import finalproject.group1.BE.web.dto.request.ImportRequest;
import finalproject.group1.BE.web.dto.request.product.ProductStatisticCSVRequest;
import finalproject.group1.BE.web.dto.request.product.ProductStatisticRequest;
import finalproject.group1.BE.web.dto.response.ResponseDataDTO;
import finalproject.group1.BE.web.dto.response.product.ProductDetailResponse;
import finalproject.group1.BE.web.dto.response.product.ProductListResponse;
import finalproject.group1.BE.web.dto.response.ResponseDTO;
import finalproject.group1.BE.web.dto.response.product.ProductStatisticResponse;
import finalproject.group1.BE.web.exception.CustomIOException;
import finalproject.group1.BE.web.exception.ValidationException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    private final ProductFavoriteService favoriteProductService;

    @PostMapping("/favorite/{productId}")
    public ResponseEntity<ResponseDTO> addProductToFavorite(@PathVariable("productId") int productId
            , Authentication authentication){
        User user = (User) authentication.getPrincipal();
        favoriteProductService.save(user.getId(),productId);
        return ResponseEntity.ok(ResponseDTO.build()
                .withHttpStatus(HttpStatus.OK).withMessage("OK"));
    }
    @PostMapping("/search")
    public ResponseEntity<ResponseDataDTO<ProductListResponse>> getProductList(@RequestBody @Valid ProductListRequest productListRequest ,
                                         BindingResult bindingResult, Pageable pageable) {
        if(bindingResult.hasErrors()){
            throw new ValidationException(bindingResult);
        }
        ProductListResponse productListResponse = productService.getProductList(productListRequest,pageable);
        return ResponseEntity.ok().body(ResponseDataDTO.success(productListResponse));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/import")
    public ResponseEntity<ResponseDTO> importProduct(@ModelAttribute @Valid ImportRequest request,
                                     BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }
        productService.importProducts(request.getCsvFile());
        return ResponseEntity.ok().body(ResponseDTO.build()
                .withHttpStatus(HttpStatus.OK).withMessage("OK"));
    }

    @GetMapping("/{sku}")
    public ResponseEntity<ResponseDataDTO<ProductDetailResponse>> getProductDetails(@PathVariable(value = "sku") String sku
            ,Authentication authentication) {
        Integer loginUserId = null;
        if(authentication != null) {
            User loginUser = (User)authentication.getPrincipal();
            loginUserId = loginUser.getId();
        }
        ProductDetailResponse response = productService.getProductDetails(sku,loginUserId);
        return ResponseEntity.ok().body(ResponseDataDTO.success(response));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/statistic")
    public ResponseEntity<ResponseDataDTO<ProductStatisticResponse>> getProductStatistic(@RequestBody @Valid ProductStatisticRequest request,
                                              BindingResult bindingResult,
                                              Pageable pageable) {
        if (bindingResult.hasErrors()){
            throw new ValidationException(bindingResult);
        }
        ProductStatisticResponse response = productService.getStatistic(pageable,request.getDate());
        return ResponseEntity.ok().body(ResponseDataDTO.success(response));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/statistic-csv")
    public void downloadProductStatisticAsCSV(@RequestBody @Valid ProductStatisticCSVRequest request
            , HttpServletResponse response) {
        try {
            response.setContentType("text/csv");
            response.setHeader("Content-Disposition", "attachment; file=report.csv");
            productService.statisticToCSV(response.getWriter(),request.getDatas());
        } catch (IOException e) {
            throw new CustomIOException(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/create",consumes = { MediaType.APPLICATION_JSON_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<ResponseDTO> addProduct(@Valid @ModelAttribute ProductRequest request
            , BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        productService.save(request,new Product());
        return ResponseEntity.ok().body(ResponseDTO.build()
                .withHttpStatus(HttpStatus.OK).withMessage("OK"));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/{id}",consumes = { MediaType.APPLICATION_JSON_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<ResponseDTO> updateProduct(@PathVariable(name = "id") int id
            ,@Valid @ModelAttribute ProductRequest updateRequest
            ,BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            throw new ValidationException(bindingResult);
        }

        productService.update(id,updateRequest);
        return ResponseEntity.ok().body(ResponseDTO.build()
                .withHttpStatus(HttpStatus.OK).withMessage("OK"));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<ResponseDTO> deleteProduct(@PathVariable(name = "id") int id){

        productService.delete(id);
        return ResponseEntity.ok().body(ResponseDTO.build()
                .withHttpStatus(HttpStatus.OK).withMessage("OK"));
    }

    @DeleteMapping("/remove-favorite/{productId}")
    public ResponseEntity<ResponseDTO> removeProductToFavorite(@PathVariable("productId") int productId
            , Authentication authentication){
        User user = (User) authentication.getPrincipal();
        favoriteProductService.delete(user.getId(),productId);
        return ResponseEntity.ok(ResponseDTO.build()
                .withHttpStatus(HttpStatus.OK).withMessage("OK"));
    }
}
