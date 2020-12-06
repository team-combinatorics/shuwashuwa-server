package team.combinatorics.shuwashuwa.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import team.combinatorics.shuwashuwa.annotation.AllAccess;
import team.combinatorics.shuwashuwa.annotation.NoToken;
import team.combinatorics.shuwashuwa.annotation.SUAccess;
import team.combinatorics.shuwashuwa.model.dto.AdminDTO;
import team.combinatorics.shuwashuwa.model.po.AdminPO;
import team.combinatorics.shuwashuwa.model.pojo.CommonResult;
import team.combinatorics.shuwashuwa.service.ImageStorageService;
import team.combinatorics.shuwashuwa.service.SuperAdministratorService;
import team.combinatorics.shuwashuwa.utils.RequestCheckUtil;

import javax.validation.constraints.NotNull;
import java.util.List;

@Api(value = "超级管理员相关接口说明")
@RestController
@Validated
@RequestMapping("/api/super")
@AllArgsConstructor
public class SuperAdministratorController {
    private final SuperAdministratorService superAdministratorService;
    private final ImageStorageService storageService;

    /**
     * 超级管理员登录系统
     */
    @ApiOperation(value = "超级管理员登录", notes = "通过输入内置的超级管理员用户名和密码进行登录", httpMethod = "GET")
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    @ApiResponses({
            @ApiResponse(code = 200, message = "登录成功"),
            @ApiResponse(code = 40011, message = "用户名或密码错误")
    })
    @NoToken
    public CommonResult<String> loginHandler(@NotNull(message = "用户名不能为空") String userName,
                                             @NotNull(message = "密码不能为空") String password)
    {
        String token = superAdministratorService.checkInfo(userName, password);
        if(token != null)
        {
            System.out.println("超级管理员请求登录");
            return new CommonResult<>(200, "登录成功", token);
        }
        return new CommonResult<>(40011, "用户名或密码错误", "");
    }

    /**
     * 超级管理员修改密码
     */
    @ApiOperation(value = "修改超级管理员密码", notes = "比对输入的原始密码后，将新密码转换为MD5格式后储存至数据库", httpMethod = "PUT")
    @RequestMapping(value = "/change", method = RequestMethod.PUT)
    @ApiResponses({
            @ApiResponse(code = 200, message = "修改成功"),
            @ApiResponse(code = 40011, message = "原始密码错误")
    })
    @SUAccess
    public CommonResult<String> changePassword(@RequestHeader @NotNull(message = "原始密码不能为空") String oldPassword,
                                               @RequestHeader @NotNull(message = "新密码不能为空") String newPassword)
    {
        boolean success = superAdministratorService.changePassword(oldPassword, newPassword);
        if(success)
            return new CommonResult<>(200, "修改成功", "Change password successfully!");
        /*TODO: 添加对数据库返回值异常的error code*/
        return new CommonResult<>(40011, "原始密码错误", "Wrong old password!");
    }

    /**
     * 超管获取缓存图片数量
     */
    @ApiOperation(value = "获取缓存图片数量", notes = "超管专属", httpMethod = "GET")
    @RequestMapping(value = "/cache", method = RequestMethod.GET)
    @ApiResponses({
            @ApiResponse(code = 200, message = "获取成功")
    })
    @SUAccess
    public CommonResult<Integer> getImageCacheNumber() {
        return new CommonResult<>(200,"请求成功",storageService.countCacheImages());
    }

    /**
     * 超管删除所有缓存图片
     */
    @ApiOperation(value = "删除指定日期前的所有缓存图片", notes = "超管专属", httpMethod = "DELETE")
    @RequestMapping(value = "/cache", method = RequestMethod.DELETE)
    @ApiResponses({
            @ApiResponse(code = 200, message = "删除成功")
    })
    @SUAccess
    public CommonResult<String> handleImageCacheClear(@RequestParam("days") int days) {
        storageService.clearCacheByTime(days);
        return new CommonResult<>(200,"删除成功","deleted");
    }

    /**
     * 超管添加管理员
     */
    @ApiOperation(value = "根据输入的信息添加新的管理员", notes = "超管专属", httpMethod = "POST")
    @RequestMapping(value = "/admin", method = RequestMethod.POST)
    @ApiResponses({
            @ApiResponse(code = 200, message = "添加成功"),
            @ApiResponse(code = 40010, message = "添加失败，信息不完整")
    })
    @SUAccess
    public CommonResult<String> addNewAdministrator(@RequestBody @NotNull(message = "管理员信息不能为空") AdminDTO adminDTO)
    {
        System.out.println(adminDTO.getUserid());
        if(RequestCheckUtil.fieldExistNull(adminDTO))
        {
            return new CommonResult<>(40010, "添加失败，信息不完整", "You need to fill all information");
        }
        System.out.println(adminDTO.getUserid()+"将被添加为管理员");
        int cnt = superAdministratorService.addAdministrator(adminDTO);
        if(cnt == 1)
        {
            return new CommonResult<>(200, "添加成功", "success");
        }
        /*TODO: 或需要为数据库返回异常值添加一个error code?*/
        return new CommonResult<>(40000, "数据库操作失败", "database failure");
    }

    /**
     * 超管修改管理员信息
     */
    @ApiOperation(value = "根据输入修改管理员信息", notes = "超管专属", httpMethod = "PATCH")
    @RequestMapping(value = "/admin", method = RequestMethod.PATCH)
    @ApiResponses({
            @ApiResponse(code = 200, message = "更新成功"),
            @ApiResponse(code = 40010, message = "更新失败，信息不能全为空")
    })
    @SUAccess
    public CommonResult<String> changeAdministratorInfo()
    {
        return null;
    }

    /**
     * 超管删除管理员
     */
    @ApiOperation(value = "根据输入的信息删除对应的管理员", notes = "超管专属", httpMethod = "DELETE")
    @RequestMapping(value = "/admin", method = RequestMethod.DELETE)
    @ApiResponses({
            @ApiResponse(code = 200, message = "添加成功"),
            @ApiResponse(code = 40010, message = "添加失败，信息不完整")
    })
    @SUAccess
    public CommonResult<String> deleteAdministrator()
    {
        return null;
    }

    /**
     * 超管获取单个管理员的信息
     */
    @ApiOperation(value = "超级管理员获取单个管理员的详细信息", notes = "超管专属", httpMethod = "GET")
    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    @ApiResponses({
            @ApiResponse(code = 200, message = "添加成功"),
            @ApiResponse(code = 40010, message = "添加失败，信息不完整")
    })
    @SUAccess
    public CommonResult<String> getAdministratorInfo()
    {
        return null;
    }

    /**
     * 超管获取管理员列表
     */
    @ApiOperation(value = "超级管理员获取所有管理员的列表", notes = "超管专属", httpMethod = "GET")
    @RequestMapping(value = "/admin/list", method = RequestMethod.GET)
    @ApiResponses({
            @ApiResponse(code = 200, message = "获取成功"),
    })
    @SUAccess
    public CommonResult<List<AdminPO>> getAdministratorList()
    {
        return new CommonResult<>(200, "获取成功", superAdministratorService.getAdministratorList());
    }

}
