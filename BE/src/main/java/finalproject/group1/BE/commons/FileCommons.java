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
            return destinationPath.substring(destinationPath.lastIndexOf(File.separator) + 1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * delete file
     *
     * @param filePath - path of file to delele
     */
    public static void delete(String filePath, String uploadDirectory) {
        try {
            Path uploadDirectoryPath = Paths.get(uploadDirectory);
            Path resolvedFilePath = uploadDirectoryPath.resolve(filePath);
            // if file exist , delete the file
            if (Files.exists(resolvedFilePath)) {
                Files.delete(resolvedFilePath);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * get file extension
     *
     * @param file - file to get extension
     * @return file extension
     */
    public static String getExtension(MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename();
            if (fileName != null) {
                return fileName.substring(fileName.lastIndexOf("."));
            }
            return "";
        } catch (IndexOutOfBoundsException e) { // if file name contain no extension
            e.printStackTrace();
            return "";
        }
    }
}
