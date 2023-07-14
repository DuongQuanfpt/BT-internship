package finalproject.group1.BE.domain.services;

import finalproject.group1.BE.domain.entities.Cart;
import finalproject.group1.BE.domain.entities.CartDetail;
import finalproject.group1.BE.domain.entities.Product;
import finalproject.group1.BE.domain.entities.User;
import finalproject.group1.BE.domain.repository.*;
import finalproject.group1.BE.web.dto.data.image.ImageData;
import finalproject.group1.BE.web.dto.request.cart.CartAddRequest;
import finalproject.group1.BE.web.dto.request.cart.CartInfoRequest;
import finalproject.group1.BE.web.dto.response.cart.CartAddResponse;
import finalproject.group1.BE.web.dto.response.cart.CartInfoDetailResponse;
import finalproject.group1.BE.web.dto.response.cart.CartInfoResponse;
import finalproject.group1.BE.web.exception.NotFoundException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.internal.bytebuddy.utility.RandomString;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CartService {
    private CartRepository cartRepository;

    private CartDetailsRepository cartDetailsRepository;

    private ProductRepository productRepository;

    private ImageRepository imageRepository;

    private ModelMapper modelMapper;


    /**
     * add product to new cart , or existing cart if it exists
     *
     * @param request
     * @param authentication
     * @return response
     */
    public CartAddResponse addToCart(CartAddRequest request, Authentication authentication) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new NotFoundException("Product Not Found"));
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
        float currentTotal = cartDetailsRepository.sumTotalPriceByCartId(cart.getId());
        cart.setTotalPrice(currentTotal + (product.getPrice() * request.getQuantity()));

        //save cart to DB
        Cart savedCart = cartRepository.save(cart);

        CartDetail newCartDetail = cartDetailsRepository.findByProductIdAndCartId(product.getId()
                , savedCart.getId()).orElse(null);
        //get detail in cart , if not exist create new cart detail
        if (newCartDetail == null){
            newCartDetail = new CartDetail();
            newCartDetail.setCart(cart);
            newCartDetail.setProduct(product);
        }
        newCartDetail.setPrice(product.getPrice());
        newCartDetail.setQuantity((newCartDetail.getQuantity()) + request.getQuantity());
        newCartDetail.setTotalPrice(newCartDetail.getTotalPrice() +
                (product.getPrice() * request.getQuantity()));

        //save cart detail to db
        cartDetailsRepository.save(newCartDetail);

        //calculate quantity and create response
        int quantity = cartDetailsRepository.sumQuantityByCardId(savedCart.getId());
        CartAddResponse response = new CartAddResponse();
        response.setQuantity(quantity);
        response.setVersionNo(cart.getVersionNo());
        response.setToken(cart.getToken());
        return response;
    }

    public CartInfoResponse getCartInfo(CartInfoRequest request, Authentication authentication) {
        User loginUser;
        Cart cart = null;
        if (authentication != null) {  //check if there are user login
            loginUser = (User) authentication.getPrincipal();
            cart = cartRepository.findByOwnerId(loginUser.getId()).orElseThrow(null);
        } else if (request.getToken() != null) { //check if there are token
            cart = cartRepository.findByToken(request.getToken()).orElse(null);
        }

        CartInfoResponse response = new CartInfoResponse();
        if (cart != null) {
            response.setId(cart.getId());
            response.setTotalPrice(cart.getTotalPrice());
            response.setVersionNo(cart.getVersionNo());

            List<CartDetail> cartDetails = cartDetailsRepository.findByCartId(cart.getId());
            response.setDetails(cartDetails.stream().map(cartDetail -> {
                CartInfoDetailResponse detailResponse = new CartInfoDetailResponse();
                detailResponse = modelMapper.map(cartDetail, CartInfoDetailResponse.class);

                ImageData imageData = imageRepository.findProductThumbnail(cartDetail.getProduct().getId());
                detailResponse.setImageName(imageData.getName());
                detailResponse.setImagePath(imageData.getPath());
                return detailResponse;
            }).collect(Collectors.toList()));
        }

        return response;
    }
}
