package finalproject.group1.BE.web.controller;

import finalproject.group1.BE.domain.services.UserService;
import finalproject.group1.BE.web.dto.request.UserListRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
public class UserController {
    private UserService userService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/search")
    public ResponseEntity getUserList( @RequestBody @Valid UserListRequest userListRequest ,
                                       BindingResult bindingResult,Pageable pageable){
        if(bindingResult.hasErrors()){
            List<String> errors = bindingResult.getFieldErrors().stream()
                    .map( fieldError -> fieldError.getField() + " " +fieldError.getDefaultMessage())
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }
        return ResponseEntity.ok().body(userService.getUserList(userListRequest,pageable));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity getUserDetail(@PathVariable(value = "id") int id){

        return ResponseEntity.ok().body(userService.getUserDetails(id));
    }

}
