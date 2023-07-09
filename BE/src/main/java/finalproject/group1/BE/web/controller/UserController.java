package finalproject.group1.BE.web.controller;

import finalproject.group1.BE.domain.services.UserService;
import finalproject.group1.BE.web.dto.request.UserListRequest;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
@Validated
public class UserController {
    private UserService userService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/list")
    public ResponseEntity getUserList(@RequestBody UserListRequest userListRequest ,
                                      @RequestParam("page") @Min(value = 0) int page ,
                                      @RequestParam("size") int size ,
                                      @RequestParam(value = "sort",required = false) String sort){

        return ResponseEntity.ok().body(userService.getUserList(userListRequest,page,size,sort));
    }

}
