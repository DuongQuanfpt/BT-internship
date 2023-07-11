package finalproject.group1.BE.web.controller;

import finalproject.group1.BE.domain.entities.Category;
import finalproject.group1.BE.domain.services.CategoryService;
import finalproject.group1.BE.web.dto.request.Category.CreateCategoryRequest;
import finalproject.group1.BE.web.dto.response.Category.CategoryListResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
@AllArgsConstructor
public class CategoryController {
    private CategoryService categoryService;

    @GetMapping()
    public ResponseEntity<List<CategoryListResponse>> getAllCategories() {
        return ResponseEntity.ok().body(categoryService.getAllCategories());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/add", consumes = { MediaType.APPLICATION_JSON_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity addCategory(@Valid @ModelAttribute CreateCategoryRequest request) {
        categoryService.createCategory(request, new Category());
        return ResponseEntity.ok().body("");
    }

}
