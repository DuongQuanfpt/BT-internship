package finalproject.group1.BE.web.controller;

import com.google.api.services.drive.model.File;
import finalproject.group1.BE.commons.GoogleDriveCommons;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
