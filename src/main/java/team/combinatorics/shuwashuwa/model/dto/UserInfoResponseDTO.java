package team.combinatorics.shuwashuwa.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("查询用户信息时返回的数据")
public class UserInfoResponseDTO {
    @ApiModelProperty(value = "注册时间，以yyyy-MM-dd HH:mm:ss表示", example = "1926-08-17 11:45:14")
    private String createTime;
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
    @ApiModelProperty("是否是志愿者")
    private Boolean volunteer;
    @ApiModelProperty("是否是管理员")
    private Boolean admin;
}
