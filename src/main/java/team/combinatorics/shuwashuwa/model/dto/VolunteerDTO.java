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
@ApiModel(description = "志愿者的用户信息")
public class VolunteerDTO {
    @ApiModelProperty("志愿者的用户id")
    private Integer userid;

    @ApiModelProperty("志愿者的用户的真实姓名")
    private String userName;

    @ApiModelProperty("成为志愿者的用户的手机号")
    private String phoneNumber;

    @ApiModelProperty("成为志愿者的用户的邮箱")
    private String email;

    @ApiModelProperty("成为志愿者的用户的身份")
    private String identity;

    @ApiModelProperty("成为志愿者的用户的部门或院系")
    private String department;

    @ApiModelProperty("成为志愿者的用户的学号或工号")
    private String studentId;

    @ApiModelProperty("接单数量")
    private Integer orderCount;
}
