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
@ApiModel("维修事件摘要")
public class ServiceAbstractDTO {
    @ApiModelProperty("维修事件id")
    private Integer serviceEventId;
    @ApiModelProperty("发起维修申请的用户id")
    private Integer userId;
    @ApiModelProperty("发起维修申请的用户姓名")
    private String userName;
    @ApiModelProperty("接单的志愿者id")
    private Integer volunteerId;
    @ApiModelProperty("接单的志愿者姓名")
    private String volunteerName;
    @ApiModelProperty("问题概括")
    private String problemSummary;
    @ApiModelProperty("维修事件状态")
    private Integer status;
    @ApiModelProperty("是否为草稿")
    private Boolean draft;
    @ApiModelProperty("是否关闭")
    private Boolean closed;
    @ApiModelProperty("电脑型号")
    private String computerModel;
}
