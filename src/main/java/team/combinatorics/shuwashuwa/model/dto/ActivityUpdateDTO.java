package team.combinatorics.shuwashuwa.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("更新活动时上传的数据")
public class ActivityUpdateDTO {
    @ApiModelProperty("要更新的活动id")
    private Integer activityId;
    @ApiModelProperty(value = "开始时间，以yyyy-MM-dd HH:mm:ss表示", example = "1926-08-17 11:45:14")
    private String startTime;
    @ApiModelProperty(value = "结束时间，以yyyy-MM-dd HH:mm:ss表示", example = "1926-08-17 11:45:14")
    private String endTime;
    @ApiModelProperty("活动地点")
    private String location;
    @ApiModelProperty("活动分段信息")
    private List<ActivityTimeSlotDTO> timeSlots;
    @ApiModelProperty("活动名称")
    private String activityName;
}
