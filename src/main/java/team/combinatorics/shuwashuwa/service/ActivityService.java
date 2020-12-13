package team.combinatorics.shuwashuwa.service;

import team.combinatorics.shuwashuwa.dao.co.SelectActivityCO;
import team.combinatorics.shuwashuwa.model.dto.ActivityResponseDTO;
import team.combinatorics.shuwashuwa.model.dto.ActivityTimeSlotDTO;
import team.combinatorics.shuwashuwa.model.dto.ActivityLaunchDTO;
import team.combinatorics.shuwashuwa.model.dto.ActivityUpdateDTO;

import java.util.List;

public interface ActivityService {
    void insertActivity(ActivityLaunchDTO activityLaunchDTO);

    void updateActivity(ActivityUpdateDTO activityUpdateDTO);

    void removeActivity(int id);

    List<ActivityResponseDTO> listActivityByConditions(SelectActivityCO co);

    List<ActivityTimeSlotDTO> listTimeSlots(Integer activityId);

    Boolean haveAttended(int userId,int activityId);

}
