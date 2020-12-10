package team.combinatorics.shuwashuwa.service;

import team.combinatorics.shuwashuwa.model.dto.ServiceEventDetailDTO;
import team.combinatorics.shuwashuwa.model.dto.ServiceFormSubmitDTO;

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

}
