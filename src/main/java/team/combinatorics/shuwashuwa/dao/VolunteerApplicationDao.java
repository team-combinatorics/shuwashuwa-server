package team.combinatorics.shuwashuwa.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import team.combinatorics.shuwashuwa.model.dto.VolunteerApplicationDTO;
import team.combinatorics.shuwashuwa.model.pojo.VolunteerApplicationDO;

import java.util.List;

@Component
public interface VolunteerApplicationDao {
    /**
     * 用户申请
     * @param id 发起申请的用户id
     * @param volunteerApplicationDTO 用于申请的数据传输对象，暂时只包括申请理由这一项
     */
    void insert(@Param("id") int id, @Param("application") VolunteerApplicationDTO volunteerApplicationDTO);

    /**
     * 通过用户id寻找维修单，可能有多个结果，因此返回一个列表
     * @param id 用户id
     * @return 维修单列表
     */
    List<VolunteerApplicationDO> selectByUserId(@Param("id") int id);

    /**
     * 通过维修单id寻找维修单，只有一个结果
     * @param id 维修单id
     * @return 维修单对象
     */
    VolunteerApplicationDO selectByFormId(@Param("id") int id);
}
