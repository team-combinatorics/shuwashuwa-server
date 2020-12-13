package team.combinatorics.shuwashuwa.controller;

import io.swagger.annotations.*;
import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import team.combinatorics.shuwashuwa.annotation.AllAccess;
import team.combinatorics.shuwashuwa.annotation.UserParam;
import team.combinatorics.shuwashuwa.dao.co.SelectActivityCO;
import team.combinatorics.shuwashuwa.model.dto.ActivityResponseDTO;
import team.combinatorics.shuwashuwa.model.dto.ActivityTimeSlotDTO;
import team.combinatorics.shuwashuwa.model.pojo.CommonResult;
import team.combinatorics.shuwashuwa.service.ActivityService;
import team.combinatorics.shuwashuwa.utils.TokenUtil;

import java.sql.Timestamp;
import java.util.List;

@Api("活动信息接口说明")
@RestController
@RequestMapping("api/activity")
@AllArgsConstructor
public class ActivityController {
    ActivityService activityService;

    @ApiOperation("根据条件筛选活动列表")
    @RequestMapping(value = "", method = RequestMethod.GET)
    @AllAccess
    public CommonResult<List<ActivityResponseDTO>> handleListRequest(
            @RequestParam("startLower") @ApiParam("开始时间下界") String startTimeLowerBound,
            @RequestParam("startUpper")  @ApiParam("开始时间上界") String startTimeUpperBound,
            @RequestParam("endLower")  @ApiParam("结束时间下界") String endTimeLowerBound,
            @RequestParam("endUpper")  @ApiParam("结束时间上界") String endTimeUpperBound

    ) {
        System.out.println("请求活动列表");
        return new CommonResult<>(200, "请求成功",
                activityService.listActivityByConditions(
                    SelectActivityCO.builder()
                        .beginTime(Timestamp.valueOf(startTimeLowerBound))
                        .endTime(Timestamp.valueOf(startTimeUpperBound))
                    .build()
                )
        );
    }

    @ApiOperation("查看一个活动的时间段列表")
    @RequestMapping(value = "/slot", method = RequestMethod.GET)
    @AllAccess
    public CommonResult<List<ActivityTimeSlotDTO>> handleTimeSlotRequest(
            @RequestParam("activity") @ApiParam(value = "活动ID",required = true) Integer activityId) {
        System.out.println("请求活动"+activityId+"时间段");
        return new CommonResult<>(200, "请求成功", activityService.listTimeSlots(activityId));
    }

    @ApiOperation("查询某用户在某活动中是否进行有效签到")
    @RequestMapping(value = "/attend", method = RequestMethod.GET)
    @AllAccess
    @UserParam("user")
    public CommonResult<Boolean> attendingActivity(
            @RequestHeader("token") @ApiParam(hidden = true) String token
    ) {
        int userid = TokenUtil.extractUserid(token);
        return new CommonResult<>(200,"请求成功",true);
    }
}
