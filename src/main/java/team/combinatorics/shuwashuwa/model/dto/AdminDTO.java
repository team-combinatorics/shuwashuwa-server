package team.combinatorics.shuwashuwa.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("添加或修改管理员信息时要上传的数据")
public class AdminDTO {
    @ApiModelProperty("新增管理员的用户id")
    private Integer userid;

    @ApiModelProperty("管理员（真实）姓名")
    private String userName;

    @ApiModelProperty("管理员手机号")
    private String phoneNumber;

    @ApiModelProperty("管理员邮箱地址")
    private String email;

    @ApiModelProperty("管理员身份")
    private String identity;

    @ApiModelProperty("管理员所在学部")
    private String department;

    @ApiModelProperty("管理员学号")
    private String studentId;
}
