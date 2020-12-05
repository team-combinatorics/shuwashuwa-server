package team.combinatorics.shuwashuwa.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import team.combinatorics.shuwashuwa.model.po.ActivityTimeSlotPO;
import team.combinatorics.shuwashuwa.model.pojo.ActivityTimeSlot;

import java.util.List;

/**
 * 操作时间段列表的dao接口
 */
@Repository
public interface ActivityTimeSlotDao {
    /**
     * 插入一个时间段
     *
     * @param activityTimeSlotPO 要插入的时间段
     * @return 插入成功的数量，如果为0表示不成功
     */
    int insert(@Param("timeSlot") ActivityTimeSlotPO activityTimeSlotPO);

    /**
     * 根据活动id获取一个时间段列表
     *
     * @param activityID 活动id
     * @return 时间段列表
     */
    List<ActivityTimeSlot> listTimeSlotsByActivityID(@Param("activityID") int activityID);

    /**
     * 由活动id和时间段id获取一个具体的时间段
     *
     * @param activityID 活动id
     * @param timeSlotID 时间段id
     * @return 时间段
     */
    ActivityTimeSlot getTimeSlot(@Param("activityID") int activityID, @Param("timeSlotID") int timeSlotID);

    /**
     * 更新一个time slot
     * 只允许更新某次活动的第几个时间段，因此输入为活动id和一个ActivityTimeSlot结构
     * 需要先判断这个activity id以及timeslot是否存在
     *
     * @param activityID       活动id
     * @param activityTimeSlot 更新的信息
     * @return 影响的行数：应当为1，为0说明更新失败
     */
    int update(@Param("activityID") int activityID, @Param("timeSlot") ActivityTimeSlot activityTimeSlot);

    /**
     * 删除一个活动的某个时间段
     * @param activityID 活动id
     * @param timeSlot 时间段
     * @return 删除的数量 0或1
     */
    int deleteOne(@Param("activityID") int activityID, @Param("timeSlot") int timeSlot);

    /**
     * 删除一个活动的全部
     * 强烈建议，当时间段信息修改时，直接删除掉所有的时间段信息重新插入，方便省事
     * @param activityID 活动id
     * @return 删除的数量 0或1
     */
    int deleteByActivityID(@Param("activityID") int activityID);

}
