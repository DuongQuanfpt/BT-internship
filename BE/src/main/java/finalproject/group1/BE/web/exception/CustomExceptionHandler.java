package finalproject.group1.BE.web.exception;

import finalproject.group1.BE.web.dto.response.error.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(ExistException.class)
    public ResponseEntity<ErrorResponse> existExceptionHandler(ExistException exception, HttpServletRequest request) {
        ErrorResponse response = new ErrorResponse("400","Already Exist", exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(CsvValidateException.class)
    public ResponseEntity<ErrorResponse> csvExceptionHandler(CsvValidateException exception, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getErrorResponse());
    }

    @ExceptionHandler({DisabledException.class, LockedException.class})
    public ResponseEntity<ErrorResponse> disabledAndLockedHandler(Exception exception, HttpServletRequest request) {
        ErrorResponse response = new ErrorResponse("401","","User have been deleted");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler({BadCredentialsException.class, InternalAuthenticationServiceException.class})
    public ResponseEntity<ErrorResponse> badCredentialHandler(Exception exception, HttpServletRequest request) {
        exception.printStackTrace();
        ErrorResponse response = new ErrorResponse("401","Bad credential","Incorrect email or password");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> notFoundHandler(NotFoundException exception, HttpServletRequest request) {
        ErrorResponse response = new ErrorResponse("400","Not Found",exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(UserLockException.class)
    public ResponseEntity<ErrorResponse> userLockHandler(UserLockException exception, HttpServletRequest request) {
        ErrorResponse response = new ErrorResponse("400","locked","user locked");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(DeleteCategoryException.class)
    public ResponseEntity<ErrorResponse> deleteCategoryHandler(DeleteCategoryException exception, HttpServletRequest request) {
        ErrorResponse response = new ErrorResponse("400","", exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> illegalArgumentHandler(IllegalArgumentException exception, HttpServletRequest request) {
        ErrorResponse response = new ErrorResponse("400","Illegal Argument", exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(CustomMessagingException.class)
    public ResponseEntity<ErrorResponse> customMessagingExceptionHandler(CustomMessagingException exception, HttpServletRequest request) {
        ErrorResponse response = new ErrorResponse("400","MessagingException", exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    @ExceptionHandler(CustomIOException.class)
    public ResponseEntity<ErrorResponse> customMIOExceptionHandler(CustomIOException exception, HttpServletRequest request) {
        ErrorResponse response = new ErrorResponse("400","IOException", exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(GoogleDriveException.class)
    public ResponseEntity<ErrorResponse> googleDriveExceptionHandler(GoogleDriveException exception, HttpServletRequest request) {
        ErrorResponse response = new ErrorResponse("400","GoogleDriveException", exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(CustomInterruptedException.class)
    public ResponseEntity<ErrorResponse> customInterruptedExceptionHandler(CustomInterruptedException exception, HttpServletRequest request) {
        ErrorResponse response = new ErrorResponse("400","CustomInterruptedException", exception.getMessage());
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
    public ResponseEntity<ErrorResponse> validationHandler(ValidationException ex) {
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
