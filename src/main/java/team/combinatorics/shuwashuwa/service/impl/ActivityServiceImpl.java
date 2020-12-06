package team.combinatorics.shuwashuwa.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import team.combinatorics.shuwashuwa.dao.ActivityInfoDao;
import team.combinatorics.shuwashuwa.dao.co.SelectActivityCO;
import team.combinatorics.shuwashuwa.exception.ErrorInfoEnum;
import team.combinatorics.shuwashuwa.exception.KnownException;
import team.combinatorics.shuwashuwa.model.dto.ActivityInfoDTO;
import team.combinatorics.shuwashuwa.model.po.ActivityInfoPO;
import team.combinatorics.shuwashuwa.service.ActivityService;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class ActivityServiceImpl implements ActivityService {
    ActivityInfoDao activityInfoDao;

    @Override
    public void insertActivity(ActivityInfoDTO activityInfoDTO) {
        if(activityInfoDTO.getLocation() == null ||
                activityInfoDTO.getStartTime() == null ||
                activityInfoDTO.getEndTime() == null)
            throw new KnownException(ErrorInfoEnum.PARAMETER_LACKING);
        ActivityInfoPO activityInfoPO = ActivityInfoPO.builder().activityName(activityInfoDTO.getActivityName())
                .startTime(Timestamp.valueOf(activityInfoDTO.getStartTime()))
                .endTime(Timestamp.valueOf(activityInfoDTO.getEndTime()))
                .activityName(activityInfoDTO.getActivityName())
                .location(activityInfoDTO.getLocation())
                .build();
        activityInfoDao.insert(activityInfoPO);
    }

    @Override
    public void updateActivity(ActivityInfoDTO activityInfoDTO) {
        if(activityInfoDTO.getActivityId() == null)
            throw new KnownException(ErrorInfoEnum.PARAMETER_LACKING);
        if(activityInfoDTO.getLocation()==null &&
                activityInfoDTO.getStartTime() == null &&
                activityInfoDTO.getEndTime() == null &&
                activityInfoDTO.getActivityName() == null)
            throw new KnownException(ErrorInfoEnum.PARAMETER_LACKING);
        ActivityInfoPO activityInfoPO = ActivityInfoPO.builder().activityName(activityInfoDTO.getActivityName())
                .startTime(Timestamp.valueOf(activityInfoDTO.getStartTime()))
                .endTime(Timestamp.valueOf(activityInfoDTO.getEndTime()))
                .activityName(activityInfoDTO.getActivityName())
                .location(activityInfoDTO.getLocation())
                .build();
        activityInfoDao.update(activityInfoPO);
    }

    @Override
    public void removeActivity(int id) {
        if(activityInfoDao.deleteByID(id) == 1)
            System.out.println("删除了活动"+id);
    }

    @Override
    public List<ActivityInfoPO> listAllActivity() {
        return activityInfoDao.listByCondition(SelectActivityCO.builder().build());
    }

    @Override
    public List<ActivityInfoPO> listActiveActivity() {
        return activityInfoDao.listByCondition(SelectActivityCO.builder()
                .beginTime(Timestamp.valueOf(LocalDateTime.now())).build());
    }
}
