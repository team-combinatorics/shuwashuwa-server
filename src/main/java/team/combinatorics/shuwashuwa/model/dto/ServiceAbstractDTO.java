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
    @ApiModelProperty("维修事件序号")
    private Integer serviceEventId;
    @ApiModelProperty("维修事件状态")
    private Integer status;
    @ApiModelProperty("发起维修申请的用户id")
    private Integer userId;
    @ApiModelProperty("电脑型号")
    private String computerModel;
    @ApiModelProperty("问题描述")
    private String problemDescription;
    //todo 数据库里还没有这个列吧
    @ApiModelProperty("问题概括")
    private String problemSummary;
}
