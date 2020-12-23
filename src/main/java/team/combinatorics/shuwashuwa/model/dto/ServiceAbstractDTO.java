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
    @ApiModelProperty(value = "维修事件创建时间，以yyyy-MM-dd HH:mm:ss表示", example = "1926-08-17 11:45:14")
    private String createTime;
    @ApiModelProperty("发起维修申请的用户姓名")
    private String userName;
    @ApiModelProperty("接单的志愿者姓名")
    private String volunteerName;
    @ApiModelProperty("参加的活动的名称")
    private String activityName;
    @ApiModelProperty(value = "预约时间段的开始时间，以yyyy-MM-dd HH:mm:ss表示", example = "1926-08-17 11:45:14")
    private String startTime;
    @ApiModelProperty(value = "预约时间段的结束时间，以yyyy-MM-dd HH:mm:ss表示", example = "1926-08-17 11:45:14")
    private String endTime;
    @ApiModelProperty("问题概括")
    private String problemSummary;
    @ApiModelProperty("电脑型号")
    private String computerModel;
    @ApiModelProperty("维修事件状态")
    private Integer status;
    @ApiModelProperty("是否有草稿状态的维修单")
    private Boolean draft;
    @ApiModelProperty("是否关闭")
    private Boolean closed;
}
