package finalproject.group1.BE.commons;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

public class FileCommons {
    /**
     * upload image for category
     *
     * @param file - input image(only accept .jpg)
     * @return url
     */
    public static String uploadFile(MultipartFile file, String fileName, String uploadDirectory) {
        try {

            fileName = fileName + getExtension(file);
            // Create the upload directory if it doesn't exist
            Path uploadDirectoryPath = Paths.get(uploadDirectory);
            if (!Files.exists(uploadDirectoryPath)) {
                Files.createDirectories(uploadDirectoryPath);
            }

            Files.copy(file.getInputStream(), uploadDirectoryPath.resolve(fileName)
                    , StandardCopyOption.REPLACE_EXISTING);

            String destinationPath = uploadDirectory + File.separator + fileName;
            return destinationPath;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void delete(String filePath, String uploadDirectory) {
        try {
            Path uploadDirectoryPath = Paths.get(uploadDirectory);
            Files.delete(uploadDirectoryPath.resolve(filePath));
        }catch (NoSuchFileException e){
            //do nothing
        }catch (IOException e) {
           throw new RuntimeException(e);
        }
    }

    public static String getExtension(MultipartFile file) {
        try{
            String fileName = file.getOriginalFilename();
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (IndexOutOfBoundsException e){
            e.printStackTrace();
            return "";
        }
    }
}
