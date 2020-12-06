package team.combinatorics.shuwashuwa.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import team.combinatorics.shuwashuwa.dao.co.SelectActivityCO;
import team.combinatorics.shuwashuwa.model.po.ActivityInfoPO;

import java.util.List;

/**
 * 活动信息的类
 */
//TODO 未测试
@Repository
public interface ActivityInfoDao {
    /**
     * 插入一个活动，开始时间、结束时间、地点都不为空，活动状态初始为0，不用插入
     *
     * @param activityTimeSlotPO 要插入的活动信息
     * @return 插入成功的数量，如果为0表示不成功
     */
    int insert(@Param("activity") ActivityInfoPO activityTimeSlotPO);

    /**
     * 根据不为null的项更新活动信息，只能更新活动开始时间、结束时间、地点、名称，不能更新状态
     *
     * @param activityInfoPO 构造的要更新的信息
     * @return 更新影响的记录数量，应当为1
     */
    int update(@Param("activity") ActivityInfoPO activityInfoPO);

    /**
     * 根据活动id更新状态
     *
     * @param id     活动id
     * @param status 状态
     * @return 更新影响的记录数量，应当为1
     */
    int updateStatus(@Param("id") int id, @Param("status") int status);

    /**
     * @param id 根据id查找一个活动
     * @return 一个活动PO
     */
    ActivityInfoPO getByID(@Param("id") int id);

    /**
     * 根据条件查找活动，条件包括时间范围（这里指活动开始时间的范围，而不是活动创建时间的范围）以及活动状态
     * 条件可以全null，此时返回所有活动
     * @param selectActivityCO 条件
     * @return 符合条件的活动列表
     */
    List<ActivityInfoPO> listByCondition(@Param("condition") SelectActivityCO selectActivityCO);

    /**
     * 根据活动id删除活动信息，注意删除和该活动信息关联的时间段信息
     * 应当不允许删除已经结束的活动或者正在进行的活动，只能删除还未开始的活动
     *
     * @param id 活动id
     * @return 删除的数量
     */
    int deleteByID(@Param("id") int id);

}
