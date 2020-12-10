package team.combinatorics.shuwashuwa.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "通用结构，用于更新维修事件中的单个字符串属性")
public class ServiceEventUniversalDTO {
    @ApiModelProperty(value = "维修事件id")
    @NotNull(message = "必须指定维修事件的ID")
    private Integer serviceEventId;
    @ApiModelProperty("信息")
    private String message;
}