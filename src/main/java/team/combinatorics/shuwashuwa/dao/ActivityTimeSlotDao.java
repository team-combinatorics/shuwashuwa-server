package team.combinatorics.shuwashuwa.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import team.combinatorics.shuwashuwa.model.po.ActivityTimeSlotPO;

@Repository
public interface ActivityTimeSlotDao {
    /**
     * 插入一个时间段
     * @param activityTimeSlotPO 要插入的时间段
     * @return 插入成功的数量，如果为0表示不成功
     */
    int insert(@Param("timeSlot") ActivityTimeSlotPO activityTimeSlotPO);
}
