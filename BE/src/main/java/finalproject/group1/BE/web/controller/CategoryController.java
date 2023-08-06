package finalproject.group1.BE.web.controller;

import com.google.api.services.drive.model.File;
import finalproject.group1.BE.commons.EmailCommons;
import finalproject.group1.BE.commons.GoogleDriveCommons;
import finalproject.group1.BE.domain.entities.Category;
import finalproject.group1.BE.domain.services.CategoryService;
import finalproject.group1.BE.web.dto.request.category.CreateCategoryRequest;
import finalproject.group1.BE.web.dto.request.category.UpdateCategoryRequest;
import finalproject.group1.BE.web.dto.response.category.CategoryListResponse;
import finalproject.group1.BE.web.dto.response.ResponseDTO;
import finalproject.group1.BE.web.exception.ValidationException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@AllArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    private final GoogleDriveCommons googleDriveCommons;
    @PostMapping(value = "/demo",consumes = { MediaType.APPLICATION_JSON_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity uploadDemo(@ModelAttribute CreateCategoryRequest request ) {
        categoryService.demoUpload(request);
        return ResponseEntity.ok().body(ResponseDTO.build()
                .withHttpStatus(HttpStatus.OK).withMessage("OK"));
    }

    @GetMapping({"/filelist"})
    public ResponseEntity<List<File>> listEverything() throws IOException, GeneralSecurityException, GeneralSecurityException, IOException {
        List<File> files = googleDriveCommons.listEverything();
        return ResponseEntity.ok(files);
    }

    @GetMapping("/search")
    public ResponseEntity getAllCategories() {
        List<CategoryListResponse> response = categoryService.getAllCategories();
        return ResponseEntity.ok().body(ResponseDTO.success(response));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/create", consumes = { MediaType.APPLICATION_JSON_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity addCategory(@Valid @ModelAttribute CreateCategoryRequest request) {
        categoryService.createCategory(request, new Category());
        return ResponseEntity.ok().body(ResponseDTO.build().withMessage("OK").withHttpStatus(HttpStatus.OK));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/{id}", consumes = { MediaType.APPLICATION_JSON_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity updateCategory(@PathVariable(value = "id") int id
            , @Valid @ModelAttribute UpdateCategoryRequest request
            , BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            throw new ValidationException(bindingResult);
        }
        categoryService.updateCategory(request,id);
        return ResponseEntity.ok().body(ResponseDTO.build().withMessage("OK").withHttpStatus(HttpStatus.OK));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity deleteCategory(@PathVariable(value = "id") int id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok().body(ResponseDTO.build().withMessage("OK").withHttpStatus(HttpStatus.OK));
    }
}
