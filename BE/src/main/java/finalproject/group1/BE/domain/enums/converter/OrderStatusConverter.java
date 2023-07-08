package finalproject.group1.BE.domain.enums.converter;

import finalproject.group1.BE.domain.enums.OrderStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class OrderStatusConverter implements AttributeConverter<OrderStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(OrderStatus orderStatus) {
        return orderStatus.getValue();
    }

    @Override
    public OrderStatus convertToEntityAttribute(Integer value) {
        try {
            return OrderStatus.getOrderStatus(value);
        }
        catch(NullPointerException ex) {
            throw ex;
        }
    }
}
