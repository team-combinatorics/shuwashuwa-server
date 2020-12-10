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
@ApiModel(description = "通用结构，用于更新维修事件中的单个字符串属性")
public class ServiceEventUniversalDTO {
    @ApiModelProperty("维修事件id")
    private Integer formID;
    @ApiModelProperty("信息")
    private String message;
}