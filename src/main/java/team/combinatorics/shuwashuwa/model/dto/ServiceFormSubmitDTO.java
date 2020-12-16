package team.combinatorics.shuwashuwa.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("维修事件的id，以及维修单的信息")
public class ServiceFormSubmitDTO {
    @ApiModelProperty("要修改的维修事件序号")
    private Integer serviceEventId;

    @ApiModelProperty("选择的活动序号")
    private Integer activityId;

    @ApiModelProperty("选择的活动时间段")
    private Integer timeSlot;

    @ApiModelProperty("电脑品牌")
    private String brand;

    @ApiModelProperty("电脑型号")
    private String computerModel;

    @ApiModelProperty("cpu型号")
    private String cpuModel;

    @ApiModelProperty("是否拥有独立显卡")
    private Boolean hasDiscreteGraphics;

    @ApiModelProperty("显卡型号")
    private String graphicsModel;

    @ApiModelProperty("笔记本类型")
    private String laptopType;

    @ApiModelProperty(value = "购买日期，用yyyy-mm表示",example = "1919-08")
    private String boughtTime;

    @ApiModelProperty("是否在保修期内")
    private Boolean underWarranty;

    @ApiModelProperty("问题描述")
    private String problemDescription;

    @ApiModelProperty("问题种类--硬件or软件")
    private String problemType;

    @ApiModelProperty("图片文件名列表（图片应已调用相关接口上传）")
    private List<String> imageList;
}
