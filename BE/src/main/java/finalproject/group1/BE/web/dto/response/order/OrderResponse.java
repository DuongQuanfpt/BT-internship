package finalproject.group1.BE.web.dto.response.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import finalproject.group1.BE.commons.Constants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    int id;
    String displayId;
    String username;
    float totalPrice;

    @JsonFormat(pattern= Constants.VALID_DATE_FORMAT)
    LocalDate orderDate;
    String orderStatus;
    String shippingAddress;
    String shippingDistrict;
    String shippingCity;
    String shippingPhoneNumber;
    List<OrderDetailResponse> details;
}
