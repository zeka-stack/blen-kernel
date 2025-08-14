package integration.vo;

import dev.dong4j.zeka.kernel.common.base.BaseVO;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

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
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class RoleVO extends BaseVO<Long> {

    /** serialVersionUID */
    private static final long serialVersionUID = 1746952410946351778L;
    /** Role */
    private String role;
    /** Role name */
    @NotEmpty(message = "至少存在一个角色")
    private List<String> roleName;
}
