package team.combinatorics.shuwashuwa.model.dto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "管理员审核志愿者申请时传输的数据")
public class VolunteerApplicationUpdateDTO {
    @ApiModelProperty("维修表id")
    private Integer formID;
    @ApiModelProperty("管理员的回复")
    private String replyByAdmin;
    @ApiModelProperty(value = "管理员给出的状态,1表示通过,2表示拒绝",allowableValues = "1,2")
    private Integer status;
}
