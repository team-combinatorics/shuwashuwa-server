package team.combinatorics.shuwashuwa.service;

import team.combinatorics.shuwashuwa.model.dto.ActivityInfoDTO;
import team.combinatorics.shuwashuwa.model.po.ActivityInfoPO;

import java.util.List;

public interface ActivityService {
    void insertActivity(ActivityInfoDTO activityInfoDTO);

    void updateActivity(ActivityInfoDTO activityInfoDTO);

    void removeActivity(int id);

    List<ActivityInfoPO> listAllActivity();

    List<ActivityInfoPO> listActiveActivity();

}
