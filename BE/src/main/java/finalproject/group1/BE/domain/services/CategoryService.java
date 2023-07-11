package finalproject.group1.BE.domain.services;

import finalproject.group1.BE.domain.entities.Category;
import finalproject.group1.BE.domain.repository.CategoryRepository;
import finalproject.group1.BE.web.dto.response.Category.CategoryListResponse;
import finalproject.group1.BE.web.dto.request.Category.CreateCategoryRequest;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CategoryService {

    private CategoryRepository categoryRepository;
    private ModelMapper modelMapper;

    public List<CategoryListResponse> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryListResponse> listCategoriesDTO = new ArrayList<>();
        modelMapper.map(categories, listCategoriesDTO);
        return listCategoriesDTO;
    }

    public List<CategoryListResponse> getCategoryById(int id) {
        Category category = categoryRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Not Found This Category !!!"));
        List<CategoryListResponse> categoryByIdDTO = new ArrayList<>();
        modelMapper.map(category, categoryByIdDTO);
        return categoryByIdDTO;
    }

    public CreateCategoryRequest createCategory(CreateCategoryRequest dto) {
        Category category = new Category();
        category.setName(dto.getName());
        Category savedCategory = categoryRepository.save(category);

        CreateCategoryRequest savedCategoryDTO = new CreateCategoryRequest();
        savedCategoryDTO.setName(savedCategory.getName());
        return savedCategoryDTO;
    }

}
