package team.combinatorics.shuwashuwa.model.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ServiceEventPO {
    private Integer id;
    private Timestamp createTime;
    private Timestamp updatedTime;
    private Integer userId;
    private Integer volunteerId;
    private String repairingResult;
    private String feedback;
    private Integer activityId;
    private Integer timeSlot;
    private String problemSummary;
    // TODO @leo_h 加了一个属性，该属性是非草稿的最后一个维修单的id，默认为null
    private Integer validFormId;
    private Integer status;
    private Boolean draft;
    private Boolean closed;
}