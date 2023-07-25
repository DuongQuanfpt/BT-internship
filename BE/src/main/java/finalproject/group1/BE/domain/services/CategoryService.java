package finalproject.group1.BE.domain.services;

import finalproject.group1.BE.commons.FileCommons;

import finalproject.group1.BE.domain.entities.Category;
import finalproject.group1.BE.domain.entities.CategoryImg;
import finalproject.group1.BE.domain.entities.Image;
import finalproject.group1.BE.domain.entities.Product;
import finalproject.group1.BE.domain.enums.ThumbnailFlag;
import finalproject.group1.BE.domain.repository.CategoryImgRepository;
import finalproject.group1.BE.domain.repository.CategoryRepository;
import finalproject.group1.BE.domain.repository.ImageRepository;
import finalproject.group1.BE.domain.repository.ProductRepository;
import finalproject.group1.BE.web.dto.request.category.UpdateCategoryRequest;
import finalproject.group1.BE.web.dto.response.category.CategoryListResponse;
import finalproject.group1.BE.web.dto.request.category.CreateCategoryRequest;
import finalproject.group1.BE.web.exception.DeleteCategoryException;
import finalproject.group1.BE.web.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.internal.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final CategoryImgRepository categoryImgRepository;
    private final ImageRepository imageRepository;
    private final ModelMapper modelMapper;

    @Value("${file.upload.category-directory}")
    private String fileUploadDirectory;

    public List<CategoryListResponse> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryListResponse> listCategoriesDTO = categories.stream()
                .map(category ->
                {
                    CategoryListResponse response = modelMapper.map(category, CategoryListResponse.class);
                    response.setImageName(category.getCategoryImg().getImage().getName());
                    response.setPath(category.getCategoryImg().getImage().getPath());
                    return response;

                })
                .collect(Collectors.toList());
        return listCategoriesDTO;
    }

    public void createCategory(CreateCategoryRequest dto, Category category) {

        //add image to category
        CategoryImg categoryImg = new CategoryImg();
        Image image = new Image();
        image.setName(dto.getImage().getOriginalFilename());
        image.setPath(FileCommons.uploadFile(dto.getImage(), RandomString.make(15), fileUploadDirectory));
        image.setThumbnailFlag(ThumbnailFlag.NO);

        categoryImg.setCategory(category);
        categoryImg.setImage(image);

        category.setName(dto.getName());
        category.setCategoryImg(categoryImg);

        categoryRepository.save(category);
    }

    /**
     * update category information
     * @param request
     * @param id      - id of category to update
     */
    @Transactional
    public void updateCategory(UpdateCategoryRequest request, int id) {
        Category category = categoryRepository.findById(id).orElse(null);
        if (category == null) { //if category not exist
           return;
        }
        //update category information
        category.setName(request.getName());
        //if user upload new image
        if (request.getImage() != null){
            //delete old image
            if(category.getCategoryImg() != null){
                FileCommons.delete(category.getCategoryImg().getImage().getPath()
                        ,fileUploadDirectory);
            }
            //create image
            Image image = new Image();
            image.setName(request.getImage().getOriginalFilename());
            image.setPath(FileCommons.uploadFile(request.getImage(),
                    RandomString.make(15),fileUploadDirectory));
            image.setThumbnailFlag(ThumbnailFlag.NO);

            //update category image
            CategoryImg categoryImg = category.getCategoryImg();
            categoryImg.setImage(image);

        }
        //save changes to DB
        categoryRepository.save(category);
    }

    /**
     * delete category information
     * @param id - id of category to delete
     */
    @Transactional
    public void deleteCategory(int id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> {
            throw new NotFoundException("Category Not Found !!!");
        });

        List<Product> productList = productRepository.findByCategoryId(id);
        if (productList.size() == 0) {
            imageRepository.deleteById(category.getCategoryImg().getImage().getId());
            categoryImgRepository.deleteById(category.getCategoryImg().getId());
            categoryRepository.deleteById(id);
        }
        else {
            throw new DeleteCategoryException("This category cannot be deleted because there are related products!");
        }
    }
}
