package team.combinatorics.shuwashuwa.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author kinami
 * @version 0.0.1
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("更新用户信息时传输的数据")
public class UserInfoUpdateDTO {
    @ApiModelProperty("用户姓名")
    private String userName;
    @ApiModelProperty("用户手机号")
    private String phoneNumber;
    @ApiModelProperty("用户邮箱")
    private String email;
    @ApiModelProperty("用户身份")
    private String identity;
    @ApiModelProperty("用户所在部门")
    private String department;
    @ApiModelProperty("年级")
    private String grade;
    @ApiModelProperty("学号")
    private String studentId;
    @ApiModelProperty("备注")
    private String comment;
}
