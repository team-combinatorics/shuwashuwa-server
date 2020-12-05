package team.combinatorics.shuwashuwa.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import team.combinatorics.shuwashuwa.dao.co.SelectServiceEventCO;
import team.combinatorics.shuwashuwa.model.dto.ServiceEventResponseDTO;
import team.combinatorics.shuwashuwa.model.dto.ServiceEventUpdateByVolunteerDTO;
import team.combinatorics.shuwashuwa.model.po.ServiceEventPO;

import java.util.List;

@Repository
public interface ServiceEventDao {
    /**
     * 插入一个新的serviceEvent
     *
     * @param serviceEventPO 一个ServiceEventPO对象
     */
    void insertServiceEvent(@Param("userID") int userid, @Param("serviceEvent") ServiceEventPO serviceEventPO);

    /**
     * 志愿者更新维修结果和状态，这两个必须都不为空
     *
     * @param volunteerID                      志愿者id
     * @param serviceEventUpdateByVolunteerDTO 更新的内容
     */
    void updateByVolunteer(@Param("volunteerID") int volunteerID,
                           @Param("updateInfo") ServiceEventUpdateByVolunteerDTO serviceEventUpdateByVolunteerDTO);

    /**
     * 用户更新预约时间，需要验证该维修单是当前用户的
     *
     * @param id         维修单id
     * @param activityID 活动id
     * @param timeSlot   预约时间段
     */
    void updateAppointment(@Param("id") int id, @Param("activityID") int activityID, @Param("timeSlot") int timeSlot);

    /**
     * 用户更新
     *
     * @param id       维修单id
     * @param feedback 用户反馈
     */
    void updateFeedback(@Param("id") int id, @Param("feedback") String feedback);

    /**
     * @param id 维修请求id
     * @return 一个完整的维修单结构
     */
    ServiceEventResponseDTO selectByServiceEventID(@Param("id") int id);

    /**
     * 条件检索
     * @param selectServiceEventCO 根据条件来检索维修单，条件说名见类说明
     * @return 一个维修单列表
     */
    List<ServiceEventResponseDTO> selectByCondition(@Param("condition") SelectServiceEventCO selectServiceEventCO);
    // TODO 这里应该写几个简单的查询语句，例如通过维修单id查找对应的用户，通过维修单id查找当前状态等

}
