package finalproject.group1.BE.domain.services;

import finalproject.group1.BE.commons.EmailCommons;
import finalproject.group1.BE.domain.entities.*;
import finalproject.group1.BE.domain.enums.OrderStatus;
import finalproject.group1.BE.domain.repository.*;
import finalproject.group1.BE.web.dto.request.order.CreateOrderRequest;
import finalproject.group1.BE.web.dto.response.order.CreateOrderResponse;
import finalproject.group1.BE.web.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
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

    private final DistrictRepository districtRepository;

    private final EmailCommons emailCommons;
    @Value("${manager.email}")
    private String managerEmail;

    @Transactional
    public CreateOrderResponse createOrder(CreateOrderRequest request, User user) {
        City city = cityRepository.findById(request.getCity())
                .orElseThrow(() -> new NotFoundException("city not found"));
        District district = districtRepository.findByIdAndCityId(request.getDistrict(), city.getId())
                .orElseThrow(() -> new NotFoundException("district not found"));
        Cart cart = cartRepository.findByOwnerIdAndVersionNo(user.getId(), request.getVersionNo())
                .orElseThrow(() -> new NotFoundException("cart not found"));

        List<CartDetail> cartDetailList = cartDetailsRepository.findByCartId(cart.getId());
        //create new order
        Order order = new Order();
        order.setDisplayId("");
        order.setOwner(user);
        order.setStatus(OrderStatus.NEW);
        order.setOrderDate(LocalDate.now());
        order.setTotalPrice((float) cartDetailList.stream()
                .mapToDouble(detail -> detail.getTotalPrice()).sum());

        //save order
        Order savedOrder = orderRepository.save(order);
        order.setDisplayId(String.format("B" + "%04d", order.getId()));
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
        }).collect(Collectors.toList());
        orderDetailRepository.saveAll(newOrderDetails);

        //create order shipping detail
        OrderShippingDetail shippingDetail = new OrderShippingDetail();
        shippingDetail.setPhoneNumber(request.getPhoneNumber());
        shippingDetail.setAddress(request.getAddress());
        shippingDetail.setOrder(order);
        shippingDetail.setCity(city);
        shippingDetail.setDistrict(district);

        orderShippingDetailRepository.save(shippingDetail);

        //delete cart
        cartDetailsRepository.deleteAll(cartDetailList);
        cartRepository.delete(cart);

        //send email to user and manager
        String emailContent = "Order " + savedOrder.getDisplayId() + " have been placed";
        emailCommons.sendSimpleMessage(user.getEmail(),"Order placed",emailContent);
        emailCommons.sendSimpleMessage(managerEmail,"Order placed",emailContent);

        // create response
        CreateOrderResponse response = new CreateOrderResponse();
        response.setDisplayId(savedOrder.getDisplayId());
        response.setTotalPrice(savedOrder.getTotalPrice());
        return response;
    }
}
