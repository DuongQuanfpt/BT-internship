package finalproject.group1.BE.domain.enums;

import lombok.Getter;

@Getter
public enum OrderStatus {
    NEW(1),
    INPROGRESS(11),
    DELIVERING(21),
    DELIVERED(31),
    RETURNED(41),
    CANCELED(91);

    private Integer value;

    private OrderStatus(Integer value) {
        this.value = value;
    }

    public static OrderStatus getOrderStatus(Integer value) {
        if(value == 1) {
            return OrderStatus.NEW;
        }
        else if(value == 11) {
            return OrderStatus.INPROGRESS;
        }
        else if(value == 21) {
            return OrderStatus.DELIVERING;
        }
        else if(value == 31) {
            return OrderStatus.DELIVERED;
        }
        else if(value == 41) {
            return OrderStatus.RETURNED;
        }
        else if (value == 91) {
            return OrderStatus.CANCELED;
        }
        else {
            return null;
        }
    }
}
