package team.combinatorics.shuwashuwa.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import team.combinatorics.shuwashuwa.dao.ServiceEventDao;
import team.combinatorics.shuwashuwa.dao.ServiceFormDao;
import team.combinatorics.shuwashuwa.dao.co.SelectServiceEventCO;
import team.combinatorics.shuwashuwa.exception.ErrorInfoEnum;
import team.combinatorics.shuwashuwa.exception.KnownException;
import team.combinatorics.shuwashuwa.model.dto.ServiceEventResponseDTO;
import team.combinatorics.shuwashuwa.model.dto.ServiceFormDTO;
import team.combinatorics.shuwashuwa.model.po.ServiceEventPO;
import team.combinatorics.shuwashuwa.model.po.ServiceFormPO;
import team.combinatorics.shuwashuwa.service.EventService;
import team.combinatorics.shuwashuwa.utils.DTOUtil;

import java.util.List;

@Service
@AllArgsConstructor
public class EventServiceImpl implements EventService {
    private final ServiceEventDao serviceEventDao;
    private final ServiceFormDao serviceFormDao;

    @Override
    public void commitForm(int userid, ServiceFormDTO serviceFormDTO) {
        //参数检查
        if(DTOUtil.fieldExistNull(serviceFormDTO))
            throw new KnownException(ErrorInfoEnum.PARAMETER_LACKING);
        //分别提取维修单和维修事件信息
        ServiceFormPO newFormPO = (ServiceFormPO) DTOUtil.convert(serviceFormDTO, ServiceFormPO.class);
        ServiceEventPO newEventPO = (ServiceEventPO) DTOUtil.convert(serviceFormDTO, ServiceEventPO.class);
        assert newEventPO!=null;
        assert newFormPO!=null;
        newEventPO.setUserId(userid);
        Integer eventId,formId;

        // 获取维修事件序号，若不存在则插入一个
        // TODO @kinami0331:来个根据userId和activityId直接查
        List<ServiceEventResponseDTO> list = serviceEventDao.listServiceEventsByCondition(
                SelectServiceEventCO.builder()
                .userId(userid)
                .activityId(serviceFormDTO.getActivityId())
                .build()
        );
        if(list.size()==0)
        {
            serviceEventDao.insert(newEventPO);
            eventId = newEventPO.getId();
        }
        else {
            eventId = list.get(0).getId();
        }

        //更新维修单
        //todo 判断草稿标记
        newFormPO.setServiceEventId(eventId);
        serviceFormDao.insert(newFormPO);
        //todo 若有草稿标记，更新而不是插入
        //todo 给formId赋值

        //关联图片
        //todo 根据formId调用图片存储服务

        //更新维修事件
        serviceEventDao.updateAppointment(list.get(0).getId(), newEventPO.getActivityId(), newEventPO.getTimeSlot());
        //todo 更新主状态为审核中
        //todo 更新草稿标记为假
    }
}
