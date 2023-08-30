package finalproject.group1.BE.web.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

/**
 * DTO wrapper response for all APIs.<br>
 * Format will be: { code: ..., message: ..., data: ... }
 * complete swagger document and validation
 *
 */
@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDTO {
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

    /**
     * Create new instant of ResponseDto.
     *
     * @return new instant
     */
    public static ResponseDTO build() {
        return new ResponseDTO();
    }

    /**
     * success response.
     *
     * @return ResponseDto
     */
    public static ResponseDTO success() {
        ResponseDTO res = new ResponseDTO();
        res.httpStatus = HttpStatus.OK;
        res.status = res.httpStatus.value();
        return res;
    }

    /**
     * Set HttpStatus for the response.
     *
     * @param httpStatus http code
     * @return ResponseDto
     */
    public ResponseDTO withHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
        this.status = httpStatus.value();
        return this;
    }

    /**
     * Set HttpHeaders for the response
     *
     * @param httpHeaders the headers
     * @return ResponseDto
     */
    public ResponseDTO withHttpHeaders(HttpHeaders httpHeaders) {
        this.headers = httpHeaders;
        return this;
    }

    /**
     * Set message for the response.
     *
     * @param message the messages
     * @return ResponseDto
     */
    public ResponseDTO withMessage(String message) {
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
    public ResponseDTO withMessage(String code, String message) {
        this.code = code;
        this.message = message;
        return this;
    }

}
