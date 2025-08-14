package integration.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 18:19
 * @since 1.0.0
 */
@Data
public class RequestForm {

    /** Name */
    @NotNull
    @Size(min = 1, max = 5)
    private String name;

    /** Password */
    @NotNull
    @Size(min = 1, max = 5)
    private String password;

}
