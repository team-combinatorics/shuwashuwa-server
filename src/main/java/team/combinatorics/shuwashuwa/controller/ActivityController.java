package team.combinatorics.shuwashuwa.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import team.combinatorics.shuwashuwa.annotation.AllAccess;
import team.combinatorics.shuwashuwa.model.dto.ActivityReturnDTO;
import team.combinatorics.shuwashuwa.model.dto.ActivityTimeSlotDTO;
import team.combinatorics.shuwashuwa.model.pojo.CommonResult;
import team.combinatorics.shuwashuwa.service.ActivityService;

import java.util.List;

@Api("活动信息接口说明")
@RestController
@RequestMapping("api/activity")
@AllArgsConstructor
public class ActivityController {
    ActivityService activityService;

    @ApiOperation(value = "查看所有活动", notes = "返回所有发起过而未被取消的活动", httpMethod = "GET")
    @RequestMapping(value = "", method = RequestMethod.GET)
    @ApiResponses({
            @ApiResponse(code = 200, message = "请求成功")
    })
    @AllAccess
    public CommonResult<List<ActivityReturnDTO>> handleListRequest() {
        System.out.println("请求活动列表");
        return new CommonResult<>(200, "请求成功", activityService.listAllActivity());
    }

    @ApiOperation(value = "查看未开始活动", notes = "返回开始时间在请求时间之后的活动列表", httpMethod = "GET")
    @RequestMapping(value = "/coming", method = RequestMethod.GET)
    @ApiResponses({
            @ApiResponse(code = 200, message = "请求成功")
    })
    @AllAccess
    public CommonResult<List<ActivityReturnDTO>> handleComingListRequest() {
        System.out.println("请求未开始活动列表");
        return new CommonResult<>(200, "请求成功", activityService.listComingActivity());
    }


    /**
     * 查看一个活动的时间段列表
     */
    @ApiOperation(value = "查看一个活动的时间段列表", notes = "返回格式化时间段的列表", httpMethod = "GET")
    @RequestMapping(value = "/slot", method = RequestMethod.GET)
    @ApiResponses({
            @ApiResponse(code = 200, message = "请求成功")
    })
    @AllAccess
    public CommonResult<List<ActivityTimeSlotDTO>> handleTimeSlotRequest(@RequestBody Integer activityId) {
        System.out.println("请求活动"+activityId+"时间段");
        return new CommonResult<>(200, "请求成功", activityService.listTimeSlots(activityId));
    }

}
