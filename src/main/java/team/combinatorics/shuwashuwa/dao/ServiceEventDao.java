package team.combinatorics.shuwashuwa.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import team.combinatorics.shuwashuwa.dao.co.SelectServiceEventCO;
import team.combinatorics.shuwashuwa.model.dto.ServiceAbstractDTO;
import team.combinatorics.shuwashuwa.model.dto.ServiceEventDetailDTO;
import team.combinatorics.shuwashuwa.model.po.ServiceEventPO;

import java.util.List;

@Repository
public interface ServiceEventDao {


    /**
     * 根据用户id建立一个新的维修事件，由于将活动id和时间段在维修单中放了一份，新建service event时已经用不到
     *
     * @param userID 用户id
     * @return 插入成功的数量，如果为0表示不成功
     */
    int insertByUserID(@Param("userID") int userID);

    /**
     * 插入一个新的serviceEvent
     *
     * @param serviceEventPO 一个ServiceEventPO对象
     * @return 插入成功的数量，如果为0表示不成功
     */
    int insert(@Param("serviceEvent") ServiceEventPO serviceEventPO);

    /**
     * 一个通用的update方法
     *
     * @param serviceEventPO 填写需要修改的信息，不能全为0
     * @return 更改成功的数量，如果为0表示不成功
     */
    int update(@Param("serviceEvent") ServiceEventPO serviceEventPO);

    /**
     * 志愿者更新维修结果和状态，志愿者id应当不是必要的，因为志愿者需要首先接单才行
     *
     * @param eventID 维修事件id
     * @param result  志愿者的反馈结果
     * @return 发生变动的数量，应当为1，为0表示操作失败
     */
    int updateByVolunteer(@Param("eventID") int eventID,
                          @Param("result") String result);

    /**
     * 更新志愿者信息，志愿者接单时设置
     *
     * @param eventID     维修事件id
     * @param volunteerID 志愿者id
     * @return 发生变动的数量，应当为1，为0表示操作失败
     */
    int updateVolunteerInfo(@Param("eventID") int eventID,
                            @Param("volunteerID") int volunteerID);

    /**
     * 用户更新反馈信息
     *
     * @param id       维修单id
     * @param feedback 用户反馈
     * @return 发生变动的数量，应当为1，为0表示操作失败
     */
    int updateFeedback(@Param("id") int id, @Param("feedback") String feedback);

    /**
     * 更新事件状态
     *
     * @param status 状态
     * @return 发生变动的数量，应当为1，为0表示操作失败
     */
    int updateStatus(@Param("id") int id, @Param("status") int status);

    /**
     * 更新草稿状态
     *
     * @param draft 状态
     * @return 发生变动的数量，应当为1，为0表示操作失败
     */
    int updateDraft(@Param("id") int id, @Param("draft") boolean draft);

    /**
     * 更新关闭状态
     *
     * @param closed 状态
     * @return 发生变动的数量，应当为1，为0表示操作失败
     */
    int updateClosed(@Param("id") int id, @Param("closed") boolean closed);

    /**
     * 更新可用的维修单id，用于提取摘要信息
     *
     * @param eventID 维修事件id
     * @param formID  维修单id
     * @return 发生变动的数量，应当为1，为0表示操作失败
     */
    int updateValidFormID(@Param("eventID") int eventID, @Param("formID") int formID);

    /**
     * 更新activityID 和 timeSlot
     *
     * @param id         事件id
     * @param activityID 活动id
     * @param timeSlot   时间段序号
     * @return 发生变动的数量，应当为1，为0表示操作失败
     */
    int updateActivityIDAndTimeSlot(@Param("id") int id, @Param("activityID") int activityID, @Param("timeSlot") int timeSlot);

    /**
     * 更新问题概述
     *
     * @param id             事件id
     * @param problemSummary 问题概述
     * @return 发生变动的数量，应当为1，为0表示操作失败
     */
    int updateProblemSummary(@Param("id") int id, @Param("problemSummary") String problemSummary);

    /**
     * @param id 维修请求id
     * @return 一个完整的维修单结构
     */
    ServiceEventDetailDTO getServiceEventByID(@Param("id") int id);

    /**
     * 条件检索
     *
     * @param selectServiceEventCO 根据条件来检索维修单，条件说名见类说明
     * @return 一个维修单列表
     */

    List<ServiceEventDetailDTO> listServiceEventsByCondition(
            @Param("condition") SelectServiceEventCO selectServiceEventCO);

    /**
     * 条件检索，获取摘要列表
     *
     * @param selectServiceEventCO 条件
     * @return 摘要列表
     */
    List<ServiceAbstractDTO> listAbstractServiceEventsByCondition(
            @Param("condition") SelectServiceEventCO selectServiceEventCO);

    /**
     * 根据条件计数，可以用于判断是否有符合条件的元素
     *
     * @param selectServiceEventCO 条件
     * @return 计数值
     */
    int countServiceEventsByCondition(@Param("condition") SelectServiceEventCO selectServiceEventCO);

    /**
     * 测试用，获取一个完整的po
     *
     * @param id id
     * @return 完整的po
     */
    ServiceEventPO getPOByID(@Param("id") int id);

}
