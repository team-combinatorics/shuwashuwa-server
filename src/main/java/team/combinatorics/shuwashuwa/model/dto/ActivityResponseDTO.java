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
    @ApiModelProperty(value = "活动创建时间，以yyyy-MM-dd HH:mm:ss表示", example = "1926-08-17 11:45:14")
    private String createTime;
    @ApiModelProperty(value = "活动信息更新时间，以yyyy-MM-dd HH:mm:ss表示", example = "1926-08-17 11:45:14")
    private String updatedTime;
    @ApiModelProperty(value = "开始时间，以yyyy-MM-dd HH:mm:ss表示", example = "1926-08-17 11:45:14")
    private String startTime;
    @ApiModelProperty(value = "结束时间，以yyyy-MM-dd HH:mm:ss表示", example = "1926-08-17 11:45:14")
    private String endTime;
    @ApiModelProperty("活动名称")
    private String activityName;
    @ApiModelProperty("活动地点")
    private String location;
    @ApiModelProperty("活动状态（暂时没用）")
    private Integer status;
}
