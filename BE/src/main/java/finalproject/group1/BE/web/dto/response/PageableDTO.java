package finalproject.group1.BE.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageableDTO {
    /**
     * total of pages
     */
    private Integer totalPages;
    /**
     * the number of elements have in a page
     */
    private Integer pageSize;
    /**
     * the number of current page
     */
    private Integer pageNumber;
}
