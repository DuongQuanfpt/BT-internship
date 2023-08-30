package finalproject.group1.BE.web.dto.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ResponseDataDTO<T> extends ResponseDTO {
    /** data */
    private T data;

    public static <T> ResponseDataDTO<T> success(T data) {
        ResponseDataDTO<T> res = new ResponseDataDTO<T>();
        res.setHttpStatus(HttpStatus.OK);
        res.setStatus(res.getHttpStatus().value());;
        res.data = data;
        return res;
    }

    /**
     * Set data for the response.
     *
     * @param data response data
     * @return ResponseDto
     */
    public ResponseDataDTO<T> withData(T data) {
        this.data = data;
        return this;
    }

}
