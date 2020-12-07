package team.combinatorics.shuwashuwa.service;

import team.combinatorics.shuwashuwa.model.dto.ActivityResponseDTO;
import team.combinatorics.shuwashuwa.model.dto.ActivityTimeSlotDTO;
import team.combinatorics.shuwashuwa.model.dto.ActivityLaunchDTO;
import team.combinatorics.shuwashuwa.model.dto.ActivityUpdateDTO;

import java.util.List;

public interface ActivityService {
    void insertActivity(ActivityLaunchDTO activityLaunchDTO);

    void updateActivity(ActivityUpdateDTO activityUpdateDTO);

    void removeActivity(int id);

    List<ActivityResponseDTO> listAllActivity();

    List<ActivityResponseDTO> listComingActivity();

    List<ActivityTimeSlotDTO> listTimeSlots(Integer activityId);

}
