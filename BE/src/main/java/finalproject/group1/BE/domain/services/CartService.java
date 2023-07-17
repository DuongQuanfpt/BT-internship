package finalproject.group1.BE.domain.services;

import finalproject.group1.BE.domain.entities.Cart;
import finalproject.group1.BE.domain.entities.CartDetail;
import finalproject.group1.BE.domain.entities.Product;
import finalproject.group1.BE.domain.entities.User;
import finalproject.group1.BE.domain.repository.*;
import finalproject.group1.BE.web.dto.data.image.ImageData;
import finalproject.group1.BE.web.dto.request.cart.CartAddRequest;
import finalproject.group1.BE.web.dto.request.cart.CartRequest;
import finalproject.group1.BE.web.dto.response.cart.CartAddResponse;
import finalproject.group1.BE.web.dto.response.cart.CartInfoDetailResponse;
import finalproject.group1.BE.web.dto.response.cart.CartInfoResponse;
import finalproject.group1.BE.web.dto.response.cart.CartSyncResponse;
import finalproject.group1.BE.web.exception.NotFoundException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.internal.bytebuddy.utility.RandomString;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
    @Transactional
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
            cart.setTotalPrice(0f);
            if (loginUser != null) {// if there are login user , set owner
                cart.setOwner(loginUser);
            } else {// if not set token
                cart.setToken(RandomString.make(20));
            }

        } else { //if exist update cart version
            cart.setVersionNo(cart.getVersionNo() + 1);
        }

        //save cart to DB
        Cart savedCart = cartRepository.save(cart);

        //get detail in cart ,
        CartDetail newCartDetail = cartDetailsRepository.findByProductIdAndCartId(product.getId()
                , cart.getId()).orElse(null);

        //if not exist create new cart detail
        if (newCartDetail == null) {
            newCartDetail = new CartDetail();
            newCartDetail.setCart(cart);
            newCartDetail.setProduct(product);
        }
        newCartDetail.setPrice(product.getPrice());
        newCartDetail.setQuantity((newCartDetail.getQuantity()) + request.getQuantity());
        newCartDetail.setTotalPrice((newCartDetail.getPrice()* newCartDetail.getQuantity()));

        //save cart detail to db
        cartDetailsRepository.save(newCartDetail);

        //calculate new total prices of product in cart
        savedCart.setTotalPrice( cartDetailsRepository.sumTotalPriceByCartId(cart.getId()));
        savedCart = cartRepository.save(savedCart);

        //calculate quantity and create response
        int quantity = cartDetailsRepository.sumQuantityByCardId(savedCart.getId());
        CartAddResponse response = new CartAddResponse();
        response.setQuantity(quantity);
        response.setVersionNo(cart.getVersionNo());
        response.setToken(cart.getToken());
        return response;
    }
    @Transactional
    public CartInfoResponse getCartInfo(CartRequest request, Authentication authentication) {
        User loginUser;
        Cart cart = null;
        if (authentication != null) {  //check if there are user login
            loginUser = (User) authentication.getPrincipal();
            cart = cartRepository.findByOwnerId(loginUser.getId()).orElse(null);
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

    @Transactional
    public CartSyncResponse synccart(CartRequest request, User user) {
        int totalQuantity = 0;
        Cart tokenCart = cartRepository.findByToken(request.getToken()).orElse(null);
        // if cart when unauthenticated exist
        if (tokenCart != null) {
            Cart userCart = cartRepository.findByOwnerId(user.getId()).orElse(null);

            //if login user has cart
            if (userCart != null) {
                //update user cart
                List<CartDetail> tokenCartDetails = cartDetailsRepository.findByCartId(tokenCart.getId());
                List<CartDetail> userCartDetails = cartDetailsRepository.findByCartId(userCart.getId());

                tokenCartDetails.stream().forEach(tokenCartDetail -> {
                    //get index of detail with matching product id (override equal)
                    int index = userCartDetails.indexOf(tokenCartDetail);
                    //if matching detail exist
                    if( index != -1){
                        //update quantity and total price
                        CartDetail detail =  userCartDetails.get(index);

                        detail.setPrice(detail.getProduct().getPrice());
                        detail.setQuantity(detail.getQuantity() +tokenCartDetail.getQuantity());
                        detail.setTotalPrice(detail.getPrice()*detail.getQuantity());
                    } else {//if not exist
                        Product product = tokenCartDetail.getProduct();
                        // create new cart detail
                        CartDetail newDetail = new CartDetail();

                        newDetail.setCart(userCart);
                        newDetail.setProduct(product);
                        newDetail.setPrice(product.getPrice());
                        newDetail.setQuantity(tokenCartDetail.getQuantity());
                        newDetail.setTotalPrice(newDetail.getPrice()*newDetail.getQuantity());
                        //add new detail to user cart details
                        userCartDetails.add(newDetail);
                    }
                });
                //delete cart when unauthenticated
                cartDetailsRepository.deleteAll(tokenCartDetails);
                cartRepository.delete(tokenCart);
                //save changes to user cart details
                cartDetailsRepository.saveAll(userCartDetails);

                //update user cart
                userCart.setTotalPrice(cartDetailsRepository.sumTotalPriceByCartId(userCart.getId()));
                userCart.setVersionNo(userCart.getVersionNo()+1);
                Cart savedCart = cartRepository.save(userCart);
                totalQuantity = cartDetailsRepository.sumQuantityByCardId(savedCart.getId());

            } else { // if login user has no cart
                //set cart when unauthenticated owner to user
                tokenCart.setToken(null);
                tokenCart.setOwner(user);
                tokenCart.setVersionNo(tokenCart.getVersionNo() + 1);

                Cart savedcart = cartRepository.save(tokenCart);
                totalQuantity = cartDetailsRepository.sumQuantityByCardId(savedcart.getId());
            }
        }

        CartSyncResponse response = new CartSyncResponse();
        response.setTotalQuantity(totalQuantity);
        return response;
    }
}
