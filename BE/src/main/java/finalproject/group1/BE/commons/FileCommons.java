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

    /**
     * delete file
     * @param filePath - path of file to delele
     */
    public static void delete(String filePath) {
        try {
            Path path = Paths.get(filePath);
            // if file exist , delete the file
            if(Files.exists(path)){
                Files.delete(path);
            }
        }catch (IOException e) {
           throw new RuntimeException(e);
        }
    }

    /**
     * get file extension
     * @param file
     * @return
     */
    public static String getExtension(MultipartFile file) {
        try{
            String fileName = file.getOriginalFilename();
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (IndexOutOfBoundsException e){ // if file name contain no extension
            e.printStackTrace();
            return "";
        }
    }
}
