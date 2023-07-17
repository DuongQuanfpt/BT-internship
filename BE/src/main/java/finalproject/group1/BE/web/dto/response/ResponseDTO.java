package finalproject.group1.BE.web.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * DTO wrapper response for all APIs.<br>
 * Format will be: { code: ..., message: ..., data: ... }
 * complete swagger document and validation
 *
 * @param <T> data type mixed
 */
@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDTO<T> {
    /** HTTP Status */
    @JsonIgnore
    private HttpStatus httpStatus = HttpStatus.OK;

    /** HTTP headers */
    @JsonIgnore
    private HttpHeaders headers;
    /** HTTP status code */

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Integer status = HttpStatus.OK.value();

    /** Response code */
    private String code;

    /** Response Message */
    private String message = "OK";

    /** data */
    private T data;

    /**
     * Create new instant of ResponseDto.
     *
     * @param <T> data type mixed
     * @return new instant
     */
    public static <T> ResponseDTO<T> build() {
        return new ResponseDTO<>();
    }

    /**
     * success response.
     *
     * @param data data
     * @return ResponseDto
     */
    public static <T> ResponseDTO<T> success(T data) {
        ResponseDTO<T> res = new ResponseDTO<T>();
        res.httpStatus = HttpStatus.OK;
        res.status = res.httpStatus.value();
        res.data = data;
        return res;
    }

    /**
     * Set HttpStatus for the response.
     *
     * @param httpStatus http code
     * @return ResponseDto
     */
    public ResponseDTO<T> withHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
        this.status = httpStatus.value();
        return this;
    }

    /**
     * Set data for the response.
     *
     * @param data response data
     * @return ResponseDto
     */
    public ResponseDTO<T> withData(T data) {
        this.data = data;
        return this;
    }

    /**
     * Set HttpHeaders for the response
     *
     * @param httpHeaders the headers
     * @return ResponseDto
     */
    public ResponseDTO<T> withHttpHeaders(HttpHeaders httpHeaders) {
        this.headers = httpHeaders;
        return this;
    }

    /**
     * Set message for the response.
     *
     * @param message the messages
     * @return ResponseDto
     */
    public ResponseDTO<T> withMessage(String message) {
        this.message = message;
        return this;
    }

    /**
     * Set message with code for the response.
     *
     * @param code    message code
     * @param message the message
     * @return ResponseDto
     */
    public ResponseDTO<T> withMessage(String code, String message) {
        this.code = code;
        this.message = message;
        return this;
    }

    /**
     * Convert to standard ResponseEntity.
     *
     * @return ResponseEntity
     */
    public ResponseEntity<ResponseDTO<T>> toResponseEntity() {
        return new ResponseEntity<ResponseDTO<T>>(this, this.httpStatus);
    }
}
