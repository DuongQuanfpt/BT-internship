package finalproject.group1.BE.web.controller;

import finalproject.group1.BE.domain.services.UserService;
import finalproject.group1.BE.web.dto.request.UserListRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {
    private UserService userService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/list")
    public ResponseEntity getUserList(@RequestBody UserListRequest userListRequest ,
                                     @RequestParam("page") int page ,
                                     @RequestParam("size") int size ,
                                     @RequestParam(value = "sort",required = false) String sort){
        return ResponseEntity.ok().body(userService.getUserList(userListRequest));
    }
}
