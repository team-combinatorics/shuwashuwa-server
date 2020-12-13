package team.combinatorics.shuwashuwa.dao.co;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SelectActivityCO {
    /**
     * 开始时间下界
     */
    private Timestamp startTimeLowerBound;
    /**
     * 开始时间上界
     */
    private Timestamp startTimeUpperBound;
    /**
     * 结束时间下界
     */
    private Timestamp endTimeLowerBound;
    /**
     * 结束时间上界
     */
    private Timestamp endTimeUpperBound;
    /**
     * 状态
     */
    private Integer status;
}
