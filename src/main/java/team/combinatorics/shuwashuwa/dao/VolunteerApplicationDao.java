package team.combinatorics.shuwashuwa.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import team.combinatorics.shuwashuwa.model.dto.VolunteerApplicationDTO;
import team.combinatorics.shuwashuwa.model.pojo.VolunteerApplicationDO;

@Component
public interface VolunteerApplicationDao {
    void insert(@Param("id") int id, @Param("application") VolunteerApplicationDTO volunteerApplicationDTO);

    VolunteerApplicationDO selectByUserId(@Param("id") int id);

    VolunteerApplicationDO selectByFormId(@Param("id") int id);
}
