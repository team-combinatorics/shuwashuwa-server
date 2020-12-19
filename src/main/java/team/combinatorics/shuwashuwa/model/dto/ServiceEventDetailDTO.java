package team.combinatorics.shuwashuwa.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 这是一个完整的一个维修请求的结构，其中维修单应该是一个list
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("返回给前端的维修事件信息结构")
public class ServiceEventDetailDTO {
    @ApiModelProperty("维修事件id")
    private Integer id;
    @ApiModelProperty(value = "维修事件创建时间，以yyyy-MM-dd HH:mm:ss表示", example = "1926-08-17 11:45:14")
    private String createTime;
    @ApiModelProperty("发起维修申请的用户id")
    private Integer userId;
    @ApiModelProperty("发起维修申请的用户姓名")
    private String userName;
    @ApiModelProperty("接单的志愿者的志愿者id")
    private Integer volunteerId;
    @ApiModelProperty("接单的志愿者的姓名")
    private String volunteerName;
    @ApiModelProperty("用户提交的历史维修单")
    private List<ServiceFormDTO> serviceForms;
    @ApiModelProperty("由志愿者填写的维修结果")
    private String repairingResult;
    @ApiModelProperty("用户反馈")
    private String feedback;
    @ApiModelProperty("参加的活动的id")
    private Integer activityId;
    @ApiModelProperty("参加的活动的名称")
    private String activityName;
    @ApiModelProperty("预约的时间段")
    private Integer timeSlot;
    @ApiModelProperty(value = "预约时间段的开始时间，以yyyy-MM-dd HH:mm:ss表示", example = "1926-08-17 11:45:14")
    private String startTime;
    @ApiModelProperty(value = "预约时间段的结束时间，以yyyy-MM-dd HH:mm:ss表示", example = "1926-08-17 11:45:14")
    private String endTime;
    @ApiModelProperty("问题摘要")
    private String problemSummary;
    @ApiModelProperty(value = "该次维修处于的状态,可能状态如下:\n" +
            "0:等待用户编辑\n" +
            "1:等待管理员审核\n" +
            "2:审核通过（待签到）\n" +
            "3:等待志愿者接单\n" +
            "4:维修中\n" +
            "5:维修完成\n", allowableValues = "0,1,2,3,4,5")
    private Integer status;
    @ApiModelProperty("是否存在维修单草稿，若为真，则serviceForms中的最后一项是用户保存的草稿")
    private Boolean draft;
    @ApiModelProperty("该维修单是否已关闭")
    private Boolean closed;
}
