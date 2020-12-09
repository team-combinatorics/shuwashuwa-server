package team.combinatorics.shuwashuwa.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import team.combinatorics.shuwashuwa.model.pojo.ServiceForm;

import java.sql.Timestamp;
import java.util.List;

/**
 * 这是一个完整的一个维修请求的结构，其中维修单应该是一个list
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("返回给前端的维修单信息结构")
public class ServiceEventResponseDTO {
    @ApiModelProperty("维修请求id")
    private Integer id;
    @ApiModelProperty("发起维修申请的用户id")
    private Integer userId;
    @ApiModelProperty("接单的志愿者的id")
    private Integer volunteerId;
    @ApiModelProperty("用户提交的历史维修单")
    private List<ServiceForm> serviceForms;
    @ApiModelProperty("由志愿者填写的维修结果")
    private String repairingResult;
    //TODO 需要在这里说明每个数字代表什么状态，例如等待审核，等待开始活动，已签到等待维修等
    @ApiModelProperty("用户反馈")
    private String feedback;
    @ApiModelProperty("参加的活动的id")
    private Integer activityId;
    @ApiModelProperty("预约的时间段")
    private Integer timeSlot;
    @ApiModelProperty("该次维修状态状态, 0表示提交以后待审核")
    private Integer status;
    @ApiModelProperty("该维修单是否处于草稿状态")
    private boolean draft;
    @ApiModelProperty("该维修单是否已关闭")
    private boolean closed;
}
