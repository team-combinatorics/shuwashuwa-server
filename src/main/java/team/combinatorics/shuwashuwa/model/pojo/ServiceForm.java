package team.combinatorics.shuwashuwa.model.pojo;

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
public class ServiceForm {
    private Integer formID;
    private String brand;
    private String computerModel;
    private String cpuModel;
    private boolean hasDiscreteGraphics;
    private String graphicsModel;
    private String laptopType;
    private Date boughtTime;
    private boolean underWarranty;
    private String problemDescription;
    private String problemType;
    private List<ServicePic> pictures;
    private Integer replyUserId;
    private String descriptionAdvice;
    private Integer status;
}
