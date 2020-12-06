package team.combinatorics.shuwashuwa.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActivityReturnDTO {
    private Integer id;
    private String createTime;
    private String updatedTime;
    private String startTime;
    private String endTime;
    private String activityName;
    private String location;
    private Integer status;
}
