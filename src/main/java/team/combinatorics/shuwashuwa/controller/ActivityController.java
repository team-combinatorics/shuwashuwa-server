package team.combinatorics.shuwashuwa.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import team.combinatorics.shuwashuwa.annotation.AllAccess;
import team.combinatorics.shuwashuwa.dao.co.SelectActivityCO;
import team.combinatorics.shuwashuwa.model.bo.ActivityTimeSlotBO;
import team.combinatorics.shuwashuwa.model.dto.ActivityResponseDTO;
import team.combinatorics.shuwashuwa.model.dto.ActivityTimeSlotDTO;
import team.combinatorics.shuwashuwa.model.dto.CommonResult;
import team.combinatorics.shuwashuwa.model.po.ActivityInfoPO;
import team.combinatorics.shuwashuwa.service.ActivityService;
import team.combinatorics.shuwashuwa.utils.DTOUtil;
import team.combinatorics.shuwashuwa.utils.TokenUtil;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Api("活动信息接口说明")
@RestController
@RequestMapping("api/activity")
@AllArgsConstructor
public class ActivityController {
    ActivityService activityService;

    @ApiOperation(value = "根据条件筛选活动列表", notes = "不需要筛选的条件无需赋值")
    @RequestMapping(value = "", method = RequestMethod.GET)
    @AllAccess
    public CommonResult<List<ActivityResponseDTO>> handleListRequest(
            @RequestParam(value = "startLower", required = false)
            @ApiParam(value = "开始时间下界，以yyyy-MM-dd HH:mm:ss表示")
                    String startTimeLowerBound,
            @RequestParam(value = "startUpper", required = false)
            @ApiParam(value = "开始时间上界，以yyyy-MM-dd HH:mm:ss表示")
                    String startTimeUpperBound,
            @RequestParam(value = "endLower", required = false)
            @ApiParam(value = "结束时间下界，以yyyy-MM-dd HH:mm:ss表示")
                    String endTimeLowerBound,
            @RequestParam(value = "endUpper", required = false)
            @ApiParam(value = "结束时间上界，以yyyy-MM-dd HH:mm:ss表示")
                    String endTimeUpperBound
    ) {
        System.out.println("请求活动列表");
        SelectActivityCO co = new SelectActivityCO();
        if (startTimeLowerBound != null) co.setStartTimeLowerBound(Timestamp.valueOf(startTimeLowerBound));
        if (startTimeUpperBound != null) co.setStartTimeUpperBound(Timestamp.valueOf(startTimeUpperBound));
        if (endTimeLowerBound != null) co.setEndTimeLowerBound(Timestamp.valueOf(endTimeLowerBound));
        if (endTimeUpperBound != null) co.setEndTimeUpperBound(Timestamp.valueOf(endTimeUpperBound));
        final List<ActivityInfoPO> poList = activityService.listActivityByConditions(co);
        List<ActivityResponseDTO> dtoList = poList.stream()
                .map(x -> DTOUtil.convert(x, ActivityResponseDTO.class))
                .collect(Collectors.toList());
        return new CommonResult<>(200, "请求成功", dtoList);
    }

    @ApiOperation("用户活动现场签到，返回状态改变的维修单数量")
    @RequestMapping(value = "/attend", method = RequestMethod.PUT)
    @AllAccess
    public CommonResult<String> handlePresence(
            @RequestHeader("token") @ApiParam(hidden = true) String token,
            @RequestParam(value = "activity") @ApiParam(value = "活动Id，从二维码参数获取", required = true) Integer activityId
    ) {
        int userid = TokenUtil.extractUserid(token);
        System.out.println("用户" + userid + "在活动" + activityId + "签到");
        activityService.setActive(userid, activityId);
        return new CommonResult<>(200, "请求成功", "success");
    }

    @ApiOperation("查看一个活动的时间段列表")
    @RequestMapping(value = "/slot", method = RequestMethod.GET)
    @AllAccess
    public CommonResult<List<ActivityTimeSlotDTO>> handleTimeSlotRequest(
            @RequestParam("activity") @ApiParam(value = "活动ID", required = true) Integer activityId) {
        System.out.println("请求活动" + activityId + "时间段");
        final List<ActivityTimeSlotBO> boList = activityService.listTimeSlots(activityId);
        List<ActivityTimeSlotDTO> dtoList = boList.stream()
                .map(x -> DTOUtil.convert(x, ActivityTimeSlotDTO.class))
                .collect(Collectors.toList());
        return new CommonResult<>(200, "请求成功", dtoList);
    }

    @ApiOperation("查询当前用户在某活动中是否进行有效签到")
    @RequestMapping(value = "/attend", method = RequestMethod.GET)
    @AllAccess
    public CommonResult<Boolean> attendingActivity(
            @RequestHeader("token") @ApiParam(hidden = true) String token,
            @RequestParam("activity") @ApiParam(value = "活动id", required = true) Integer activityId
    ) {
        int userid = TokenUtil.extractUserid(token);
        System.out.println("用户" + userid + "查询了活动" + activityId + "的签到结果");
        boolean result = activityService.haveAttended(userid, activityId);
        return new CommonResult<>(200, "请求成功", result);
    }
}
