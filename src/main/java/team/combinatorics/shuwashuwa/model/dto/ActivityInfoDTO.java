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
@ApiModel("创建或更新活动时上传的数据")
public class ActivityInfoDTO {
    @ApiModelProperty("活动Id，创建时不需要填")
    private Integer activityId;
    @ApiModelProperty("开始时间，以yyyy-MM-dd HH:mm:ss表示")
    private String startTime;
    @ApiModelProperty("结束时间，以yyyy-MM-dd HH:mm:ss表示")
    private String endTime;
    @ApiModelProperty("活动地点")
    private String location;
    @ApiModelProperty("活动名称(optional)")
    private String activityName;
}
