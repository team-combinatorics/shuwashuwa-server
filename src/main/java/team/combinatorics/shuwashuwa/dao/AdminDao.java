package team.combinatorics.shuwashuwa.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import team.combinatorics.shuwashuwa.model.po.AdminPO;

import java.util.List;

@Repository
public interface AdminDao {
    /**
     * 插入一个管理员
     *
     * @param adminPO 管理员信息
     * @return 成功的数量，应当为1
     */
    int insert(@Param("admin") AdminPO adminPO);

    /**
     * 修改管理员信息，不允许修改userid，不能全为空
     *
     * @param adminPO 管理员信息
     * @return 修改成功的记录数量
     */
    int update(@Param("admin") AdminPO adminPO);

    /**
     * 获取管理员列表，返回所有
     * TODO：这里应该定义一个管理员的摘要信息，在点进详情之前只显示摘要？
     *
     * @return 所有管理员的列表
     */
    List<AdminPO> listAdmins();

    /**
     * 根据id获取管理员信息
     *
     * @return 管理员信息
     */
    AdminPO getByID(@Param("id") int id);

    /**
     * 根据用户id获取管理员id
     * @param userID 用户id
     * @return 管理员id
     */
    int getAdminIDByUserID(@Param("userID") int userID);

    /**
     * 根据id删除管理员
     *
     * @param id 管理员id
     * @return 删除的数量，应该为1
     */
    int deleteByID(@Param("id") int id);


}
