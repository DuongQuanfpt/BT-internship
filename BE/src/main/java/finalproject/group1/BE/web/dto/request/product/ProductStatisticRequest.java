package finalproject.group1.BE.web.dto.request.product;

import finalproject.group1.BE.web.annotation.ValidDateFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductStatisticRequest {
    @ValidDateFormat()
    String date;
}
