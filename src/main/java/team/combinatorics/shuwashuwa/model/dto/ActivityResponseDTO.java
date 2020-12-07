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
@ApiModel("查询活动列表时返回的活动结构")
public class ActivityResponseDTO {
    @ApiModelProperty("活动编号")
    private Integer id;
    @ApiModelProperty("活动创建时间")
    private String createTime;
    @ApiModelProperty("活动信息更新时间")
    private String updatedTime;
    @ApiModelProperty("开始时间，以yyyy-MM-dd HH:mm:ss表示")
    private String startTime;
    @ApiModelProperty("结束时间，以yyyy-MM-dd HH:mm:ss表示")
    private String endTime;
    @ApiModelProperty("活动名称")
    private String activityName;
    @ApiModelProperty("活动地点")
    private String location;
    @ApiModelProperty("活动状态（暂时没用）")
    private Integer status;
}
