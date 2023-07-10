package finalproject.group1.BE.web.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(ExistException.class)
    public ResponseEntity ExistExceptionHandler(ExistException exception , HttpServletRequest request){
//        String language = "en";
//        UserAlreadyExistException existException = (UserAlreadyExistException) exception;
//        return  ResponseEntity.ok(messageSource.getMessage("E002"
//                ,new Object[]{existException.getUsername(),"0"}
//                ,new Locale(language)));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(" already exist");
    }

    @ExceptionHandler({DisabledException.class , LockedException.class})
    public ResponseEntity DisabledAndLockedHandler(Exception exception,HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("user have been deleted");
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity NotFoundHandler(NotFoundException exception , HttpServletRequest request){
//        String language = "en";
//        UserAlreadyExistException existException = (UserAlreadyExistException) exception;
//        return  ResponseEntity.ok(messageSource.getMessage("E002"
//                ,new Object[]{existException.getUsername(),"0"}
//                ,new Locale(language)));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(" not found");
    }

}
