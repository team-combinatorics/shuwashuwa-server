package team.combinatorics.shuwashuwa.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import team.combinatorics.shuwashuwa.dao.ServiceEventDao;
import team.combinatorics.shuwashuwa.dao.ServiceFormDao;
import team.combinatorics.shuwashuwa.exception.ErrorInfoEnum;
import team.combinatorics.shuwashuwa.exception.KnownException;
import team.combinatorics.shuwashuwa.model.dto.ServiceEventDetailDTO;
import team.combinatorics.shuwashuwa.model.dto.ServiceFormSubmitDTO;
import team.combinatorics.shuwashuwa.model.po.ServiceEventPO;
import team.combinatorics.shuwashuwa.model.po.ServiceFormPO;
import team.combinatorics.shuwashuwa.service.EventService;
import team.combinatorics.shuwashuwa.service.ImageStorageService;
import team.combinatorics.shuwashuwa.utils.DTOUtil;

@Service
@AllArgsConstructor
public class EventServiceImpl implements EventService {
    private final ServiceEventDao serviceEventDao;
    private final ServiceFormDao serviceFormDao;
    private final ImageStorageService imageStorageService;

    @Override
    public ServiceEventDetailDTO createNewEvent(int userid) {
        ServiceEventPO eventPO = ServiceEventPO.builder().userId(userid).build();
        //todo @kinami 创建维修事件只需要userid，传PO怕是太慢了
        serviceEventDao.insert(eventPO);
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
        //todo @kinami 我只想要PO
        ServiceEventDetailDTO eventDetail = serviceEventDao.getServiceEventByID(serviceFormSubmitDTO.getServiceEventId());

        //检查权限
        if(eventDetail.getUserId() != userid)
            throw new KnownException(ErrorInfoEnum.DATA_NOT_YOURS);

        //提取维修单信息
        ServiceFormPO newFormPO = (ServiceFormPO) DTOUtil.convert(serviceFormSubmitDTO, ServiceFormPO.class);
        assert newFormPO != null;
        int eventId = newFormPO.getServiceEventId();

        //保存维修单。存在草稿，则覆盖草稿
        if(eventDetail.getDraft()) {
            int draftId = serviceFormDao.getLastFormIDByEventID(eventId);
            newFormPO.setId(draftId);
            //todo @kinami 维修单update
        }
        else {
            serviceFormDao.insert(newFormPO);
        }

        //设置图片关联
        for(String imagePath: serviceFormSubmitDTO.getImageList()) {
            imageStorageService.bindWithService(imagePath,newFormPO.getId());
        }

        //todo @kinami 更新维修事件状态（三个都要）
    }

}
