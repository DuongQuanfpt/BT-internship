package finalproject.group1.BE.web.dto.request.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * dto reset password
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PasswordResetRequest {
    /**
     * change password token
     */
    private String token;
}
