package team.combinatorics.shuwashuwa.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team.combinatorics.shuwashuwa.annotation.AllAccess;
import team.combinatorics.shuwashuwa.model.pojo.CommonResult;
import team.combinatorics.shuwashuwa.service.ImageStorageService;
import team.combinatorics.shuwashuwa.utils.TokenUtil;

@Api(value = "图片相关接口说明")
@RestController
@RequestMapping("/api/image")
@AllArgsConstructor
public class ImageController {

    private final ImageStorageService storageService;

    @ApiOperation(value = "上传图片", notes = "上传图片到服务器并存储，暂时不与维修单关联，返回图片地址，" +
            "每个用户限制缓存6张不与维修单关联的图片，图片大小不超过1MB", httpMethod = "POST")
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ApiResponses({
            @ApiResponse(code = 200, message = "上传成功"),
            @ApiResponse(code = 40008, message = "文件存储失败")
    })
    @AllAccess
    public CommonResult<String> handleImageUpload(@RequestParam("file") MultipartFile file,
                                                 @RequestHeader("token") String token) {
        int userid = TokenUtil.extractUserid(token);
        String position = storageService.store(userid,file);

        return new CommonResult<>(200,"上传成功",position);
    }

    @ApiOperation(value = "删除缓存图片", notes = "删除缓存图片", httpMethod = "DELETE")
    @RequestMapping(value = "", method = RequestMethod.DELETE)
    @ApiResponses({
            @ApiResponse(code = 200, message = "删除成功"),
            @ApiResponse(code = 40009, message = "指定的图片路径不在用户上传记录中")
    })
    @AllAccess
    public CommonResult<String> handleImageCacheDelete(@RequestParam("path") String path,
                                                  @RequestHeader("token") String token) {
        int userid = TokenUtil.extractUserid(token);
        storageService.removeFromCache(userid,path);

        return new CommonResult<>(200,"删除成功","deleted");
    }
}
