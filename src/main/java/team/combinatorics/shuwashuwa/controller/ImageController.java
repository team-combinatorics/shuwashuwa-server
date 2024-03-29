package team.combinatorics.shuwashuwa.controller;

import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team.combinatorics.shuwashuwa.annotation.AllAccess;
import team.combinatorics.shuwashuwa.model.dto.CommonResult;
import team.combinatorics.shuwashuwa.service.ImageStorageService;
import team.combinatorics.shuwashuwa.utils.TokenUtil;

@Api(value = "图片相关接口说明")
@RestController
@RequestMapping("/api/image")
@AllArgsConstructor
public class ImageController {

    private final ImageStorageService storageService;

    @ApiOperation(value = "上传图片", notes = "上传图片到服务器并存储，暂时不与维修单关联，在data中返回图片地址。" +
            "每个用户限制缓存6张图片，每张图片大小不超过1MB。" +
            "同时会生产一张缩略图，若返回的文件名为xxx.png，那么生成的缩略图为100_xxx.png。" +
            "访问图片的url为/img/xxx.png或/img/100_xxx.png")
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ApiResponses({
            @ApiResponse(code = 200, message = "上传成功"),
            @ApiResponse(code = 40008, message = "文件存储失败")
    })
    @AllAccess
    public CommonResult<String> handleImageUpload(
            @RequestParam("file") @ApiParam(value = "用form-data传输的文件",required = true) MultipartFile file,
            @RequestHeader("token") @ApiParam(hidden = true) String token
    ) {
        int userid = TokenUtil.extractUserid(token);
        String location = storageService.store(userid,file);
        System.out.println("上传图片，保存为"+location);
        return new CommonResult<>(200,"上传成功", location);
    }

    @ApiOperation(value = "取消使用图片", notes = "检查图片是否在缓存队列中，若是则将其删除")
    @RequestMapping(value = "", method = RequestMethod.DELETE)
    @ApiResponses({
            @ApiResponse(code = 200, message = "删除成功"),
            @ApiResponse(code = 40009, message = "试图访问其他用户上传的图片")
    })
    @AllAccess
    public CommonResult<String> handleCancelUse(
            @RequestParam("fileName")
            @ApiParam(value = "上传时返回的文件名",example = "114-514.png",required = true)
                    String path,
            @RequestHeader("token")
            @ApiParam(hidden = true)
                    String token
    ) {
        int userid = TokenUtil.extractUserid(token);
        System.out.println("取消了图片"+path+"的使用");
        storageService.setUseless(userid, path);

        return new CommonResult<>(200,"删除成功","deleted");
    }
}
