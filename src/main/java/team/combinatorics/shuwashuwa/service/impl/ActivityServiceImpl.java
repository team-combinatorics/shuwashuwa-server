package team.combinatorics.shuwashuwa.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import team.combinatorics.shuwashuwa.dao.ActivityInfoDao;
import team.combinatorics.shuwashuwa.dao.ActivityTimeSlotDao;
import team.combinatorics.shuwashuwa.dao.co.SelectActivityCO;
import team.combinatorics.shuwashuwa.exception.ErrorInfoEnum;
import team.combinatorics.shuwashuwa.exception.KnownException;
import team.combinatorics.shuwashuwa.model.dto.ActivityResponseDTO;
import team.combinatorics.shuwashuwa.model.dto.ActivityTimeSlotDTO;
import team.combinatorics.shuwashuwa.model.dto.ActivityLaunchDTO;
import team.combinatorics.shuwashuwa.model.dto.ActivityUpdateDTO;
import team.combinatorics.shuwashuwa.model.po.ActivityInfoPO;
import team.combinatorics.shuwashuwa.model.po.ActivityTimeSlotPO;
import team.combinatorics.shuwashuwa.model.pojo.ActivityTimeSlot;
import team.combinatorics.shuwashuwa.service.ActivityService;
import team.combinatorics.shuwashuwa.utils.DTOUtil;

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
        ActivityInfoPO activityInfoPO = new ActivityInfoPO();
        activityInfoPO.setActivityName(activityUpdateDTO.getActivityName());
        activityInfoPO.setLocation(activityInfoPO.getLocation());
        if(activityUpdateDTO.getStartTime()!=null)
            activityInfoPO.setStartTime(Timestamp.valueOf(activityUpdateDTO.getStartTime()));
        if(activityUpdateDTO.getEndTime()!=null)
            activityInfoPO.setEndTime(Timestamp.valueOf(activityUpdateDTO.getEndTime()));
        if(!DTOUtil.fieldAllNull(activityInfoPO))
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
    public List<ActivityResponseDTO> listAllActivity() {
        final List<ActivityInfoPO> raw = activityInfoDao.listByCondition(SelectActivityCO.builder().build());
        return formattedActivityList(raw);
    }

    @Override
    public List<ActivityResponseDTO> listComingActivity() {
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
                    DTOUtil.stamp2str(timeSlot.getStartTime()),
                    DTOUtil.stamp2str(timeSlot.getEndTime())));
        }
        return converted;
    }

    private List<ActivityResponseDTO> formattedActivityList(List<ActivityInfoPO> raw) {
        List<ActivityResponseDTO> converted = new Vector<>();
        for (ActivityInfoPO activity: raw) {
            converted.add(ActivityResponseDTO.builder()
                    .createTime(DTOUtil.stamp2str(activity.getCreateTime()))
                    .updatedTime(DTOUtil.stamp2str(activity.getUpdatedTime()))
                    .startTime(DTOUtil.stamp2str(activity.getStartTime()))
                    .endTime(DTOUtil.stamp2str(activity.getEndTime()))
                    .activityName(activity.getActivityName())
                    .id(activity.getId())
                    .location(activity.getLocation())
                    .status(activity.getStatus())
                    .build());
        }
        return converted;
    }
}
