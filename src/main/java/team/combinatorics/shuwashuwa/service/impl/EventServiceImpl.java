package team.combinatorics.shuwashuwa.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import team.combinatorics.shuwashuwa.dao.ServiceEventDao;
import team.combinatorics.shuwashuwa.dao.ServiceFormDao;
import team.combinatorics.shuwashuwa.dao.co.SelectServiceEventCO;
import team.combinatorics.shuwashuwa.model.dto.ServiceEventResponseDTO;
import team.combinatorics.shuwashuwa.model.dto.ServiceFormDTO;
import team.combinatorics.shuwashuwa.model.dto.ServiceFormUpdateDTO;
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
    public void commitForm(int userid, ServiceFormDTO serviceFormDTO, Integer timeSlot, Integer activityID) {
        ServiceFormPO serviceFormPO = (ServiceFormPO) DTOUtil.convert(serviceFormDTO, ServiceFormPO.class);
        // 每个活动中用户应该只有一个维修事件，所以这个条件查询的list长度应该为0或者1
        List<ServiceEventResponseDTO> list = serviceEventDao.listServiceEventsByCondition(SelectServiceEventCO.builder()
                .userId(userid)
                .activityId(activityID)
                .build());
        if(list.size()==0) { // 第一次提交，不存在维修事件，那么新建一个
            ServiceEventPO serviceEventPO = ServiceEventPO.builder()
                    .userId(userid)
                    .status(0)
                    .activityId(activityID)
                    .timeSlot(timeSlot)
                    .build();
            int cntEvent = serviceEventDao.insert(serviceEventPO);

            /* TODO: 需要进行是否存在维修单草稿的判断 */
            serviceFormPO.setServiceEventId(serviceEventPO.getId());
        }
        else { // 第二次及以上的提交，存在维修事件
            /* TODO: 需要进行是否存在维修单草稿的判断 */
            serviceFormPO.setServiceEventId(list.get(0).getId());
        }
        int cntForm = serviceFormDao.insert(serviceFormPO);
    }
}
