package finalproject.group1.BE.web.controller;

import finalproject.group1.BE.domain.services.UserService;
import finalproject.group1.BE.web.dto.request.user.ChangePasswordRequest;
import finalproject.group1.BE.web.dto.request.ImportRequest;
import finalproject.group1.BE.web.dto.request.user.UserListRequest;
import finalproject.group1.BE.web.dto.request.user.UserUpdateRequest;
import finalproject.group1.BE.web.dto.response.ResponseDTO;
import finalproject.group1.BE.web.dto.response.ResponseDataDTO;
import finalproject.group1.BE.web.dto.response.user.UserDetailResponse;
import finalproject.group1.BE.web.dto.response.user.UserListResponse;
import finalproject.group1.BE.web.exception.ValidationException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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
    public ResponseEntity<ResponseDataDTO<List<UserListResponse>>> getUserList(@RequestBody @Valid UserListRequest userListRequest,
                                      BindingResult bindingResult, Pageable pageable) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }
        List<UserListResponse> response = userService.getUserList(userListRequest, pageable);
        return ResponseEntity.ok().body(ResponseDataDTO.success(response));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDataDTO<UserDetailResponse>> getUserDetail(@PathVariable(value = "id") int id) {

        UserDetailResponse response = userService.getUserDetails(id);
        return ResponseEntity.ok().body(ResponseDataDTO.success(response));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/lock/{id}")
    public ResponseEntity<ResponseDTO> lockUser(@PathVariable(value = "id") int id) {

        userService.lockUser(id);
        return ResponseEntity.ok().body(ResponseDTO.build()
                .withHttpStatus(HttpStatus.OK).withMessage("OK"));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("")
    public ResponseEntity<ResponseDTO> updateUser(@RequestBody @Valid UserUpdateRequest updateRequest,
                                     BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        userService.update(updateRequest);
        return ResponseEntity.ok().body(ResponseDTO.build()
                .withHttpStatus(HttpStatus.OK).withMessage("OK"));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/import")
    public ResponseEntity<ResponseDTO> importUser(@ModelAttribute @Valid ImportRequest request,
                                     BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }
        userService.importUsers(request.getCsvFile());
        return ResponseEntity.ok().body(ResponseDTO.build()
                .withHttpStatus(HttpStatus.OK).withMessage("OK"));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO>  deleteUserByAdmin(@PathVariable(value = "id") int id) {

        userService.deleteUserByAdmin(id);
        return ResponseEntity.ok().body(ResponseDTO.build()
                .withHttpStatus(HttpStatus.OK).withMessage("OK"));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDTO>  deleteUser(Authentication authentication) {
        userService.deleteUser(authentication);
        return ResponseEntity.ok().body(ResponseDTO.build()
                .withHttpStatus(HttpStatus.OK).withMessage("OK"));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("/change-password")
    public ResponseEntity<ResponseDTO>  changePassword(@RequestBody @Valid ChangePasswordRequest changePasswordRequest,
                                         BindingResult bindingResult,
                                         Authentication authentication) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        userService.changePassword(changePasswordRequest, authentication);
        return ResponseEntity.ok().body(ResponseDTO.build().withMessage("OK").withHttpStatus(HttpStatus.OK));
    }
}
