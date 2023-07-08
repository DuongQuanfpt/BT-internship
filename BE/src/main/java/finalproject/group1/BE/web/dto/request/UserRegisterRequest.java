package finalproject.group1.BE.web.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import finalproject.group1.BE.web.annotation.UppercaseAndNumber;
import finalproject.group1.BE.web.annotation.ValidDateFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

/**
 * dto for user registration
 */
@Getter
@Setter
@AllArgsConstructor
public class UserRegisterRequest {
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


    /**
     * user name
     */
    @NotEmpty
    @Size(min = 8, max = 255)
    String username;

    /**
     * user birthday
     */
    @NotNull
    @ValidDateFormat(message = "invalid date format")
    String birthDay;
}
