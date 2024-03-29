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
@ApiModel(description = "用户提交申请单的传输结构")
public class VolunteerApplicationAdditionDTO {
    @ApiModelProperty(value = "申请理由",required = true)
    private String reasonForApplication;
    @ApiModelProperty(value = "学生证照片的位置",required = true)
    private String cardPicLocation;
}
