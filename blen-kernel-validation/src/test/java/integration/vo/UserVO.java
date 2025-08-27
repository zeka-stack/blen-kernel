package integration.vo;

import dev.dong4j.zeka.kernel.common.base.BaseVO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertFalse;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.io.Serial;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
@EqualsAndHashCode(callSuper = true)
public class UserVO extends BaseVO<Long> {

    /** serialVersionUID */
    @Serial
    private static final long serialVersionUID = 1063735110827253744L;
    /** Name */
    @NotBlank(message = "名称为必填项")
    @Size(max = 20)
    private String name;
    /** Age */
    @NotBlank(message = "年龄不能为空")
    @Pattern(regexp = "^[0-9]{1,2}$", message = "年龄不正确")
    private String age;
    /** Is false */
    @AssertFalse(message = "必须为false")
    private Boolean isFalse;
    /** 如果是空,则不校验,如果不为空,则校验 */
    @Pattern(regexp = "^[0-9]{4}-[0-9]{2}-[0-9]{2}$", message = "出生日期格式不正确")
    private String birthday;
    /** Address */
    @NotBlank(message = "地址为必填项")
    private String address;
    /** Email */
    @NotBlank(message = "邮箱为必填项")
    @Email(message = "邮箱格式错误")
    private String email;
    /** 嵌套验证, 必须使用 @Valid */
    @Valid
    @NotNull(message = "role 不能为空")
    private RoleVO role;
}
