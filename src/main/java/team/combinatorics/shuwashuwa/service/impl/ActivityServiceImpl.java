package team.combinatorics.shuwashuwa.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import team.combinatorics.shuwashuwa.dao.ActivityInfoDao;
import team.combinatorics.shuwashuwa.dao.ActivityTimeSlotDao;
import team.combinatorics.shuwashuwa.dao.co.SelectActivityCO;
import team.combinatorics.shuwashuwa.exception.ErrorInfoEnum;
import team.combinatorics.shuwashuwa.exception.KnownException;
import team.combinatorics.shuwashuwa.model.dto.ActivityReturnDTO;
import team.combinatorics.shuwashuwa.model.dto.ActivityTimeSlotDTO;
import team.combinatorics.shuwashuwa.model.dto.ActivityLaunchDTO;
import team.combinatorics.shuwashuwa.model.dto.ActivityUpdateDTO;
import team.combinatorics.shuwashuwa.model.po.ActivityInfoPO;
import team.combinatorics.shuwashuwa.model.po.ActivityTimeSlotPO;
import team.combinatorics.shuwashuwa.model.pojo.ActivityTimeSlot;
import team.combinatorics.shuwashuwa.service.ActivityService;
import team.combinatorics.shuwashuwa.utils.RequestCheckUtil;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Vector;

@Service
@AllArgsConstructor
public class ActivityServiceImpl implements ActivityService {
    ActivityInfoDao activityInfoDao;
    ActivityTimeSlotDao timeSlotDao;

    @Override
    public void insertActivity(ActivityLaunchDTO activityLaunchDTO) {
        if(activityLaunchDTO.getLocation() == null ||
                activityLaunchDTO.getStartTime() == null ||
                activityLaunchDTO.getEndTime() == null ||
                activityLaunchDTO.getTimeSlots() == null)
            throw new KnownException(ErrorInfoEnum.PARAMETER_LACKING);

        //插入活动
        ActivityInfoPO activityInfoPO = ActivityInfoPO.builder()
                .startTime(Timestamp.valueOf(activityLaunchDTO.getStartTime()))
                .endTime(Timestamp.valueOf(activityLaunchDTO.getEndTime()))
                .activityName(activityLaunchDTO.getActivityName())
                .location(activityLaunchDTO.getLocation())
                .build();
        activityInfoDao.insert(activityInfoPO);

        //关联活动时间段
        addTimeSlots(activityInfoPO.getId(), activityLaunchDTO.getTimeSlots());
    }

    @Override
    public void updateActivity(ActivityUpdateDTO activityUpdateDTO) {
        if(activityUpdateDTO.getActivityId() == null)
            throw new KnownException(ErrorInfoEnum.PARAMETER_LACKING);
        if(activityUpdateDTO.getLocation()==null &&
                activityUpdateDTO.getStartTime() == null &&
                activityUpdateDTO.getEndTime() == null &&
                activityUpdateDTO.getActivityName() == null &&
                activityUpdateDTO.getTimeSlots() == null)
            throw new KnownException(ErrorInfoEnum.PARAMETER_LACKING);

        //更新活动整体信息
        ActivityInfoPO activityInfoPO = ActivityInfoPO.builder()
                .startTime(Timestamp.valueOf(activityUpdateDTO.getStartTime()))
                .endTime(Timestamp.valueOf(activityUpdateDTO.getEndTime()))
                .activityName(activityUpdateDTO.getActivityName())
                .location(activityUpdateDTO.getLocation())
                .build();
        if(!RequestCheckUtil.fieldAllNull(activityInfoPO))
        {
            activityInfoPO.setId(activityUpdateDTO.getActivityId());
            activityInfoDao.update(activityInfoPO);
        }

        //更新活动时间段
        if(activityUpdateDTO.getTimeSlots()!=null)
        {
            timeSlotDao.deleteByActivityID(activityUpdateDTO.getActivityId());
            addTimeSlots(activityUpdateDTO.getActivityId(), activityUpdateDTO.getTimeSlots());
        }
    }

    private void addTimeSlots(Integer activityId, List<ActivityTimeSlotDTO> timeSlots) {
        ActivityTimeSlotPO timeSlotPO = ActivityTimeSlotPO.builder()
                .activityId(activityId).build();
        for (ActivityTimeSlotDTO timeSlotDTO : timeSlots) {
            timeSlotPO.setStartTime(Timestamp.valueOf(timeSlotDTO.getStartTime()));
            timeSlotPO.setEndTime(Timestamp.valueOf(timeSlotDTO.getEndTime()));
            timeSlotPO.setTimeSlot(timeSlotDTO.getTimeSlot());
            timeSlotDao.insert(timeSlotPO);
        }
    }

    @Override
    public void removeActivity(int id) {
        if(activityInfoDao.deleteByID(id) == 1)
            System.out.println("删除了活动"+id);
        //todo: 可能需要更新维修单
        System.out.println("删除了活动时间段数据" +
                timeSlotDao.deleteByActivityID(id) + "条");
    }

    @Override
    public List<ActivityReturnDTO> listAllActivity() {
        final List<ActivityInfoPO> raw = activityInfoDao.listByCondition(SelectActivityCO.builder().build());
        return formattedActivityList(raw);
    }

    @Override
    public List<ActivityReturnDTO> listComingActivity() {
        final List<ActivityInfoPO> raw = activityInfoDao.listByCondition(SelectActivityCO.builder()
                .beginTime(Timestamp.valueOf(LocalDateTime.now())).build());
        return formattedActivityList(raw);
    }

    @Override
    public List<ActivityTimeSlotDTO> listTimeSlots(Integer activityId) {
        List<ActivityTimeSlot> raw = timeSlotDao.listTimeSlotsByActivityID(activityId);
        List<ActivityTimeSlotDTO> converted = new Vector<>();
        for(ActivityTimeSlot timeSlot: raw) {
            converted.add(new ActivityTimeSlotDTO(timeSlot.getTimeSlot(),
                    timeSlot.getStartTime().toString(),
                    timeSlot.getEndTime().toString()));
        }
        return converted;
    }

    private List<ActivityReturnDTO> formattedActivityList(List<ActivityInfoPO> raw) {
        List<ActivityReturnDTO> converted = new Vector<>();
        for (ActivityInfoPO activity: raw) {
            converted.add(ActivityReturnDTO.builder()
                    .activityName(activity.getActivityName())
                    .createTime(activity.getCreateTime().toString())
                    .endTime(activity.getEndTime().toString())
                    .id(activity.getId())
                    .location(activity.getLocation())
                    .status(activity.getStatus())
                    .startTime(activity.getStartTime().toString())
                    .updatedTime(activity.getUpdatedTime().toString())
                    .build());
        }
        return converted;
    }
}
