package team.combinatorics.shuwashuwa.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import team.combinatorics.shuwashuwa.dao.co.SelectApplicationCO;
import team.combinatorics.shuwashuwa.model.dto.VolunteerApplicationDTO;
import team.combinatorics.shuwashuwa.model.dto.VolunteerApplicationUpdateDTO;
import team.combinatorics.shuwashuwa.model.pojo.VolunteerApplicationDO;

import java.util.List;

@Component
public interface VolunteerApplicationDao {
    /**
     * 用户申请
     *
     * @param id                      发起申请的用户id
     * @param volunteerApplicationDTO 用于申请的数据传输对象，暂时只包括申请理由这一项
     */
    void insert(@Param("id") int id,
                @Param("application") VolunteerApplicationDTO volunteerApplicationDTO);

    /**
     * 管理员更新维修单状态
     *
     * @param formID                        维修单id
     * @param adminID                       管理员id
     * @param volunteerApplicationUpdateDTO 管理员的回复结构，必须都不为空
     */
    void updateApplicationByAdmin(@Param("formID") int formID,
                                  @Param("adminID") int adminID,
                                  @Param("adminReply") VolunteerApplicationUpdateDTO volunteerApplicationUpdateDTO);

    /**
     * 通过用户id寻找申请表，可能有多个结果，因此返回一个列表
     *
     * @param id 用户id
     * @return 申请表列表
     */
    List<VolunteerApplicationDO> selectByUserId(@Param("id") int id);

    /**
     * 通过申请表id寻找申请表，只有一个结果
     *
     * @param id 申请表id
     * @return 申请表对象
     */
    VolunteerApplicationDO selectByFormId(@Param("id") int id);

    /**
     * 条件查询，返回一个或多个结果，条件可以都为null，此时返回所有申请表。条件中不提供formID，因为formID可以唯一确定一个申请表。
     *
     * @param selectApplicationCO 用于选取的条件，各种属性见注释，各个属性可以都为空，此时会返回所有列表
     * @return 申请表列表
     */
    List<VolunteerApplicationDO> selectByCondition(@Param("condition") SelectApplicationCO selectApplicationCO);


}
