package team.combinatorics.shuwashuwa.model.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ServiceAbstractBO {
    private Integer serviceEventId;
    private Timestamp createTime;
    private String userName;
    private String volunteerName;
    private String activityName;
    private Timestamp startTime;
    private Timestamp endTime;
    private String problemSummary;
    private String computerModel;
    private Integer status;
    private Boolean draft;
    private Boolean closed;
}
