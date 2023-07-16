package finalproject.group1.BE.commons;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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

            Files.copy(file.getInputStream(), uploadDirectoryPath.resolve(fileName));

            String destinationPath = uploadDirectory + File.separator + fileName;
            String fileUrl = destinationPath.substring(destinationPath.lastIndexOf(File.separator) + 1);
            return fileUrl;
        } catch (IOException e) {
            return "Failed to upload file: " + e.getMessage();
        }
    }

    public static void delete(String filePath, String uploadDirectory) {
        try {
            Path uploadDirectoryPath = Paths.get(uploadDirectory);
            Files.delete(uploadDirectoryPath.resolve(filePath));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static String getExtension(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        return fileName.substring(fileName.lastIndexOf("."));
    }
}
