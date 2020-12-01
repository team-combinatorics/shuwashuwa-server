package team.combinatorics.shuwashuwa.model.pojo;

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
public class ServiceFormDO {
    private Integer id;
    private Timestamp createTime;
    private Timestamp updatedTime;
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
    private String decriptionEditingAdvice;
    private String repairingResult;
    private Integer status;
    private String feedback;
    private Integer activityId;
    private Integer timeSlot;
}