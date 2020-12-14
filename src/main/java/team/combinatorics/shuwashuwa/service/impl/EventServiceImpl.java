package team.combinatorics.shuwashuwa.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import team.combinatorics.shuwashuwa.dao.ServiceEventDao;
import team.combinatorics.shuwashuwa.dao.ServiceFormDao;
import team.combinatorics.shuwashuwa.dao.co.SelectServiceEventCO;
import team.combinatorics.shuwashuwa.exception.ErrorInfoEnum;
import team.combinatorics.shuwashuwa.exception.KnownException;
import team.combinatorics.shuwashuwa.model.dto.ServiceAbstractDTO;
import team.combinatorics.shuwashuwa.model.dto.ServiceEventDetailDTO;
import team.combinatorics.shuwashuwa.model.dto.ServiceEventUniversalDTO;
import team.combinatorics.shuwashuwa.model.dto.ServiceFormSubmitDTO;
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
        assert serviceEventDao.insert(eventPO) == 1;
        return serviceEventDao.getServiceEventByID(eventPO.getId());
    }

    @Override
    public void submitForm(int userid, ServiceFormSubmitDTO serviceFormSubmitDTO, boolean isDraft) {
        //参数检查
        if(!isDraft && DTOUtil.fieldExistNull(serviceFormSubmitDTO))
            throw new KnownException(ErrorInfoEnum.PARAMETER_LACKING);
        if(serviceFormSubmitDTO.getServiceEventId() == null)
            throw new KnownException(ErrorInfoEnum.PARAMETER_LACKING);

        //获取维修事件的创建者和状态
        //todo @kinami 我只想要userid,volunteerId,status
        ServiceEventDetailDTO eventDetail =
                serviceEventDao.getServiceEventByID(serviceFormSubmitDTO.getServiceEventId());

        //检查权限
        if(eventDetail.getUserId() != userid)
            throw new KnownException(ErrorInfoEnum.DATA_NOT_YOURS);

        //签到后不允许修改
        if(eventDetail.getStatus() >=3)
            throw new KnownException(ErrorInfoEnum.STATUS_UNMATCHED);

        //提取维修单信息
        ServiceFormPO newFormPO = (ServiceFormPO) DTOUtil.convert(serviceFormSubmitDTO, ServiceFormPO.class);
        assert newFormPO != null;
        int eventId = newFormPO.getServiceEventId();

        //保存维修单。存在草稿，则覆盖草稿
        if(eventDetail.getDraft()) {
            int draftId = serviceFormDao.getLastFormIDByEventID(eventId);
            newFormPO.setId(draftId);
            serviceFormDao.update(newFormPO);
        }
        else {
            serviceFormDao.insert(newFormPO);
        }

        //若是草稿保存，设置草稿标记
        if(isDraft)
            serviceEventDao.updateDraft(eventId,true);
        //若正式提交，设置图片关联，更新维修单状态
        else {
            for (String imagePath : serviceFormSubmitDTO.getImageList()) {
                imageStorageService.bindWithService(imagePath, newFormPO.getId());
            }
            serviceEventDao.updateDraft(eventId, false);
            serviceEventDao.updateStatus(eventId, 1);
        }
    }

    @Override
    public void rejectForm(int userid, ServiceEventUniversalDTO stringUpdateDTO) {
//todo 草稿状态的实现，待讨论
    }

    @Override
    public void acceptForm(int userid, ServiceEventUniversalDTO stringUpdateDTO) {

    }

    @Override
    public void setActive(int userid, Integer activityId) {
        for (ServiceAbstractDTO service : serviceEventDao.listAbstractServiceEventsByCondition(
                SelectServiceEventCO.builder().userId(userid).activityId(activityId).closed(false).build()
        ) ) {
            if(service.getStatus()==2)
                serviceEventDao.updateStatus(service.getServiceEventId(),3);
        }
    }

    @Override
    public void takeOrder(int userid, Integer serviceEventId) {
        ServiceEventDetailDTO detailDTO = getServiceDetail(serviceEventId);
        if(detailDTO.getStatus()!=3)
            throw new KnownException(ErrorInfoEnum.STATUS_UNMATCHED);
        serviceEventDao.updateVolunteerInfo(serviceEventId,userid);
        serviceEventDao.updateStatus(serviceEventId,4);
    }

    @Override
    public void giveUpOrder(int userid, Integer serviceEventId) {
        ServiceEventDetailDTO detailDTO = getServiceDetail(serviceEventId);
        if(detailDTO.getVolunteerId()!=userid)
            throw new KnownException(ErrorInfoEnum.DATA_NOT_YOURS);
        if(detailDTO.getStatus()!=4)
            throw new KnownException(ErrorInfoEnum.STATUS_UNMATCHED);
        serviceEventDao.updateStatus(serviceEventId,3);
    }

    @Override
    public void completeOrder(int userid, ServiceEventUniversalDTO stringUpdateDTO) {
        ServiceEventDetailDTO detailDTO = getServiceDetail(stringUpdateDTO.getServiceEventId());
        if(detailDTO.getVolunteerId()!=userid)
            throw new KnownException(ErrorInfoEnum.DATA_NOT_YOURS);
        if(detailDTO.getStatus()!=4)
            throw new KnownException(ErrorInfoEnum.STATUS_UNMATCHED);
        serviceEventDao.updateByVolunteer(stringUpdateDTO.getServiceEventId(),stringUpdateDTO.getMessage());
        serviceEventDao.updateStatus(stringUpdateDTO.getServiceEventId(),5);
    }

    @Override
    public void updateFeedback(int userid, ServiceEventUniversalDTO stringUpdateDTO) {
        ServiceEventDetailDTO detailDTO = getServiceDetail(stringUpdateDTO.getServiceEventId());
        if(detailDTO.getUserId()!=userid)
            throw new KnownException(ErrorInfoEnum.DATA_NOT_YOURS);
        serviceEventDao.updateFeedback(stringUpdateDTO.getServiceEventId(),stringUpdateDTO.getMessage());
    }

    @Override
    public void shutdownService(int userid, Integer serviceEventId) {
        ServiceEventDetailDTO detailDTO = getServiceDetail(serviceEventId);
        if(detailDTO.getUserId()!=userid)
            throw new KnownException(ErrorInfoEnum.DATA_NOT_YOURS);
        serviceEventDao.updateClosed(serviceEventId,true);
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
