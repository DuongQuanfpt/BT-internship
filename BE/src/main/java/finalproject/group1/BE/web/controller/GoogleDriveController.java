package finalproject.group1.BE.web.controller;

import com.google.api.services.drive.model.File;
import finalproject.group1.BE.commons.GoogleDriveCommons;
import finalproject.group1.BE.web.dto.response.ResponseDTO;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/drive")
@RequiredArgsConstructor
public class GoogleDriveController {
    private final GoogleDriveCommons googleDriveCommons;

    @GetMapping()
    public ResponseEntity<List<File>> listEverything() throws IOException, GeneralSecurityException, GeneralSecurityException, IOException {
        List<File> files = googleDriveCommons.listEverything();
        return ResponseEntity.ok(files);
    }

    @GetMapping(value = "/dowload/{id}")
    public ResponseEntity dowloadFile(@PathVariable(value = "id") String id
            , HttpServletResponse response) throws IOException {

        googleDriveCommons.downloadFile(id, response.getOutputStream());
        return ResponseEntity.ok(ResponseDTO.build().withMessage("OK")
                .withHttpStatus(HttpStatus.OK));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteFile(@PathVariable(value = "id") String id) {
        googleDriveCommons.deleteFileOrFolder(id);
        return ResponseEntity.ok(ResponseDTO.build().withMessage("OK")
                .withHttpStatus(HttpStatus.OK));
    }


}
