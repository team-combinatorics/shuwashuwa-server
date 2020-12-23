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
@ApiModel(description = "管理员审核志愿者申请时传输的数据，审核通过时应该一并传输申请成为志愿者的用户的用户信息")
public class VolunteerApplicationAuditDTO {
    @ApiModelProperty(value = "志愿者申请表id",required = true)
    private Integer formId;
    @ApiModelProperty(value = "管理员的回复",required = true)
    private String replyByAdmin;
    @ApiModelProperty(value = "管理员给出的状态,1表示通过,2表示拒绝", allowableValues = "1,2",required = true)
    private Integer status;
    /*
     * 以下为审核成功时需要填写的用户信息
     */
    @ApiModelProperty("成为志愿者的用户的真实姓名（审核通过时该信息才有效）")
    private String userName;
    @ApiModelProperty("成为志愿者的用户的手机号（审核通过时该信息才有效）")
    private String phoneNumber;
    @ApiModelProperty("成为志愿者的用户的邮箱（审核通过时该信息才有效）")
    private String email;
    @ApiModelProperty("成为志愿者的用户的身份（审核通过时该信息才有效）")
    private String identity;
    @ApiModelProperty("成为志愿者的用户的部门或院系（审核通过时该信息才有效）")
    private String department;
    @ApiModelProperty("成为志愿者的用户的学号或工号（审核通过时该信息才有效）")
    private String studentId;
}
