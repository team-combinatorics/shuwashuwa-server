package team.combinatorics.shuwashuwa.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "管理员完成对维修单的审核时发送的信息")
public class ServiceEventAuditDTO {
    @ApiModelProperty(value = "审核的维修事件ID")
    private Integer serviceEventId;
    @ApiModelProperty(value = "审核的维修单ID")
    private Integer serviceFormId;
    @ApiModelProperty(value = "是否审核通过")
    private Boolean result;
    @ApiModelProperty(value = "审核不通过的原因，或想对用户说的话",example = "记得把电脑带过来")
    private String message;
    @ApiModelProperty("对电脑问题的简要概括")
    private String problemSummary;
}