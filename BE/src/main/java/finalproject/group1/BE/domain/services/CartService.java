package finalproject.group1.BE.domain.services;

import finalproject.group1.BE.domain.entities.Cart;
import finalproject.group1.BE.domain.entities.CartDetail;
import finalproject.group1.BE.domain.entities.Product;
import finalproject.group1.BE.domain.entities.User;
import finalproject.group1.BE.domain.repository.CartDetailsRepository;
import finalproject.group1.BE.domain.repository.CartRepository;
import finalproject.group1.BE.domain.repository.ProductRepository;
import finalproject.group1.BE.web.dto.request.cart.CartAddRequest;
import finalproject.group1.BE.web.dto.response.cart.CartAddResponse;
import finalproject.group1.BE.web.exception.NotFoundException;
import lombok.AllArgsConstructor;
import org.modelmapper.internal.bytebuddy.utility.RandomString;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@AllArgsConstructor
public class CartService {
    private CartRepository cartRepository;

    private ProductRepository productRepository;

    /**
     * add product to new cart , or existing cart if it exists
     *
     * @param request
     * @param authentication
     * @return response
     */
    public CartAddResponse addToCart(CartAddRequest request, Authentication authentication) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new NotFoundException());
        Cart cart = null;
        User loginUser = null;
        if (authentication != null) {  //check if there are user login
            loginUser = (User) authentication.getPrincipal();
            cart = cartRepository.findByOwnerId(loginUser.getId()).orElse(null);
        } else if (request.getToken() != null) { //check if there are token
            cart = cartRepository.findByToken(request.getToken()).orElse(null);
        }

        //if there are no existing cart , create new
        if (cart == null) {
            cart = new Cart();
            if (loginUser != null) {// if there are login user , set owner
                cart.setOwner(loginUser);
            } else {// if not set token
                cart.setToken(RandomString.make(20));
            }

        } else { //if exist update cart version
            cart.setVersionNo(cart.getVersionNo() + 1);
        }

        //calculate new total prices of product in cart
        float currentTotal = (float) cart.getCartDetails().stream()
                .mapToDouble(value -> value.getTotalPrice())
                .sum();
        cart.setTotalPrice(currentTotal + (product.getPrice() * request.getQuantity()));

        //check if cart detail list have been updated
        if (!updateDetailInCart(request, cart.getCartDetails())){
            //if not
            //create new cart detail
            CartDetail newCartDetail = new CartDetail();
            newCartDetail.setProduct(product);
            newCartDetail.setPrice(product.getPrice());
            newCartDetail.setQuantity(request.getQuantity());
            newCartDetail.setTotalPrice(product.getPrice() * request.getQuantity());

            //add new cart detail to cart
            newCartDetail.setCart(cart);
            cart.getCartDetails().add(newCartDetail);
        }

        //save to DB
        cartRepository.save(cart);

        //calculate quantity and create response
        int quantity = cart.getCartDetails().stream()
                .mapToInt(value -> value.getQuantity())
                .sum();
        CartAddResponse response = new CartAddResponse();
        response.setQuantity(quantity);
        response.setVersionNo(cart.getVersionNo());
        response.setToken(cart.getToken());
        return response;
    }


    /**
     * update the detail in cart which have the same product id as in request
     *
     * @param request
     * @param cartDetails - detail list of the cart
     * @return true if the list has been updated , otherwise false
     */
    private boolean updateDetailInCart(CartAddRequest request, List<CartDetail> cartDetails) {
        AtomicBoolean isUpdate = new AtomicBoolean(false);
        cartDetails.stream().forEach(cartDetail -> {
            if (cartDetail.getProduct().getId() == request.getProductId()) {
                isUpdate.set(true);

                cartDetail.setQuantity(cartDetail.getQuantity() + request.getQuantity());
                cartDetail.setTotalPrice(cartDetail.getQuantity() * cartDetail.getPrice());
            }
        });

        return isUpdate.get();
    }


}
