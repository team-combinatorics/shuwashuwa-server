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
@ApiModel(description = "管理员审核维修单时传输的数据")
public class ServiceFormRejectionDTO {
    @ApiModelProperty("维修单id")
    private Integer formID;
    @ApiModelProperty("修改建议")
    private String advice;
}
//todo 这东西大概没啥大用