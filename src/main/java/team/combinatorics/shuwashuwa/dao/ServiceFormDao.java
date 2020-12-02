package team.combinatorics.shuwashuwa.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import team.combinatorics.shuwashuwa.model.po.ServiceFormPO;

@Component
public interface ServiceFormDao {
    /**
     * 插入
     * @param serviceFormPO 构造的申请表信息
     */
    void insertServiceForm(@Param("form")ServiceFormPO serviceFormPO);
}
