package finalproject.group1.BE.web.dto.request.order;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * dto request create order
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {
    /**
     * buyer phone number
     */
    @NotEmpty
    @Size(min = 10 , max = 11)
    @Pattern(regexp = "^[0]{1}[0-9]{9,10}",message = "not a phone number")
    String phoneNumber;

    /**
     * buyer address
     */
    @NotEmpty
    @Size(max = 255)
    String address;

    /**
     * city id
     */
    @NotNull
    int city;

    /**
     * district id
     */
    @NotNull
    int district;

    /**
     * cart version
     */
    @NotNull
    int versionNo;
}
