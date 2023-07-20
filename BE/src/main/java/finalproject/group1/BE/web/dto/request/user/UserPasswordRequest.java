package finalproject.group1.BE.web.dto.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * dto request password
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserPasswordRequest {
    /**
     * user email to sent reset passsword link
     */
    @NotEmpty
    @Email
    @Size(max = 255)
    private String email;
}
