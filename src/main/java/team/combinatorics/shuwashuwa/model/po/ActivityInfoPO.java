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
public class ActivityInfoPO {
    private Integer id;
    private Timestamp createTime;
    private Timestamp updatedTime;
    private Timestamp startTime;
    private Timestamp endTime;
    private String activityName;
    private String location;
}