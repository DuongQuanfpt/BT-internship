package finalproject.group1.BE.web.controller;

import finalproject.group1.BE.domain.entities.User;
import finalproject.group1.BE.domain.services.CartService;
import finalproject.group1.BE.web.dto.request.cart.CartAddRequest;
import finalproject.group1.BE.web.dto.response.ResponseDto;
import finalproject.group1.BE.web.dto.response.cart.CartAddResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class CartController {

    private CartService cartService;
    @PostMapping("/add-cart")
    public ResponseEntity addToCart(@Valid @RequestBody  CartAddRequest addRequest,
                                    Authentication authentication,
                                    BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            List<String> errors = bindingResult.getFieldErrors().stream()
                    .map( fieldError -> fieldError.getField() + " " +fieldError.getDefaultMessage())
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }

        CartAddResponse response = cartService.addToCart(addRequest,authentication);
        return ResponseEntity.ok().body(ResponseDto.success(response));
    }
}
