package team.combinatorics.shuwashuwa.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import team.combinatorics.shuwashuwa.dao.ActivityInfoDao;
import team.combinatorics.shuwashuwa.dao.ActivityTimeSlotDao;
import team.combinatorics.shuwashuwa.dao.ServiceEventDao;
import team.combinatorics.shuwashuwa.dao.co.SelectActivityCO;
import team.combinatorics.shuwashuwa.dao.co.SelectServiceEventCO;
import team.combinatorics.shuwashuwa.exception.ErrorInfoEnum;
import team.combinatorics.shuwashuwa.exception.KnownException;
import team.combinatorics.shuwashuwa.model.bo.ServiceAbstractBO;
import team.combinatorics.shuwashuwa.model.dto.*;
import team.combinatorics.shuwashuwa.model.po.ActivityInfoPO;
import team.combinatorics.shuwashuwa.model.po.ActivityTimeSlotPO;
import team.combinatorics.shuwashuwa.model.bo.ActivityTimeSlotBO;
import team.combinatorics.shuwashuwa.service.ActivityService;
import team.combinatorics.shuwashuwa.utils.DTOUtil;

import java.sql.Timestamp;
import java.util.List;

@Service
@AllArgsConstructor
public class ActivityServiceImpl implements ActivityService {
    ActivityInfoDao activityInfoDao;
    ActivityTimeSlotDao timeSlotDao;
    ServiceEventDao serviceEventDao;

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

        if(activityInfoPO.getActivityName()==null) {
            activityInfoPO.setActivityName("第"+activityInfoPO.getId()+"次活动");
            activityInfoDao.update(activityInfoPO);
        }

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
        ActivityInfoPO activityInfoPO = DTOUtil.convert(activityUpdateDTO,ActivityInfoPO.class);
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
        for(ServiceAbstractBO service : serviceEventDao.listServiceAbstractsByCondition(
                SelectServiceEventCO.builder().activityId(id).build()
        )) {
            serviceEventDao.updateClosed(service.getServiceEventId(),true);
        }

        System.out.println("删除了活动时间段数据" +
                timeSlotDao.deleteByActivityID(id) + "条");
    }

    @Override
    public List<ActivityInfoPO> listActivityByConditions(SelectActivityCO co) {
        return activityInfoDao.listByCondition(co);
    }

    @Override
    public List<ActivityTimeSlotBO> listTimeSlots(Integer activityId) {
        return timeSlotDao.listTimeSlotsByActivityID(activityId);
    }

    @Override
    public void setActive(int userid, Integer activityId) {
        if(activityId==null)
            throw new KnownException(ErrorInfoEnum.PARAMETER_LACKING);

        serviceEventDao.listServiceAbstractsByCondition(
                SelectServiceEventCO.builder().userId(userid).activityId(activityId).status(2).closed(false).build()
        ).forEach(x -> serviceEventDao.updateStatus(x.getServiceEventId(),3));
    }

    @Override
    public Boolean haveAttended(int userId, int activityId) {
        final SelectServiceEventCO co = SelectServiceEventCO.builder()
                .userId(userId)
                .activityId(activityId)
                .status(3)
        .build();
        int count = 0;
        count += serviceEventDao.countServiceEventsByCondition(co);
        co.setStatus(4);
        count += serviceEventDao.countServiceEventsByCondition(co);
        co.setStatus(5);
        count += serviceEventDao.countServiceEventsByCondition(co);
        return count>0;
    }
}
