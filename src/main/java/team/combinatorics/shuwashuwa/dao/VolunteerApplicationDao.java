package team.combinatorics.shuwashuwa.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import team.combinatorics.shuwashuwa.dao.co.SelectApplicationCO;
import team.combinatorics.shuwashuwa.model.bo.VolunteerApplicationAbstractBO;
import team.combinatorics.shuwashuwa.model.bo.VolunteerApplicationDetailBO;
import team.combinatorics.shuwashuwa.model.dto.VolunteerApplicationAuditDTO;
import team.combinatorics.shuwashuwa.model.po.VolunteerApplicationPO;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface VolunteerApplicationDao {
    /**
     * 用户申请
     *
     * @param volunteerApplicationPO 构造好的要插入的对象
     * @return 插入成功的数量，如果为0表示不成功
     */
    int insert(@Param("application") VolunteerApplicationPO volunteerApplicationPO);

    /**
     * 管理员更新维修单状态
     *
     * @param adminID                       管理员id
     * @param volunteerApplicationAuditDTO 管理员的回复结构，必须都不为空
     */
    int updateApplicationByAdmin(
            @Param("adminID") int adminID,
            @Param("adminReply") VolunteerApplicationAuditDTO volunteerApplicationAuditDTO,
            @Param("preUpdateTime") Timestamp timestamp);

    /**
     * 通过用户id寻找申请表，可能有多个结果，因此返回一个列表
     *
     * @param id 用户id
     * @return 申请表列表
     */
    List<VolunteerApplicationPO> listApplicationsByUserId(@Param("id") int id);

    /**
     * 通过申请表id寻找申请表，只有一个结果
     *
     * @param id 申请表id
     * @return 申请表对象
     */
    VolunteerApplicationPO getApplicationByFormId(@Param("id") int id);

    /**
     * 通过申请表id寻找申请表详细信息
     *
     * @param id 申请表id
     * @return 申请表对象
     */
    VolunteerApplicationDetailBO getApplicationDetailByFormId(@Param("id") int id);

    /**
     * 条件查询，返回一个或多个结果，条件可以都为null，此时返回所有申请表。条件中不提供formID，因为formID可以唯一确定一个申请表。
     *
     * @param selectApplicationCO 用于选取的条件，各种属性见注释，各个属性可以都为空，此时会返回所有列表
     * @return 申请表列表
     */
    List<VolunteerApplicationAbstractBO> listApplicationAbstractByCondition(
            @Param("condition") SelectApplicationCO selectApplicationCO);
    // List<VolunteerApplicationPO> listApplicationsByCondition(@Param("condition") SelectApplicationCO selectApplicationCO);


}
