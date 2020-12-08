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
@ApiModel("活动的一个可选子时间段")
public class ActivityTimeSlotDTO {
    @ApiModelProperty("时间段序号")
    private Integer timeSlot;
    @ApiModelProperty("开始时间，以yyyy-MM-dd HH:mm:ss表示")
    private String startTime;
    @ApiModelProperty("结束时间，以yyyy-MM-dd HH:mm:ss表示")
    private String endTime;
}