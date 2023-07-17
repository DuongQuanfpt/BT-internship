package finalproject.group1.BE.domain.services;

import com.google.common.io.Files;
import finalproject.group1.BE.commons.Constants;
import finalproject.group1.BE.commons.FileCommons;
import finalproject.group1.BE.domain.entities.*;
import finalproject.group1.BE.domain.enums.ThumbnailFlag;
import finalproject.group1.BE.domain.repository.CategoryRepository;
import finalproject.group1.BE.web.dto.response.category.CategoryListResponse;
import finalproject.group1.BE.web.dto.request.category.CreateCategoryRequest;
import finalproject.group1.BE.web.exception.ExistException;
import finalproject.group1.BE.web.exception.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.internal.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
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

//         //Update category image
//        CategoryImg categoryImg = category.getCategoryImg();
//        //check if category has image
//        if (categoryImg != null) {
//            //delete existing image
//            FileCommons.delete(categoryImg.getImage().getPath(), fileUploadDirectory);
//            category.setCategoryImg(null);
//        }

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
}
