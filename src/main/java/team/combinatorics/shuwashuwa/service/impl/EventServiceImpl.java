package team.combinatorics.shuwashuwa.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.combinatorics.shuwashuwa.dao.ServiceEventDao;
import team.combinatorics.shuwashuwa.dao.ServiceFormDao;
import team.combinatorics.shuwashuwa.dao.co.SelectServiceEventCO;
import team.combinatorics.shuwashuwa.exception.ErrorInfoEnum;
import team.combinatorics.shuwashuwa.exception.KnownException;
import team.combinatorics.shuwashuwa.model.dto.*;
import team.combinatorics.shuwashuwa.model.po.ServiceEventPO;
import team.combinatorics.shuwashuwa.model.po.ServiceFormPO;
import team.combinatorics.shuwashuwa.service.EventService;
import team.combinatorics.shuwashuwa.service.ImageStorageService;
import team.combinatorics.shuwashuwa.utils.DTOUtil;

import java.util.List;

@Service
@AllArgsConstructor
public class EventServiceImpl implements EventService {
    private final ServiceEventDao serviceEventDao;
    private final ServiceFormDao serviceFormDao;
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
    public ServiceEventDetailDTO createNewEvent(int userid) {
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
        //获取维修事件，这里开始了一个事务，会加悲观锁，其他人无法再进行任何操作，包括读
        ServiceEventPO eventPO = serviceEventDao.getServiceEventForUpdate(serviceFormSubmitDTO.getServiceEventId());
        //检查权限
        if (eventPO.getUserId() != userid)
            throw new KnownException(ErrorInfoEnum.DATA_NOT_YOURS);
        //签到后不允许修改
        if (eventPO.getStatus() >= 3)
            throw new KnownException(ErrorInfoEnum.STATUS_UNMATCHED);
        //提取维修单信息
        ServiceFormPO newFormPO = (ServiceFormPO) DTOUtil.convert(serviceFormSubmitDTO, ServiceFormPO.class);
        int eventId = newFormPO.getServiceEventId();
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

    @Override
    public void auditForm(int userid, ServiceEventAuditDTO auditDTO) {
        if (DTOUtil.fieldExistNull(auditDTO))
            throw new KnownException(ErrorInfoEnum.PARAMETER_LACKING);
        int formId = auditDTO.getServiceFormId();
        int eventId = auditDTO.getServiceEventId();
        if (formId == serviceFormDao.getLastFormIDByEventID(eventId)) {
            if (serviceEventDao.getPOByID(eventId).getStatus() != 1)
                throw new KnownException(ErrorInfoEnum.STATUS_UNMATCHED);
            serviceEventDao.updateStatus(eventId, auditDTO.getResult() ? 2 : 0);
        }
        serviceFormDao.updateAdvice(formId, userid, auditDTO.getMessage());
        serviceEventDao.updateProblemSummary(eventId, auditDTO.getProblemSummary());
    }

    @Override
    public void takeOrder(int userid, Integer serviceEventId) {
        if (serviceEventId == null)
            throw new KnownException(ErrorInfoEnum.PARAMETER_LACKING);
        ServiceEventDetailDTO detailDTO = getServiceDetail(serviceEventId);
        if (detailDTO.getStatus() != 3)
            throw new KnownException(ErrorInfoEnum.STATUS_UNMATCHED);

        serviceEventDao.updateVolunteerInfo(serviceEventId, userid);
        serviceEventDao.updateStatus(serviceEventId, 4);
    }

    @Override
    public void giveUpOrder(int userid, Integer serviceEventId) {
        if (serviceEventId == null)
            throw new KnownException(ErrorInfoEnum.PARAMETER_LACKING);
        final ServiceEventPO eventPO = serviceEventDao.getPOByID(serviceEventId);
        if (eventPO.getVolunteerId() != userid)
            throw new KnownException(ErrorInfoEnum.DATA_NOT_YOURS);
        if (eventPO.getStatus() != 4)
            throw new KnownException(ErrorInfoEnum.STATUS_UNMATCHED);

        serviceEventDao.updateStatus(serviceEventId, 3);
    }

    @Override
    public void completeOrder(int userid, ServiceEventUniversalDTO stringUpdateDTO) {
        if (DTOUtil.fieldExistNull(stringUpdateDTO))
            throw new KnownException(ErrorInfoEnum.PARAMETER_LACKING);
        final ServiceEventPO eventPO = serviceEventDao.getPOByID(stringUpdateDTO.getServiceEventId());
        if (eventPO.getVolunteerId() != userid)
            throw new KnownException(ErrorInfoEnum.DATA_NOT_YOURS);
        if (eventPO.getStatus() != 4)
            throw new KnownException(ErrorInfoEnum.STATUS_UNMATCHED);

        serviceEventDao.updateByVolunteer(stringUpdateDTO.getServiceEventId(), stringUpdateDTO.getMessage());
        serviceEventDao.updateStatus(stringUpdateDTO.getServiceEventId(), 5);
    }

    @Override
    public void updateFeedback(int userid, ServiceEventUniversalDTO stringUpdateDTO) {
        if (DTOUtil.fieldExistNull(stringUpdateDTO))
            throw new KnownException(ErrorInfoEnum.PARAMETER_LACKING);
        final ServiceEventPO eventPO = serviceEventDao.getPOByID(stringUpdateDTO.getServiceEventId());
        if (eventPO.getUserId() != userid)
            throw new KnownException(ErrorInfoEnum.DATA_NOT_YOURS);
        if (eventPO.getStatus() != 5)
            throw new KnownException(ErrorInfoEnum.STATUS_UNMATCHED);

        serviceEventDao.updateFeedback(stringUpdateDTO.getServiceEventId(), stringUpdateDTO.getMessage());
    }

    @Override
    public void shutdownService(int userid, Integer serviceEventId) {
        if (serviceEventId == null)
            throw new KnownException(ErrorInfoEnum.PARAMETER_LACKING);
        ServiceEventDetailDTO detailDTO = getServiceDetail(serviceEventId);
        if (detailDTO.getUserId() != userid)
            throw new KnownException(ErrorInfoEnum.DATA_NOT_YOURS);

        serviceEventDao.updateClosed(serviceEventId, true);
    }

    @Override
    public List<ServiceAbstractDTO> listServiceEvents(SelectServiceEventCO co) {
        return serviceEventDao.listAbstractServiceEventsByCondition(co);
    }

    @Override
    public ServiceEventDetailDTO getServiceDetail(Integer eventId) {
        return serviceEventDao.getServiceEventByID(eventId);
    }
}
