package team.combinatorics.shuwashuwa.model.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import team.combinatorics.shuwashuwa.model.dto.ServiceFormDTO;

import java.sql.Timestamp;
import java.util.List;

/**
 * 这是一个完整的一个维修请求的结构，其中维修单应该是一个list
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ServiceEventDetailBO {
    private Integer id;
    private Integer userId;
    private String userName;
    private Integer volunteerId;
    private String volunteerName;
    private List<ServiceFormDTO> serviceForms;
    private String repairingResult;
    private String feedback;
    private Integer activityId;
    private Integer activityName;
    private Integer timeSlot;
    private Timestamp startTime;
    private Timestamp endTime;
    private String problemSummary;
    private Integer status;
    private Boolean draft;
    private Boolean closed;
}
