package finalproject.group1.BE.domain.services;

import com.google.common.io.Files;
import finalproject.group1.BE.commons.Constants;
import finalproject.group1.BE.domain.entities.*;
import finalproject.group1.BE.domain.enums.ThumbnailFlag;
import finalproject.group1.BE.domain.repository.CategoryRepository;
import finalproject.group1.BE.web.dto.response.Category.CategoryListResponse;
import finalproject.group1.BE.web.dto.request.Category.CreateCategoryRequest;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.internal.bytebuddy.utility.RandomString;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CategoryService {

    private CategoryRepository categoryRepository;
    private ModelMapper modelMapper;

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

        // Update category image

//        CategoryImg categoryImg = category.getCategoryImg();
//        //check if category has image
//        if (categoryImg != null) {
//            //delete existing image
//            deleteFile(categoryImg);
//        }

        //add image to category

        CategoryImg categoryImg = new CategoryImg();
        Image image = createImageFromMultipartFile(dto.getImage()
                ,false, RandomString.make(30));
        categoryImg.setCategory(category);
        categoryImg.setImage(image);

        category.setName(dto.getName());
        category.setCategoryImg(categoryImg);

        categoryRepository.save(category);
    }


    private Image createImageFromMultipartFile(MultipartFile multipartFile, boolean isThumbnail, String fileName) {
        String extension = Files.getFileExtension(multipartFile.getOriginalFilename());
        File imgFile = new File(Constants.IMAGE_FOLDER_PATH + fileName + "." + extension);
        try (OutputStream os = new FileOutputStream(imgFile)) {
            os.write(multipartFile.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Image image = new Image();
        image.setName(multipartFile.getOriginalFilename());
        image.setPath(imgFile.getPath());
        image.setThumbnailFlag(ThumbnailFlag.getThumbnailFlag(isThumbnail));

        return image;
    }

    public void deleteFile(CategoryImg categoryImg){
            File file = new File(categoryImg.getImage().getPath());
            file.delete();
    }

}
