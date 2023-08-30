package finalproject.group1.BE.web.exception;

import finalproject.group1.BE.web.dto.response.error.ErrorResponse;
import lombok.Getter;

import java.io.IOException;

@Getter
public class CsvValidateException extends RuntimeException{
    private ErrorResponse errorResponse;
    public CsvValidateException(String message) {
        super(message);
    }

    public CsvValidateException(ErrorResponse errorResponse) {
        this.errorResponse = errorResponse;
    }

    private void writeObject(java.io.ObjectOutputStream stream)
            throws IOException {
        stream.defaultWriteObject();
    }

    private void readObject(java.io.ObjectInputStream stream)
            throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
    }
}

