package finalproject.group1.BE.web.controller;

import finalproject.group1.BE.domain.services.UserService;
import finalproject.group1.BE.web.dto.request.UserListRequest;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
public class UserController {
    private UserService userService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/list")
    public ResponseEntity getUserList(@RequestBody UserListRequest userListRequest ,
                                       Pageable pageable){

        return ResponseEntity.ok().body(userService.getUserList(userListRequest,pageable));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/detail")
    public ResponseEntity getUserDetail(@RequestParam(value = "id") int id){

        return ResponseEntity.ok().body(userService.getUserDetails(id));
    }

}
