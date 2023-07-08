package finalproject.group1.BE.web.controller;

import finalproject.group1.BE.domain.services.UserService;
import finalproject.group1.BE.web.dto.request.UserRegisterRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthenticationController {
    private UserService userService;

    @PostMapping("/register")
    private ResponseEntity userRegister(@RequestBody @Valid UserRegisterRequest registerRequest
            , BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            List<String> errorList = bindingResult.getAllErrors().stream()
                    .map(objectError -> {
                        FieldError fieldError = (FieldError) objectError;
                        return fieldError.getField() +" "+ objectError.getDefaultMessage();
                    })
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errorList);
        }
        userService.saveUser(registerRequest);
        return ResponseEntity.ok().body("");
    }
}
