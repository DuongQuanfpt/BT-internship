package finalproject.group1.BE.web.controller;

import finalproject.group1.BE.domain.entities.User;
import finalproject.group1.BE.domain.services.CartService;
import finalproject.group1.BE.web.dto.request.cart.CartAddRequest;
import finalproject.group1.BE.web.dto.request.cart.CartDeleteRequest;
import finalproject.group1.BE.web.dto.request.cart.CartRequest;
import finalproject.group1.BE.web.dto.request.cart.CartUpdateRequest;
import finalproject.group1.BE.web.dto.response.ResponseDataDTO;
import finalproject.group1.BE.web.dto.response.cart.*;
import finalproject.group1.BE.web.exception.ValidationException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class CartController {

    private CartService cartService;

    @PostMapping("/add-cart")
    public ResponseEntity<ResponseDataDTO<CartAddResponse>> addToCart(
            @Valid @RequestBody CartAddRequest addRequest,
                                    BindingResult bindingResult,
                                    Authentication authentication) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        CartAddResponse response = cartService.addToCart(addRequest, authentication);
        return ResponseEntity.ok().body(ResponseDataDTO.success(response));
    }

    @PostMapping("/cart-info")
    public ResponseEntity<ResponseDataDTO<CartInfoResponse>> cartInfo(@Valid @RequestBody CartRequest request,
                                   BindingResult bindingResult,
                                   Authentication authentication) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }
        CartInfoResponse response = cartService.getCartInfo(request,authentication);
        return ResponseEntity.ok().body(ResponseDataDTO.success(response));
    }

    @PostMapping("/sync-cart")
    public ResponseEntity<ResponseDataDTO<CartSyncResponse>> syncCart(@Valid @RequestBody CartRequest request,
                                    BindingResult bindingResult,
                                    Authentication authentication){
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        User loginUser = (User) authentication.getPrincipal();
        CartSyncResponse response = cartService.synccart(request,loginUser);
        return ResponseEntity.ok().body(ResponseDataDTO.success(response));
    }

    /**
     * API for get total number of product in cart
     *
     * @param request temporary cart token if user not login
     * @param bindingResult validate result
     * @param authentication authentication object
     * @return CartQuantityResponse number of product and newest cart version
     */
    @GetMapping("/cart-quantity")
    public ResponseEntity<ResponseDataDTO<CartQuantityResponse>> getCartQuantity(@Valid @RequestBody CartRequest request,
                                   BindingResult bindingResult,
                                   Authentication authentication){
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }
        CartQuantityResponse response = cartService.calculateCartQuantity(request,authentication);
        return ResponseEntity.ok().body(ResponseDataDTO.success(response));
    }

    @PutMapping("/update-cart")
    public ResponseEntity<ResponseDataDTO<CartUpdateAndDeleteResponse>> updateCart(@RequestBody @Valid CartUpdateRequest updateRequest,
                                     BindingResult bindingResult,
                                     Authentication authentication) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        CartUpdateAndDeleteResponse response = cartService.updateCart(updateRequest, authentication);
        return ResponseEntity.ok().body(ResponseDataDTO.success(response));
    }

    @DeleteMapping("/delete-cart")
    public ResponseEntity<ResponseDataDTO<CartUpdateAndDeleteResponse>> deleteCart(@RequestBody @Valid CartDeleteRequest deleteRequest,
                                     BindingResult bindingResult,
                                     Authentication authentication) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        CartUpdateAndDeleteResponse response = cartService.deleteCart(deleteRequest, authentication);
        return ResponseEntity.ok().body(ResponseDataDTO.success(response));
    }
}
