package finalproject.group1.BE.web.controller;

import finalproject.group1.BE.domain.entities.User;
import finalproject.group1.BE.domain.services.OrderService;
import finalproject.group1.BE.web.dto.request.order.CreateOrderRequest;
import finalproject.group1.BE.web.dto.response.ResponseDto;
import finalproject.group1.BE.web.exception.ValidationException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
@AllArgsConstructor
public class OrderController {
    private OrderService orderService;
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/create")
    public ResponseEntity createOrder(@Valid @RequestBody CreateOrderRequest request,
                                      BindingResult bindingResult,
                                      Authentication authentication){
        if(bindingResult.hasErrors()){
            throw new ValidationException(bindingResult);
        }

        User loginUser = (User) authentication.getPrincipal();
        return ResponseEntity.ok().body(ResponseDto.success(orderService.createOrder(request,loginUser)));
    }
}
