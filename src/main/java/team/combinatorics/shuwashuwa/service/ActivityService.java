package team.combinatorics.shuwashuwa.service;

import team.combinatorics.shuwashuwa.dao.co.SelectActivityCO;
import team.combinatorics.shuwashuwa.model.bo.ActivityTimeSlotBO;
import team.combinatorics.shuwashuwa.model.dto.ActivityResponseDTO;
import team.combinatorics.shuwashuwa.model.dto.ActivityTimeSlotDTO;
import team.combinatorics.shuwashuwa.model.dto.ActivityLaunchDTO;
import team.combinatorics.shuwashuwa.model.dto.ActivityUpdateDTO;
import team.combinatorics.shuwashuwa.model.po.ActivityInfoPO;

import java.util.List;

public interface ActivityService {
    void insertActivity(ActivityLaunchDTO activityLaunchDTO);

    void updateActivity(ActivityUpdateDTO activityUpdateDTO);

    void removeActivity(int id);

    List<ActivityInfoPO> listActivityByConditions(SelectActivityCO co);

    List<ActivityTimeSlotBO> listTimeSlots(Integer activityId);

    void setActive(int userid, Integer activityId);

    Boolean haveAttended(int userId,int activityId);

}
