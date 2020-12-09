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
public class SelectServiceEventCO {
    /**
     * 维修事件创建的开始时间
     */
    Timestamp beginTime;
    /**
     * 维修事件创建的结束时间
     */
    Timestamp endTime;
    /**
     * 用户id
     */
    private Integer userId;
    /**
     * 志愿者id
     */
    private Integer volunteerId;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 活动id
     */
    private Integer activityId;
    /**
     * 预约时间段
     */
    private Integer timeSlot;
    /**
     * 草稿状态
     */
    private Boolean draft;
    /**
     * 是否关闭
     */
    private Boolean closed;
}
