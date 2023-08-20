package finalproject.group1.BE.web.exception;

import finalproject.group1.BE.web.dto.response.error.ErrorResponse;
import lombok.Getter;

@Getter
public class CsvValidateException extends RuntimeException{
    private ErrorResponse errorResponse;
    public CsvValidateException(String message) {
        super(message);
    }

    public CsvValidateException(ErrorResponse errorResponse) {
        this.errorResponse = errorResponse;
    }
}
