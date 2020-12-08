package team.combinatorics.shuwashuwa.service;

import team.combinatorics.shuwashuwa.model.dto.ServiceFormDTO;

public interface EventService {

    /**
     * 用户提交维修单。如果是第一次提交，会创建对应的维修事件，此外还会对应地修改维修单数据库
     * @param serviceFormDTO 用户提交的内容
     * @param timeSlot 选择的时间段
     * @param activityID 对应的活动ID，由前端提供
     */
    void commitForm(int userid, ServiceFormDTO serviceFormDTO, Integer timeSlot, Integer activityID);
}
