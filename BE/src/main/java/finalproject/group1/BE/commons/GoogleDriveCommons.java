package finalproject.group1.BE.commons;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.HttpStatusCodes;
import com.google.api.client.http.InputStreamContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import finalproject.group1.BE.web.config.GoogleDriveConfig;
import finalproject.group1.BE.web.exception.IllegalArgumentException;
import finalproject.group1.BE.web.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class GoogleDriveCommons {
    private final GoogleDriveConfig googleDriveConfig;

    public List<File> listEverything() throws IOException, GeneralSecurityException {
        // Print the names and IDs for up to 10 files.
        String query = " 'me' in owners ";
        FileList result = googleDriveConfig.getDrive().files().list()
                .setQ(query)
                .setPageSize(10)
                .setFields("nextPageToken, files(id, name,webContentLink)")
                .execute();
        return result.getFiles();
    }

    public String uploadFile(MultipartFile file, String folderPath) {
        try {
            String folderId = getFolderId(folderPath);
            if (null != file) {
                File fileMetadata = new File();
                fileMetadata.setParents(Collections.singletonList(folderId));
                fileMetadata.setName(file.getOriginalFilename());
                File uploadFile = googleDriveConfig.getDrive()
                        .files()
                        .create(fileMetadata, new InputStreamContent(
                                file.getContentType(),
                                new ByteArrayInputStream(file.getBytes()))
                        )
                        .setFields("id").execute();
                return uploadFile.getId();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    // get id folder google drive
    public String getFolderId(String folderName) throws Exception {
        String parentId = null;
        String[] folderNames = folderName.split("/");

        Drive driveInstance = googleDriveConfig.getDrive();
        for (String name : folderNames) {
            parentId = findOrCreateFolder(parentId, name, driveInstance);
        }
        return parentId;
    }

    private String findOrCreateFolder(String parentId, String folderName, Drive driveInstance) throws Exception {
        String folderId = searchFolderId(parentId, folderName, driveInstance);
        // Folder already exists, so return id
        if (folderId != null) {
            return folderId;
        }
        //Folder dont exists, create it and return folderId
        File fileMetadata = new File();
        fileMetadata.setMimeType("application/vnd.google-apps.folder");
        fileMetadata.setName(folderName);

        if (parentId != null) {
            fileMetadata.setParents(Collections.singletonList(parentId));
        }
        return driveInstance.files().create(fileMetadata)
                .setFields("id")
                .execute()
                .getId();
    }


    private String searchFolderId(String parentId, String folderName, Drive service) throws Exception {
        String folderId = null;
        FileList result;

        String query = " mimeType = 'application/vnd.google-apps.folder'" +
                " and 'me' in owners" +
                " and name = '" + folderName + "'";

        if (parentId == null) {
            query = query + " and 'root' in parents";
        } else {
            query = query + " and '" + parentId + "' in parents";
        }

        result = service.files().list()
                .setQ(query)
                .setPageSize(10)
                .setFields("nextPageToken, files(id, name)")
                .execute();

        if (!result.isEmpty()) {
            folderId = result.getFiles().get(0).getId();
        }
        return folderId;
    }

    // Delete file by id
    public void deleteFileOrFolder(String fileId) {
        try {
            googleDriveConfig.getDrive().files().delete(fileId).execute();
        } catch (GoogleJsonResponseException e) {
            if (e.getStatusCode() != HttpStatusCodes.STATUS_CODE_NOT_FOUND) {
                throw new RuntimeException(e);
            }

        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }

    }

    // Download file by id
    public void downloadFile(String id, OutputStream outputStream) {
        if (id != null) {
            try {
                googleDriveConfig.getDrive().files()
                        .get(id).executeMediaAndDownloadTo(outputStream);
            }catch (HttpResponseException e) {
                if (e.getStatusCode() != HttpStatusCodes.STATUS_CODE_NOT_FOUND) {
                    throw new RuntimeException(e);
                }
                throw new NotFoundException("File not found on drive");
            } catch (IOException | GeneralSecurityException e) {
                throw new RuntimeException(e);
            }
        }
    }

    //get file by Id
    public File getFileById(String fileId) {
        try {
            return googleDriveConfig.getDrive().files().get(fileId).execute();
        } catch (GoogleJsonResponseException e) {
            if (e.getStatusCode() != HttpStatusCodes.STATUS_CODE_NOT_FOUND) {
                throw new RuntimeException(e);
            }
           return null;
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }

    }

    // Get file from url
    public File getFileByUrl(String url) {
        Pattern pattern = Pattern.compile("[-\\w]{25,}");
        Matcher matcher = pattern.matcher(url);
        if (!matcher.find()) {
            throw new IllegalArgumentException("invalid image url");
        }
        String fileId = matcher.group();
        File file = getFileById(fileId);
        if (file == null) {
            throw new NotFoundException("No image found at url : " + url);
        }
        return file;
    }

    public String getFileIdFromUrl(String url){
        Pattern pattern = Pattern.compile("[-\\w]{25,}");
        Matcher matcher = pattern.matcher(url);
        if (matcher.find())
        {
            return matcher.group();
        }
        return null;
    }
}
