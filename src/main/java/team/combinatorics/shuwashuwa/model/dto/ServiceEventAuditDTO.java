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
@ApiModel(description = "管理员完成对维修单的审核时发送的信息")
public class ServiceEventAuditDTO {
    @ApiModelProperty(value = "审核的维修事件ID",required = true)
    private Integer serviceEventId;
    @ApiModelProperty(value = "审核的维修单ID",required = true)
    private Integer serviceFormId;
    @ApiModelProperty(value = "是否审核通过",required = true)
    private Boolean result;
    @ApiModelProperty(value = "审核不通过的原因，或想对用户说的话",example = "记得把电脑带过来",required = true)
    private String message;
    @ApiModelProperty(value = "对电脑问题的简要概括",required = true,example = "重装系统")
    private String problemSummary;
}