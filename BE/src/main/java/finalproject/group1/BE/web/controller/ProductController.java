package finalproject.group1.BE.web.controller;

import finalproject.group1.BE.domain.entities.Product;
import finalproject.group1.BE.domain.services.ProductService;
import finalproject.group1.BE.web.dto.request.ProductRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/products")
@AllArgsConstructor
public class ProductController {
    private ProductService productService;

    @PostMapping(value = "/create",consumes = { MediaType.APPLICATION_JSON_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity addProduct(@Valid @ModelAttribute ProductRequest request
            , BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            List<String> errors = bindingResult.getFieldErrors().stream()
                    .map( fieldError -> fieldError.getField() + " " +fieldError.getDefaultMessage())
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }

        productService.save(request,new Product());
        return ResponseEntity.ok().body("");
    }

    @PutMapping(value = "/{id}",consumes = { MediaType.APPLICATION_JSON_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity updateProduct(@PathVariable(name = "id") int id
            ,@Valid @ModelAttribute ProductRequest updateRequest
            ,BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            List<String> errors = bindingResult.getFieldErrors().stream()
                    .map( fieldError -> fieldError.getField() + " " +fieldError.getDefaultMessage())
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }

        productService.update(id,updateRequest);
        return ResponseEntity.ok().body("");
    }
}
