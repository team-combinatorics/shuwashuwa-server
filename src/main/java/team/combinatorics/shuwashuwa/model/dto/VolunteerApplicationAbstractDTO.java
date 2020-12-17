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
@ApiModel("返回志愿者申请列表时显示的摘要信息")
public class VolunteerApplicationAbstractDTO {
    /**
     * 前端应当能拿到申请表的id
     */
    @ApiModelProperty("申请表id")
    private Integer id;
    @ApiModelProperty("申请表提交时间")
    private Timestamp createTime;
    @ApiModelProperty("发起申请的用户的用户id")
    private Integer userId;
    @ApiModelProperty("发起申请的用户的姓名（用户自己填写的）")
    private String userName;
    @ApiModelProperty("发起申请的用户的院系（用户自己填写的）")
    private String department;
    @ApiModelProperty("发起申请的用户的年级（用户自己填写的）")
    private String grade;
    @ApiModelProperty("申请表状态（查看历史申请时应当能区分状态）")
    private Integer status;
}
