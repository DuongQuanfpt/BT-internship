package finalproject.group1.BE.domain.services;

import finalproject.group1.BE.commons.Constants;
import finalproject.group1.BE.commons.EmailCommons;
import finalproject.group1.BE.domain.entities.*;
import finalproject.group1.BE.domain.enums.DeleteFlag;
import finalproject.group1.BE.domain.enums.OrderStatus;
import finalproject.group1.BE.domain.enums.Role;
import finalproject.group1.BE.domain.repository.*;
import finalproject.group1.BE.web.dto.data.image.ImageData;
import finalproject.group1.BE.web.dto.request.order.CreateOrderRequest;
import finalproject.group1.BE.web.dto.request.order.SearchOrderRequest;
import finalproject.group1.BE.web.dto.request.order.UpdateOrderRequest;
import finalproject.group1.BE.web.dto.response.order.CreateOrderResponse;
import finalproject.group1.BE.web.dto.response.order.OrderDetailResponse;
import finalproject.group1.BE.web.dto.response.order.OrderResponse;
import finalproject.group1.BE.web.dto.response.order.OrderSearchResponse;
import finalproject.group1.BE.web.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    private final OrderDetailRepository orderDetailRepository;

    private final OrderShippingDetailRepository orderShippingDetailRepository;

    private final CartRepository cartRepository;

    private final CartDetailsRepository cartDetailsRepository;

    private final CityRepository cityRepository;

    private final ImageRepository imageRepository;

    private final DistrictRepository districtRepository;

    private final EmailCommons emailCommons;
    private final ModelMapper modelMapper;
    @Value("${manager.email}")
    private String managerEmail;

    /**
     * create order
     *
     * @param request - contain new order data
     * @param user    - login user
     * @return product quantity in order and the order display id
     */
    @Transactional
    public CreateOrderResponse createOrder(CreateOrderRequest request, User user) {
        City city = cityRepository.findById(request.getCity())
                .orElseThrow(() -> new NotFoundException("city not found"));
        District district = districtRepository.findByIdAndCityId(request.getDistrict(), city.getId())
                .orElseThrow(() -> new NotFoundException("district not found"));
        Cart cart = cartRepository.findByOwnerIdAndVersionNo(user.getId(), request.getVersionNo())
                .orElseThrow(() -> new NotFoundException("cart not found"));

        List<CartDetail> cartDetailList = cartDetailsRepository.findByCartId(cart.getId());
        //get details with deleted product
        List<CartDetail> detailsOfDeletedProduct = cartDetailList.stream()
                .filter(cartDetail -> cartDetail.getProduct().getDeleteFlag() == DeleteFlag.DELETED).toList();
        //if details with deleted product exist
        if (!detailsOfDeletedProduct.isEmpty()) {
            String s = detailsOfDeletedProduct.stream()
                    .map(cartDetail -> cartDetail.getProduct().getOldSku())
                    .collect(Collectors.joining(","));
            String msg = String.format(Constants.PRODUCT_NOT_AVAILABLE, s);
            throw new NotFoundException(msg);
        }
        //create new order
        Order order = new Order();
        order.setDisplayId("");
        order.setOwner(user);
        order.setStatus(OrderStatus.NEW);
        order.setOrderDate(LocalDate.now());
        order.setTotalPrice((float) cartDetailList.stream()
                .mapToDouble(detail -> detail.getQuantity() * detail.getProduct().getPrice()).sum());

        //save order
        Order savedOrder = orderRepository.save(order);
        savedOrder.setDisplayId(String.format(Constants.DISPLAY_ID_FORMAT, order.getId()));
        savedOrder = orderRepository.save(savedOrder);

        //create order details
        Order finalSavedOrder = savedOrder;
        List<OrderDetail> newOrderDetails = cartDetailList.stream().map(cartDetail -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(finalSavedOrder);
            orderDetail.setProduct(cartDetail.getProduct());
            orderDetail.setPrice(cartDetail.getProduct().getPrice());
            orderDetail.setQuantity(cartDetail.getQuantity());
            orderDetail.setTotalPrice(orderDetail.getPrice() * orderDetail.getQuantity());

            return orderDetail;
        }).toList();
        orderDetailRepository.saveAll(newOrderDetails);

        //create order shipping detail
        OrderShippingDetail shippingDetail = new OrderShippingDetail();
        shippingDetail.setPhoneNumber(request.getPhoneNumber());
        shippingDetail.setAddress(request.getAddress());
        shippingDetail.setOrder(order);
        shippingDetail.setCity(city);
        shippingDetail.setDistrict(district);

        orderShippingDetailRepository.save(shippingDetail);

        //delete user cart
        cartDetailsRepository.deleteAll(cartDetailList);
        cartRepository.delete(cart);

        //send email to user and manager
        String[] toEmails = {managerEmail,user.getEmail()};
        String emailContent = String.format(Constants.ORDER_EMAIL_CONTENT, order.getDisplayId());
        emailCommons.sendSimpleMessage(toEmails, Constants.ORDER_EMAIL_SUBJECT, emailContent);

        // create response
        CreateOrderResponse response = new CreateOrderResponse();
        response.setDisplayId(savedOrder.getDisplayId());
        response.setTotalPrice(savedOrder.getTotalPrice());
        return response;
    }

    /**
     * get order by request
     *
     * @param request
     * @param loginUser - the login user
     * @param pageable  - pagination criteria
     * @return order list
     */
    public OrderSearchResponse searchOrder(SearchOrderRequest request, User loginUser, Pageable pageable) {

        LocalDate orderDate = null;
        if (request.getOrderDate() != null) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.VALID_DATE_FORMAT);
                orderDate = LocalDate.parse(request.getOrderDate(), formatter);
            } catch (DateTimeParseException e) {
                //do nothing
            }
        }

        Integer loginUserId = null;
        //if login user role is user , search order by login user id
        if (Role.ROLE_USER == loginUser.getRole()) {
            loginUserId = loginUser.getId();
        }

        OrderStatus status = null;
        if (request.getStatus() != null) {
            status = OrderStatus.getOrderStatus(Integer.valueOf(request.getStatus()));
        }

        //get order by search condition
        List<Order> result = orderRepository.findOrderBySearchConditions(
                request.getProductName(), request.getSku(), request.getOrderId(),
                orderDate, status, request.getUserName(), loginUserId, pageable);
        //create response
        List<OrderResponse> orderResponses = result.stream().map(order -> {
            OrderResponse orderResponse = modelMapper.map(order, OrderResponse.class);
            orderResponse.setUsername(order.getOwner().getName());
            orderResponse.setShippingDistrict(order.getShippingDetail().getDistrict().getName());
            orderResponse.setShippingCity(order.getShippingDetail().getCity().getName());
            // set details response
            List<OrderDetailResponse> detailResponses = orderDetailRepository.findByOrderId(order.getId())
                    .stream().map(orderDetail -> {
                        Product product = orderDetail.getProduct();
                        OrderDetailResponse detailResponse = modelMapper.map(orderDetail, OrderDetailResponse.class);
                        //if product exist
                        if (product.getDeleteFlag() != DeleteFlag.DELETED) {
                            ImageData imageData = imageRepository.findProductThumbnail(product.getId());
                            detailResponse.setImagePath(imageData.getPath());
                            detailResponse.setImageName(imageData.getName());
                            detailResponse.setStatus(product.getDeleteFlag().name());
                        }
                        detailResponse.setStatus(product.getDeleteFlag().name());

                        return detailResponse;
                    })
                    .toList();
            orderResponse.setDetails(detailResponses);

            return orderResponse;
        }).toList();

        OrderSearchResponse response = new OrderSearchResponse();
        response.setOrders(orderResponses);
        return response;
    }

    /**
     * update order by request
     * @param updateOrderRequest
     */
    @Transactional
    public void updateOrder(UpdateOrderRequest updateOrderRequest, Authentication authentication) {
        User loginUser = null;
        if (authentication == null) {  //check if there are user login
          throw new NotFoundException("login user not found");
        }
        loginUser = (User) authentication.getPrincipal();
        if (OrderStatus.getOrderStatus(updateOrderRequest.getStatus()) == null) {
            throw new NotFoundException("Not Found Status !!!");
        }

        if (loginUser.getRole() == Role.ROLE_ADMIN) {
            Optional<Order> orderOptional = orderRepository.findByIdAndDisplayId(updateOrderRequest.getId(), updateOrderRequest.getDisplayId());
            Order order = orderOptional.orElseThrow(() -> new NotFoundException("Order not found with id and displayId"));

            // Update the order status
            order.setStatus(OrderStatus.getOrderStatus(updateOrderRequest.getStatus()));
            orderRepository.save(order);

        }
        else if (loginUser.getRole() == Role.ROLE_USER) {
            Optional<Order> orderOptional = orderRepository.findByOwnerIdAndDisplayId(loginUser.getId(), updateOrderRequest.getDisplayId());
            Order order = orderOptional.orElseThrow(() -> new NotFoundException("Order not found with userId and displayId"));

            // Update the order status
            order.setStatus(OrderStatus.getOrderStatus(updateOrderRequest.getStatus()));
            orderRepository.save(order);
        }
    }
}
