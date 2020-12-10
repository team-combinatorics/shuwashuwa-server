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
@ApiModel("志愿者申请结果")
public class VolunteerApplicationResultDTO {
    @ApiModelProperty("申请序号")
    private Integer id;
    @ApiModelProperty("申请理由")
    private String reasonForApplication;
    @ApiModelProperty(value = "申请时间，以yyyy-MM-dd HH:mm:ss表示", example = "1926-08-17 11:45:14")
    private String createTime;
    @ApiModelProperty(value = "审核时间，以yyyy-MM-dd HH:mm:ss表示", example = "1926-08-17 11:45:14")
    private String updatedTime;
    @ApiModelProperty("管理员回复")
    private String replyByAdmin;
    @ApiModelProperty("回复的管理员")
    private Integer adminId;
    @ApiModelProperty("申请状态")
    private Integer status;
}
