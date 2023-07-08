package finalproject.group1.BE.web.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;

@RestControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(UserExistException.class)
    public ResponseEntity UserExistExceptionHandler(UserExistException exception , HttpServletRequest request){
//        String language = "en";
//        UserAlreadyExistException existException = (UserAlreadyExistException) exception;
//        return  ResponseEntity.ok(messageSource.getMessage("E002"
//                ,new Object[]{existException.getUsername(),"0"}
//                ,new Locale(language)));
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("email already exist");
    }

    @ExceptionHandler(UserLockException.class)
    public ResponseEntity UserLockExceptionHandler(UserLockException exception , HttpServletRequest request){
//        String language = "en";
//        UserAlreadyExistException existException = (UserAlreadyExistException) exception;
//        return  ResponseEntity.ok(messageSource.getMessage("E002"
//                ,new Object[]{existException.getUsername(),"0"}
//                ,new Locale(language)));
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("user is locked");
    }
}
