package finalproject.group1.BE.web.controller;

import finalproject.group1.BE.domain.entities.User;
import finalproject.group1.BE.domain.services.CartService;
import finalproject.group1.BE.web.dto.request.cart.CartAddRequest;
import finalproject.group1.BE.web.dto.request.cart.CartRequest;
import finalproject.group1.BE.web.dto.response.ResponseDto;
import finalproject.group1.BE.web.dto.response.cart.CartAddResponse;
import finalproject.group1.BE.web.dto.response.cart.CartInfoResponse;
import finalproject.group1.BE.web.dto.response.cart.CartSyncResponse;
import finalproject.group1.BE.web.exception.ValidationException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class CartController {

    private CartService cartService;

    @PostMapping("/add-cart")
    public ResponseEntity addToCart(@Valid @RequestBody CartAddRequest addRequest,
                                    BindingResult bindingResult,
                                    Authentication authentication) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        CartAddResponse response = cartService.addToCart(addRequest, authentication);
        return ResponseEntity.ok().body(ResponseDto.success(response));
    }

    @PostMapping("/cart-info")
    public ResponseEntity cartInfo(@Valid @RequestBody CartRequest request,
                                   BindingResult bindingResult,
                                   Authentication authentication) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }
        CartInfoResponse response = cartService.getCartInfo(request,authentication);
        return ResponseEntity.ok().body(ResponseDto.success(response));
    }

    @PostMapping("/sync-cart")
    public  ResponseEntity syncCart(@Valid @RequestBody CartRequest request,
                                    BindingResult bindingResult,
                                    Authentication authentication){
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        User loginUser = (User) authentication.getPrincipal();
        CartSyncResponse response = cartService.synccart(request,loginUser);
        return ResponseEntity.ok().body(ResponseDto.success(response));
    }
}
