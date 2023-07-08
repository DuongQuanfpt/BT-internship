package finalproject.group1.BE.web.dto.request;

import finalproject.group1.BE.web.annotation.UppercaseAndNumber;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserLoginRequest {
    /**
     * user email
     */
    @NotEmpty
    @Email
    @Size(max = 255)
    String email;

    /**
     * user password
     */
    @NotEmpty
    @Size(min = 8, max = 32)
    @UppercaseAndNumber(message = "Must contain an uppercase and a number")
    String password;
}
