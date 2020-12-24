package team.combinatorics.shuwashuwa.service;

import team.combinatorics.shuwashuwa.dao.co.SelectApplicationCO;
import team.combinatorics.shuwashuwa.model.bo.VolunteerApplicationAbstractBO;
import team.combinatorics.shuwashuwa.model.bo.VolunteerApplicationDetailBO;
import team.combinatorics.shuwashuwa.model.dto.VolunteerApplicationAdditionDTO;
import team.combinatorics.shuwashuwa.model.dto.VolunteerApplicationAuditDTO;

import java.util.List;

/**
 * 跟志愿者身份申请相关的操作
 */
public interface VolunteerService {

    /**
     * 申请成为志愿者
     *
     * @param userid                          要申请成为志愿者的用户id
     * @param volunteerApplicationAdditionDTO 申请时的补充信息，包括申请理由和学生证照片
     */
    void addVolunteerApplication(int userid, VolunteerApplicationAdditionDTO volunteerApplicationAdditionDTO);

    /**
     * 根据条件获取摘要信息列表
     *
     * @param selectApplicationCO 由controller构造的条件结构
     * @return 申请表摘要信息列表
     */
    List<VolunteerApplicationDetailBO> listVolunteerApplicationByCondition(SelectApplicationCO selectApplicationCO);

    /**
     * 管理员完成维修单的填写
     *
     * @param adminUserId 管理员的用户id
     * @param auditDTO    管理员回复的结构
     * @return 如果审核通过成为志愿者，返回志愿者的id（先写上又没什么问题）
     */
    int completeApplicationByAdmin(int adminUserId, VolunteerApplicationAuditDTO auditDTO);

    /**
     * 根据申请表id获取申请表的详细信息
     *
     * @param formId 申请表的id
     */
    VolunteerApplicationDetailBO getApplicationDetailByFormId(int formId);

    /**
     * 根据用户id查询志愿者id
     * @param userid 用户id，请保证这是志愿者
     * @return 志愿者id
     */
    Integer getVolunteerIdByUserid(Integer userid);

}
