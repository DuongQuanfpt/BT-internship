package finalproject.group1.BE.web.controller;

import finalproject.group1.BE.domain.services.UserService;
import finalproject.group1.BE.web.dto.request.user.UserListRequest;
import finalproject.group1.BE.web.dto.response.ResponseDTO;
import finalproject.group1.BE.web.dto.response.user.UserDetailResponse;
import finalproject.group1.BE.web.dto.response.user.UserListResponse;
import finalproject.group1.BE.web.exception.ValidationException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
public class UserController {
    private UserService userService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/search")
    public ResponseEntity getUserList( @RequestBody @Valid UserListRequest userListRequest ,
                                       BindingResult bindingResult,Pageable pageable){
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }
        List<UserListResponse> response =  userService.getUserList(userListRequest,pageable);
        return ResponseEntity.ok().body(ResponseDTO.success(response));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity getUserDetail(@PathVariable(value = "id") int id){

        UserDetailResponse response = userService.getUserDetails(id);
        return ResponseEntity.ok().body(ResponseDTO.success(response));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/lock/{id}")
    public ResponseEntity lockUser(@PathVariable(value = "id") int id){

        userService.lockUser(id);
        return ResponseEntity.ok().body(ResponseDTO.build()
                .withHttpStatus(HttpStatus.OK).withMessage("OK"));
    }

}
