package team.combinatorics.shuwashuwa.service;

import team.combinatorics.shuwashuwa.dao.co.SelectApplicationCO;
import team.combinatorics.shuwashuwa.model.so.VolunteerApplicationAbstract;
import team.combinatorics.shuwashuwa.model.dto.VolunteerApplicationAdditionDTO;
import team.combinatorics.shuwashuwa.model.so.VolunteerApplicationDetail;
import team.combinatorics.shuwashuwa.model.dto.VolunteerApplicationUpdateDTO;

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
     * @param userid              当前操作的用户的id
     * @param selectApplicationCO 由controller构造的条件结构
     * @return 申请表摘要信息列表
     */
    List<VolunteerApplicationAbstract> listVolunteerApplicationByCondition(int userid, SelectApplicationCO selectApplicationCO);

    /**
     * TODO 这里要完善这个update结构
     * 管理员完成维修单的填写
     *
     * @param adminUserId 管理员的用户id
     * @param updateDTO   管理员回复的结构
     * @return 如果审核通过成为志愿者，返回志愿者的id（先写上又没什么问题）
     */
    int completeApplicationByAdmin(int adminUserId, VolunteerApplicationUpdateDTO updateDTO);

    /**
     * 根据申请表id获取申请表的详细信息
     *
     * @param formId 申请表的id
     */
    VolunteerApplicationDetail getApplicationDetailByFormId(int formId);


}
