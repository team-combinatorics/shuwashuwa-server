package team.combinatorics.shuwashuwa.model.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ServiceFormPO {
    private Integer id;
    private Timestamp createTime;
    private Timestamp updatedTime;
    private String brand;
    private String computerModel;
    private String cpuModel;
    private Boolean hasDiscreteGraphics;
    private String graphicsModel;
    private String laptopType;
    private String boughtTime;
    private Boolean underWarranty;
    private String problemDescription;
    private String problemType;
    private Integer replyAdminId;
    private String descriptionAdvice;
    private Integer activityId;
    private Integer timeSlot;
    private Integer serviceEventId;
}