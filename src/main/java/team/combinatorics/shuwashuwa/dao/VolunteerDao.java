package team.combinatorics.shuwashuwa.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import team.combinatorics.shuwashuwa.model.po.AdminPO;
import team.combinatorics.shuwashuwa.model.po.VolunteerPO;

import java.util.List;

//TODO 未测试
@Repository
public interface VolunteerDao {

    /**
     * 插入一个志愿者
     *
     * @param volunteerPO 志愿者信息
     * @return 成功的数量，应当为1
     */
    int insert(@Param("volunteer") VolunteerPO volunteerPO);

    /**
     * 修改志愿者信息，不允许修改userid，不能全为空
     * order count也不在这里修改
     *
     * @param volunteerPO 志愿者信息
     * @return 修改成功的记录数量
     */
    int update(@Param("volunteer") VolunteerPO volunteerPO);

    /**
     * 更新order count
     *
     * @param id    志愿者id
     * @param count 要更新的count
     * @return 默认
     */
    int updateOrderCount(@Param("id") int id, @Param("count") int count);

    /**
     * 获取志愿者列表，返回所有
     * TODO：这里应该定义一个志愿者的摘要信息，在点进详情之前只显示摘要？
     *
     * @return 所有志愿者的列表
     */
    List<AdminPO> listVolunteers();

    /**
     * 根据id获取志愿者信息
     *
     * @return 志愿者信息
     */
    VolunteerPO getByID(@Param("id") int id);

    /**
     * 根据用户id获取志愿者id
     *
     * @param userID 用户id
     * @return 志愿者id
     */
    int getVolunteerIDByUserID(@Param("userID") int userID);

    /**
     * 根据志愿者id获取该志愿者的order count
     *
     * @param id 志愿者id
     * @return count
     */
    int getOrderCount(@Param("id") int id);

    /**
     * 根据id删除志愿者
     *
     * @param id 志愿者id
     * @return 删除的数量，应该为1
     */
    int deleteByID(@Param("id") int id);
}
