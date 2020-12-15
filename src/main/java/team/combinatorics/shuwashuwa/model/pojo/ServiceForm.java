package team.combinatorics.shuwashuwa.model.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;

/**
 * @author KiNAmi
 * 一个用于表示一个维修单的类，包含了维修单中上传的图片路径
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("返回给前端的维修单信息")
public class ServiceForm {
    @ApiModelProperty(value = "维修单ID")
    private Integer formID;

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

    @ApiModelProperty("购买时间")
    private Date boughtTime;

    @ApiModelProperty("是否在保修期内")
    private boolean underWarranty;

    @ApiModelProperty("问题描述")
    private String problemDescription;

    @ApiModelProperty("问题种类--硬件or软件")
    private String problemType;

    @ApiModelProperty("关联到此维修单的图片列表")
    private List<String> imageList;

    @ApiModelProperty("审核维修单的管理员Id")
    private Integer replyAdminId;

    @ApiModelProperty("审核意见")
    private String descriptionAdvice;

    @ApiModelProperty("预约活动id")
    private Integer activityId;

    @ApiModelProperty("活动时间段")
    private Integer timeSlot;
}
