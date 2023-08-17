package finalproject.group1.BE.web.exception;

public class InvalidCsvException extends RuntimeException{
    public InvalidCsvException(String message) {
        super(message);
    }
}
