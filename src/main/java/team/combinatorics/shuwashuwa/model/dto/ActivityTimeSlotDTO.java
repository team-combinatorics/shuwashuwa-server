package team.combinatorics.shuwashuwa.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActivityTimeSlotDTO {
    @ApiModelProperty("时间戳序号")
    private Integer timeSlot;
    @ApiModelProperty("开始时间，以yyyy-MM-dd HH:mm:ss[.fffffffff]表示")
    private String startTime;
    @ApiModelProperty("结束时间，以yyyy-MM-dd HH:mm:ss[.fffffffff]表示")
    private String endTime;
}