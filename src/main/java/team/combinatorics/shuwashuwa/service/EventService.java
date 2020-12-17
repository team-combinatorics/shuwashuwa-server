package team.combinatorics.shuwashuwa.service;

import team.combinatorics.shuwashuwa.dao.co.SelectServiceEventCO;
import team.combinatorics.shuwashuwa.model.dto.*;

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
     * 审核维修单
     * @param userid 管理员的用户ID
     * @param auditDTO 审核结果结构
     */
    void auditForm(int userid, ServiceEventAuditDTO auditDTO);

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
    void completeOrder(int userid, ServiceSimpleUpdateDTO stringUpdateDTO);

    /**
     * 用户更新反馈
     * @param userid 用户id
     * @param stringUpdateDTO 内含用户的反馈
     */
    void updateFeedback(int userid, ServiceSimpleUpdateDTO stringUpdateDTO);

    void shutdownService(int userid, Integer serviceEventId);

    /**
     * 按条件对象筛选维修事件
     * @param co 用于筛选的条件对象
     */
    List<ServiceAbstractDTO> listServiceEvents(SelectServiceEventCO co);

    ServiceEventDetailDTO getServiceDetail(Integer eventId);
}
