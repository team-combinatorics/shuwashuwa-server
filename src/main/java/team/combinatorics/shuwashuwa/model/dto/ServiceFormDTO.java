package team.combinatorics.shuwashuwa.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("用户填写维修单时需要上传的数据")
public class ServiceFormDTO {
    @ApiModelProperty("电脑品牌")
    private String brand;

    @ApiModelProperty("电脑型号")
    private String computerModel;

    @ApiModelProperty("cpu型号")
    private String cpuModel;

    @ApiModelProperty("是否拥有独立显卡")
    private boolean hasDiscreteGraphics;

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
}
