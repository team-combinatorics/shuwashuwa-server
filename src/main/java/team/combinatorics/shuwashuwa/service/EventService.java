package team.combinatorics.shuwashuwa.service;

import team.combinatorics.shuwashuwa.model.dto.ServiceAbstractDTO;
import team.combinatorics.shuwashuwa.model.dto.ServiceEventDetailDTO;
import team.combinatorics.shuwashuwa.model.dto.ServiceEventUniversalDTO;
import team.combinatorics.shuwashuwa.model.dto.ServiceFormSubmitDTO;

import java.util.List;

public interface EventService {

    /**
     * 创建一个空的维修事件，仅包含一个草稿
     * @param userid 预约维修的用户的ID
     */
    ServiceEventDetailDTO createNewEvent(int userid);

    /**
     * 用户上传维修单
     * @param userid 上传维修单的用户id，用于验证
     * @param serviceFormSubmitDTO 维修单信息
     * @param isDraft 若为false，表示交付审核
     */
    void submitForm(int userid, ServiceFormSubmitDTO serviceFormSubmitDTO, boolean isDraft);

    /**
     * 判定一个状态为审核中的维修单不合格
     * @param userid 管理员的用户ID
     * @param stringUpdateDTO 内含拒绝理由，字符串不允许为空
     */
    void rejectForm(int userid, ServiceEventUniversalDTO stringUpdateDTO);

    /**
     * 判定一个状态为审核中的维修单合格
     * @param userid 管理员的用户ID
     * @param stringUpdateDTO 内含活动开始前想对用户说的话，字符串可以为空
     */
    void acceptForm(int userid, ServiceEventUniversalDTO stringUpdateDTO);

    void setActive(int userid, Integer activityId);

    /**
     * 志愿者接单
     * @param userid 志愿者的用户id
     * @param serviceEventId 维修事件id
     */
    void takeOrder(int userid, Integer serviceEventId);

    /**
     * 志愿者放弃接单
     * @param userid 志愿者的用户id
     * @param serviceEventId 维修事件id
     */
    void giveUpOrder(int userid, Integer serviceEventId);

    /**
     * 志愿者完成维修
     * @param userid 志愿者的id
     * @param stringUpdateDTO 内含志愿者对维修结果的描述
     */
    void completeOrder(int userid, ServiceEventUniversalDTO stringUpdateDTO);

    /**
     * 用户更新反馈
     * @param userid 用户id
     * @param stringUpdateDTO 内含用户的反馈
     */
    void updateFeedback(int userid, ServiceEventUniversalDTO stringUpdateDTO);

    void shutdownService(int userid, Integer serviceEventId);

    /**
     * 返回未审核的维修事件
     */
    List<ServiceAbstractDTO> listUnauditedEvents();

    /**
     * 返回指定用户创建的维修事件
     * @param userid 查询的用户id
     */
    List<ServiceAbstractDTO> listServicesCreatedBy(int userid);

    /**
     * 返回指定用户需要完善维修信息的维修事件
     * @param userid 查询的用户id
     */
    List<ServiceAbstractDTO> listServiceToEditOf(int userid);

    /**
     * 返回未审核的维修事件
     */
    List<ServiceAbstractDTO> listPendingEvents(Integer activityId);

    ServiceEventDetailDTO getServiceDetail(Integer eventId);
}
