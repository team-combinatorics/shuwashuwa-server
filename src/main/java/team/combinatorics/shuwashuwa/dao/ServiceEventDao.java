package team.combinatorics.shuwashuwa.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import team.combinatorics.shuwashuwa.dao.co.SelectServiceEventCO;
import team.combinatorics.shuwashuwa.model.dto.ServiceAbstractDTO;
import team.combinatorics.shuwashuwa.model.dto.ServiceEventDetailDTO;
import team.combinatorics.shuwashuwa.model.dto.ServiceCompleteDTO;
import team.combinatorics.shuwashuwa.model.po.ServiceEventPO;

import java.util.List;

@Repository
public interface ServiceEventDao {

    /**
     * 根据用户id建立一个新的维修事件，由于将活动id和时间段在维修单中放了一份，新建service event时已经用不到
     *
     * @param userID 用户id
     * @return 插入成功的数量，如果为0表示不成功
     */
    int insertByUserID(@Param("userID") int userID);

    // TODO 上面的方法无法得到插入后的id，下面的方法可以，自行决定用哪个

    /**
     * 插入一个新的serviceEvent
     *
     * @param serviceEventPO 一个ServiceEventPO对象
     * @return 插入成功的数量，如果为0表示不成功
     */
    int insert(@Param("serviceEvent") ServiceEventPO serviceEventPO);

    /**
     * 志愿者更新维修结果和状态，这两个必须都不为空
     *
     * @param eventID     维修事件id
     * @param volunteerID 志愿者id
     * @param result      志愿者的反馈结果
     */
    void updateByVolunteer(@Param("eventID") int eventID,
                           @Param("volunteerID") int volunteerID,
                           @Param("result") String result);


    /**
     * 用户更新反馈信息
     *
     * @param id       维修单id
     * @param feedback 用户反馈
     */
    void updateFeedback(@Param("id") int id, @Param("feedback") String feedback);


    /**
     * @param id 维修请求id
     * @return 一个完整的维修单结构
     */
    ServiceEventDetailDTO getServiceEventByID(@Param("id") int id);

    /**
     * 条件检索
     *
     * @param selectServiceEventCO 根据条件来检索维修单，条件说名见类说明
     * @return 一个维修单列表
     */

    List<ServiceEventDetailDTO> listServiceEventsByCondition(
            @Param("condition") SelectServiceEventCO selectServiceEventCO);

    // TODO 尬住了，之后再说
    /**
     * 条件检索，获取摘要列表
     * @param selectServiceEventCO 条件
     * @return 摘要列表
     */
    List<ServiceAbstractDTO> listAbstractServiceEventsByCondition(
            @Param("condition") SelectServiceEventCO selectServiceEventCO);

    // TODO 这里应该写几个简单的查询语句，例如通过维修事件id查找对应的用户，通过维修事件id查找当前状态等

    /**
     * 根据条件计数，可以用于判断是否有符合条件的元素
     *
     * @param selectServiceEventCO 条件
     * @return 计数值
     */
    int countServiceEventsByCondition(@Param("condition") SelectServiceEventCO selectServiceEventCO);

}
