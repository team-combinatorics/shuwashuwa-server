package team.combinatorics.shuwashuwa.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

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
    @ApiModelProperty("申请时间")
    private Timestamp createTime;
    @ApiModelProperty("审核时间")
    private Timestamp updatedTime;
    @ApiModelProperty("管理员回复")
    private String replyByAdmin;
    @ApiModelProperty("回复的管理员")
    private Integer adminId;
    @ApiModelProperty("申请状态")
    private Integer status;
}
