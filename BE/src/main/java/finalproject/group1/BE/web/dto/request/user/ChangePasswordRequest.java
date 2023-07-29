package finalproject.group1.BE.web.dto.request.user;

import finalproject.group1.BE.web.annotation.UppercaseAndNumber;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordRequest {
    /**
     * user's old password
     */
    @NotEmpty
    @Size(min = 8, max = 32)
    @UppercaseAndNumber(message = "Must contain an uppercase and a number")
    private String oldPassword;
    /**
     * user's new password
     */
    @NotEmpty
    @Size(min = 8, max = 32)
    @UppercaseAndNumber(message = "Must contain an uppercase and a number")
    private String password;
}
