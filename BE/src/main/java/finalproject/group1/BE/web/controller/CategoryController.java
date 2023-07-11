package finalproject.group1.BE.web.controller;

import finalproject.group1.BE.domain.services.CategoryService;
import finalproject.group1.BE.web.dto.request.Category.CreateCategoryRequest;
import finalproject.group1.BE.web.dto.response.Category.CategoryListResponse;
import lombok.AllArgsConstructor;
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
    @PostMapping("/add")
    public ResponseEntity<CreateCategoryRequest> addCategory(@RequestBody CreateCategoryRequest request) {
        return ResponseEntity.ok().body(categoryService.createCategory(request));
    }

}
