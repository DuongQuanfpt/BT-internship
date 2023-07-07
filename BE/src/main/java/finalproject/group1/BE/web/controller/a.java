package finalproject.group1.BE.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class a {
    @GetMapping("/okie")
    public ResponseEntity okie(){
        return ResponseEntity.ok("OKIE DOOKIE");
    }
}
