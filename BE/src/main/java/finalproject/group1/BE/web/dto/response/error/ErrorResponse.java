package finalproject.group1.BE.web.dto.response.error;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
/**
 * Sample return error message that can be used to FE to display error on a target
 */
@Data
@Builder
public class ErrorResponse {
    private final String statusCode;
    private final String code;
    private final String message;
    private final String target;
    private final List<ErrorResponse> details = new ArrayList<>();

    public ErrorResponse(String statusCode, String code, String message) {
        this(statusCode, code, message, null);
    }

    public ErrorResponse(String statusCode, String code, String message, String target) {
        this.statusCode = statusCode;
        this.code = code;
        this.message = message;
        this.target = target;
    }

    public void withDetails(List<ErrorResponse> details) {
        this.details.clear();
        this.details.addAll(details);
    }
}
