package team.combinatorics.shuwashuwa.model.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * 一个表示时间段的结构，只包含时间段的序号（表示第几个时间段），开始时间，结束时间
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActivityTimeSlotBO {
    private Integer timeSlot;
    private Timestamp startTime;
    private Timestamp endTime;
}
