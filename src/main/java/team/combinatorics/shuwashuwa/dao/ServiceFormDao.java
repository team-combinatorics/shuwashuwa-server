package team.combinatorics.shuwashuwa.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import team.combinatorics.shuwashuwa.model.dto.ServiceFormUpdateDTO;
import team.combinatorics.shuwashuwa.model.po.ServiceFormPO;

@Component
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

}
