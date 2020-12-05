package team.combinatorics.shuwashuwa.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import team.combinatorics.shuwashuwa.model.dto.ServiceFormUpdateDTO;
import team.combinatorics.shuwashuwa.model.po.ServiceFormPO;
import team.combinatorics.shuwashuwa.model.pojo.ServiceForm;

import java.util.List;

@Repository
public interface ServiceFormDao {
    /**
     * 插入
     *
     * @param serviceFormPO 构造的申请表信息
     */
    void insertServiceForm(@Param("form") ServiceFormPO serviceFormPO);

    /**
     * 管理员更新维修单信息
     *
     * @param adminID              管理员id
     * @param serviceFormUpdateDTO 更新的内容
     */
    void updateAdvice(@Param("adminID") int adminID, @Param("updateInfo") ServiceFormUpdateDTO serviceFormUpdateDTO);

    /**
     * 根据form id寻找form
     * @param id form id
     * @return 一个ServiceForm结构
     */
    ServiceForm selectServiceFormByFormID(@Param("id") int id);

    /**
     * 根据event id寻找form
     * @param eventID event id
     * @return 一个ServiceForm结构
     */
    List<ServiceForm> selectServiceFormByServiceEventID(@Param("eventID") int eventID);

}
