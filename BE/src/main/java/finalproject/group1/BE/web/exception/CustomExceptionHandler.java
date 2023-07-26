package finalproject.group1.BE.web.exception;

import finalproject.group1.BE.web.dto.response.error.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(ExistException.class)
    public ResponseEntity ExistExceptionHandler(ExistException exception, HttpServletRequest request) {
        ErrorResponse response = new ErrorResponse("400","Already Exist", exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler({DisabledException.class, LockedException.class})
    public ResponseEntity DisabledAndLockedHandler(Exception exception, HttpServletRequest request) {
        ErrorResponse response = new ErrorResponse("401","","User have been deleted");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler({BadCredentialsException.class, InternalAuthenticationServiceException.class})
    public ResponseEntity BadCredentialHandler(Exception exception, HttpServletRequest request) {
        ErrorResponse response = new ErrorResponse("401","Bad credential","Incorrect email or password");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity NotFoundHandler(NotFoundException exception, HttpServletRequest request) {
        ErrorResponse response = new ErrorResponse("400","Not Found",exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(UserLockException.class)
    public ResponseEntity UserLockHandler(UserLockException exception, HttpServletRequest request) {
        ErrorResponse response = new ErrorResponse("400","locked","user locked");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(DeleteCategoryException.class)
    public ResponseEntity DeleteCategoryHandler(DeleteCategoryException exception, HttpServletRequest request) {
        ErrorResponse response = new ErrorResponse("400","", exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity IllegalArgumentHandler(IllegalArgumentException exception, HttpServletRequest request) {
        ErrorResponse response = new ErrorResponse("400","Illegal Argument", exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }


    /**
     * Handle for validate exception, return error message
     * Example for return response from BindingResult
     *
     * @param ex exception
     * @return response
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity ValidationHandler(ValidationException ex) {
        BindingResult bindingResult = ex.getBindingResult();

        ErrorResponse re = new ErrorResponse("400",
                ex.getMessage(),
                "Invalid input");

        List<ErrorResponse> details = new ArrayList<>();
        bindingResult.getAllErrors().forEach((objectError -> {
            ErrorResponse newDetail;
            if (objectError instanceof FieldError fieldError) {
                newDetail = new ErrorResponse(fieldError.getCode(), fieldError.getField(), fieldError.getDefaultMessage());
            } else {
                newDetail = new ErrorResponse(objectError.getCode(), "", objectError.getDefaultMessage());
            }
            details.add(newDetail);
        }));
        re.withDetails(details);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(re);
    }

}
