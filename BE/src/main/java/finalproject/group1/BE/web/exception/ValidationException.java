package finalproject.group1.BE.web.exception;

import lombok.Getter;
import org.springframework.validation.BindingResult;

import java.io.IOException;

@Getter
public class ValidationException extends RuntimeException{
    BindingResult bindingResult;

    public ValidationException(BindingResult bindingResult) {
        this.bindingResult = bindingResult;
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
