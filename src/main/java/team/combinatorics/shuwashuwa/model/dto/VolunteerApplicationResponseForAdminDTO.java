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
@ApiModel("一个待审核志愿者申请")
public class VolunteerApplicationResponseForAdminDTO {
    @ApiModelProperty("申请序号")
    private Integer id;
    @ApiModelProperty("发起申请的用户")
    private Integer userId;
    @ApiModelProperty(value = "申请时间，以yyyy-MM-dd HH:mm:ss表示", example = "1926-08-17 11:45:14")
    private String createTime;
    @ApiModelProperty("申请理由")
    private String reasonForApplication;
    @ApiModelProperty("学生证图片的云端文件名")
    private String cardPicLocation;
}
