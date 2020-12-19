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
@ApiModel("创建活动时上传的数据")
public class ActivityLaunchDTO {
    @ApiModelProperty(value = "开始时间，以yyyy-MM-dd HH:mm:ss表示", example = "1926-08-17 11:45:14",required = true)
    private String startTime;
    @ApiModelProperty(value = "结束时间，以yyyy-MM-dd HH:mm:ss表示", example = "1926-08-17 11:45:14",required = true)
    private String endTime;
    @ApiModelProperty(value = "活动地点",required = true)
    private String location;
    @ApiModelProperty(value = "活动分段信息",required = true)
    private List<ActivityTimeSlotDTO> timeSlots;
    @ApiModelProperty("活动名称")
    private String activityName;
}
