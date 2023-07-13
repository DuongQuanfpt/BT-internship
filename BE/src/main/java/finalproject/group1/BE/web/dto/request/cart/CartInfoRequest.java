package finalproject.group1.BE.web.dto.request.cart;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartInfoRequest {

    @Size(min = 20 , max = 20)
    String token;
}
