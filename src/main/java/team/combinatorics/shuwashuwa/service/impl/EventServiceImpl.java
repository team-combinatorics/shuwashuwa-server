package team.combinatorics.shuwashuwa.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.combinatorics.shuwashuwa.dao.*;
import team.combinatorics.shuwashuwa.dao.co.SelectServiceEventCO;
import team.combinatorics.shuwashuwa.exception.ErrorInfoEnum;
import team.combinatorics.shuwashuwa.exception.KnownException;
import team.combinatorics.shuwashuwa.model.bo.ServiceAbstractBO;
import team.combinatorics.shuwashuwa.model.bo.ServiceEventDetailBO;
import team.combinatorics.shuwashuwa.model.dto.*;
import team.combinatorics.shuwashuwa.model.po.ServiceEventPO;
import team.combinatorics.shuwashuwa.model.po.ServiceFormPO;
import team.combinatorics.shuwashuwa.model.po.VolunteerPO;
import team.combinatorics.shuwashuwa.service.EventService;
import team.combinatorics.shuwashuwa.service.ImageStorageService;
import team.combinatorics.shuwashuwa.utils.DTOUtil;
import team.combinatorics.shuwashuwa.utils.WechatUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class EventServiceImpl implements EventService {
    private final ServiceEventDao serviceEventDao;
    private final ServiceFormDao serviceFormDao;
    private final AdminDao adminDao;
    private final VolunteerDao volunteerDao;
    private final UserDao userDao;

    private final ImageStorageService imageStorageService;

    /*维修事件Status属性说明
     * 0:等待用户编辑
     * 1:等待管理员审核
     * 2:审核通过（待签到）
     * 3:等待志愿者接单
     * 4:维修中
     * 5:维修完成
     * */

    @Override
    public ServiceEventDetailBO createNewEvent(int userid) {
        ServiceEventPO eventPO = ServiceEventPO.builder().userId(userid).build();
        serviceEventDao.insert(eventPO);
        int eventId = eventPO.getId();

        //插入草稿
        serviceFormDao.insert(ServiceFormPO.builder().serviceEventId(eventId).build());
        serviceEventDao.updateDraft(eventId, true);

        return serviceEventDao.getServiceEventByID(eventId);
    }

    /**
     * 提交维修单
     *
     * @param userid               上传维修单的用户id，用于验证
     * @param serviceFormSubmitDTO 提交的维修单信息
     * @param isDraft              要提交的维修单是否是草稿
     */
    @Transactional // 标记为一个事务，该方法不能被该service中的其他方法调用！
    @Override
    public void submitForm(int userid, ServiceFormSubmitDTO serviceFormSubmitDTO, boolean isDraft) {
        // 如果不是草稿，检查是否所有内容都填写了
        if (!isDraft && DTOUtil.fieldExistNull(serviceFormSubmitDTO))
            throw new KnownException(ErrorInfoEnum.PARAMETER_LACKING);
        // 如果是草稿，活动id必填
        if (serviceFormSubmitDTO.getServiceEventId() == null)
            throw new KnownException(ErrorInfoEnum.PARAMETER_LACKING);
        //提取维修单信息
        ServiceFormPO newFormPO = DTOUtil.convert(serviceFormSubmitDTO, ServiceFormPO.class);
        int eventId = newFormPO.getServiceEventId();
        //获取维修事件，这里开始了一个事务，会加悲观锁，其他人无法再进行任何操作，包括读
        ServiceEventPO eventPO = serviceEventDao.getServiceEventForUpdate(serviceFormSubmitDTO.getServiceEventId());
        //检查权限
        if (eventPO.getUserId() != userid)
            throw new KnownException(ErrorInfoEnum.DATA_NOT_YOURS);
        //签到后不允许修改
        if (eventPO.getStatus() >= 3)
            throw new KnownException(ErrorInfoEnum.STATUS_UNMATCHED);
        //保存维修单。存在草稿，则覆盖草稿
        if (eventPO.getDraft()) {
            int draftId = serviceFormDao.getLastFormIDByEventID(eventId);
            newFormPO.setId(draftId);
            serviceFormDao.update(newFormPO);
        } else {
            serviceFormDao.insert(newFormPO);
        }
        //若是草稿保存，设置草稿标记
        if (isDraft)
            serviceEventDao.updateDraft(eventId, true);
        else { //正式提交
            for (String imagePath : serviceFormSubmitDTO.getImageList())
                imageStorageService.bindWithService(imagePath, newFormPO.getId());
            serviceEventDao.update(
                    ServiceEventPO.builder()
                            .id(eventId)
                            .draft(false)
                            .status(1)
                            .validFormId(newFormPO.getId())
                            .activityId(newFormPO.getActivityId())
                            .timeSlot(newFormPO.getTimeSlot())
                            .build()
            );
        }
    }

    @Transactional
    @Override
    public void auditForm(int userid, ServiceEventAuditDTO auditDTO) throws Exception {
        //参数检查和提取
        if (DTOUtil.fieldExistNull(auditDTO))
            throw new KnownException(ErrorInfoEnum.PARAMETER_LACKING);
        int formId = auditDTO.getServiceFormId();
        int eventId = auditDTO.getServiceEventId();
        // 查询数据库，并且加上悲观锁
        ServiceEventPO eventPO = serviceEventDao.getServiceEventForUpdate(eventId);
        //根据查询结果判断能否更新
        if (formId != eventPO.getValidFormId() || eventPO.getStatus() != 1)
            throw new KnownException(ErrorInfoEnum.STATUS_UNMATCHED);
        //更新状态
        serviceEventDao.updateStatus(eventId, auditDTO.getResult() ? 2 : 0);
        //更新其他
        serviceFormDao.updateAdvice(formId, adminDao.getAdminIDByUserID(userid), auditDTO.getMessage());
        serviceEventDao.updateProblemSummary(eventId, auditDTO.getProblemSummary());

        // 向用户发送结果通知
        String result = auditDTO.getResult() ? "审核通过" : "审核不通过";
        Map<String, NoticeMessage> data = new HashMap<>();
        data.put("phrase5", NoticeMessage.builder()
                .value(result)
                .build());
        data.put("thing8", NoticeMessage.builder()
                .value(auditDTO.getMessage())
                .build());
        data.put("thing13", NoticeMessage.builder()
                .value(auditDTO.getProblemSummary())
                .build());
        WechatNoticeDTO wechatNoticeDTO = WechatNoticeDTO.builder()
                .touser(userDao.getUserByUserid(eventPO.getUserId()).getOpenid())
                .data(data)
                .page("pages/service-detail/service-detail?id="+auditDTO.getServiceEventId())
                .build();

        // 捕获异常，防止操作失败
        try {
            WechatUtil.sendNotice(wechatNoticeDTO, WechatUtil.WechatNoticeType.TAKE_NOTICE);
        } catch (Exception e) {
            System.out.println("userId=" + userid + " eventId=" + eventId + " 发送通知失败" + e.getMessage());
        }
    }

    @Transactional
    @Override
    public void takeOrder(int userid, Integer serviceEventId) {
        //参数检查和提取
        if (serviceEventId == null)
            throw new KnownException(ErrorInfoEnum.PARAMETER_LACKING);
        int myVolunteerId = volunteerDao.getVolunteerIDByUserID(userid);
        // 查询数据库，并且加上悲观锁
        ServiceEventPO eventPO = serviceEventDao.getServiceEventForUpdate(serviceEventId);
        //根据查询结果判断能否更新
        if (eventPO.getStatus() != 3)
            throw new KnownException(ErrorInfoEnum.STATUS_UNMATCHED);
        if (eventPO.getUserId() == userid)
            throw new KnownException(ErrorInfoEnum.FARMING);
        //更新状态
        serviceEventDao.updateStatus(serviceEventId, 4);
        //更新其他
        serviceEventDao.updateVolunteerInfo(serviceEventId, myVolunteerId);

        // 向用户发送结果通知
        Map<String, NoticeMessage> data = new HashMap<>();
        data.put("thing2", NoticeMessage.builder()
                .value(userDao.getUserByUserid(userid).getUserName())
                .build());
        data.put("thing3", NoticeMessage.builder()
                .value("若一段时间后仍未叫号，请联系在场志愿者")
                .build());
        WechatNoticeDTO wechatNoticeDTO = WechatNoticeDTO.builder()
                .touser(userDao.getUserByUserid(eventPO.getUserId()).getOpenid())
                .data(data)
                .page("pages/service-detail/service-detail?id="+serviceEventId)
                .build();

        // 捕获异常，防止操作失败
        try {
            WechatUtil.sendNotice(wechatNoticeDTO, WechatUtil.WechatNoticeType.AUDIT_NOTICE);
        } catch (Exception e) {
            System.out.println("userId=" + userid + " eventId=" + serviceEventId + " 发送通知失败" + e.getMessage());
        }
    }

    @Transactional
    @Override
    public void giveUpOrder(int userid, Integer serviceEventId) {
        //参数检查和提取
        if (serviceEventId == null)
            throw new KnownException(ErrorInfoEnum.PARAMETER_LACKING);
        // 查询数据库，并且加上悲观锁
        ServiceEventPO eventPO = serviceEventDao.getServiceEventForUpdate(serviceEventId);
        //根据查询结果判断能否更新
        if (eventPO.getVolunteerId() != volunteerDao.getVolunteerIDByUserID(userid))
            throw new KnownException(ErrorInfoEnum.DATA_NOT_YOURS);
        if (eventPO.getStatus() != 4)
            throw new KnownException(ErrorInfoEnum.STATUS_UNMATCHED);
        //更新状态
        serviceEventDao.updateStatus(serviceEventId, 3);
    }

    @Transactional
    @Override
    public void completeOrder(int userid, ServiceSimpleUpdateDTO stringUpdateDTO) {
        //参数检查和提取
        if (DTOUtil.fieldExistNull(stringUpdateDTO))
            throw new KnownException(ErrorInfoEnum.PARAMETER_LACKING);
        int myVolunteerId = volunteerDao.getVolunteerIDByUserID(userid);
        // 查询数据库，并且加上悲观锁
        ServiceEventPO eventPO = serviceEventDao.getServiceEventForUpdate(stringUpdateDTO.getServiceEventId());
        //根据查询结果判断能否更新
        if (eventPO.getVolunteerId() != myVolunteerId)
            throw new KnownException(ErrorInfoEnum.DATA_NOT_YOURS);
        if (eventPO.getStatus() != 4)
            throw new KnownException(ErrorInfoEnum.STATUS_UNMATCHED);
        //更新状态
        serviceEventDao.updateStatus(stringUpdateDTO.getServiceEventId(), 5);
        //更新其他
        serviceEventDao.updateByVolunteer(stringUpdateDTO.getServiceEventId(), stringUpdateDTO.getMessage());

        //查询数据库
        VolunteerPO volunteerPO = volunteerDao.getByID(myVolunteerId);
        //更新计数器
        volunteerDao.updateOrderCount(myVolunteerId, volunteerPO.getOrderCount() + 1);
    }

    //该方法不需要同步保护
    @Transactional
    @Override
    public void updateFeedback(int userid, ServiceSimpleUpdateDTO stringUpdateDTO) {
        //参数检查和提取
        if (DTOUtil.fieldExistNull(stringUpdateDTO))
            throw new KnownException(ErrorInfoEnum.PARAMETER_LACKING);
        // 查询数据库，并且加上悲观锁
        ServiceEventPO eventPO = serviceEventDao.getServiceEventForUpdate(stringUpdateDTO.getServiceEventId());
        //根据查询结果判断能否更新
        if (eventPO.getUserId() != userid)
            throw new KnownException(ErrorInfoEnum.DATA_NOT_YOURS);
        if (eventPO.getStatus() != 5)
            throw new KnownException(ErrorInfoEnum.STATUS_UNMATCHED);
        //更新
        serviceEventDao.updateFeedback(stringUpdateDTO.getServiceEventId(), stringUpdateDTO.getMessage());
    }

    //该方法不需要同步保护
    @Transactional
    @Override
    public void shutdownService(int userid, Integer serviceEventId) {
        //参数检查和提取
        if (serviceEventId == null)
            throw new KnownException(ErrorInfoEnum.PARAMETER_LACKING);
        //查询数据库
        ServiceEventPO eventPO = serviceEventDao.getServiceEventForUpdate(serviceEventId);
        //根据查询结果判断能否更新
        if (eventPO.getUserId() != userid)
            throw new KnownException(ErrorInfoEnum.DATA_NOT_YOURS);
        //更新
        serviceEventDao.updateClosed(serviceEventId, true);
    }

    @Override
    public List<ServiceAbstractBO> listServiceEvents(SelectServiceEventCO co) {
        return serviceEventDao.listServiceAbstractsByCondition(co);
    }

    @Override
    public Integer countServiceEvents(SelectServiceEventCO co) {
        return serviceEventDao.countServiceEventsByCondition(co);
    }

    @Override
    public ServiceEventDetailBO getServiceDetail(Integer eventId) {
        return serviceEventDao.getServiceEventByID(eventId);
    }
}
