package team.combinatorics.shuwashuwa.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("志愿者申请表的详细信息结构")
public class VolunteerApplicationDetailDTO {
    @ApiModelProperty("申请表id")
    private Integer id;
    @ApiModelProperty("申请表提交时间")
    private Timestamp createTime;
    /**
     * 申请表应该只会被管理员修改一次，所以这个应该没问题
     * 这个时间等于提交时间的时候说明还没有审核
     * 总之先写了这个属性，用不用之后再说
     */
    @ApiModelProperty("申请表审核时间")
    private Timestamp updatedTime;
    @ApiModelProperty("发起申请的用户的用户id")
    private Integer userId;
    @ApiModelProperty("用户填写的申请理由")
    private String reasonForApplication;
    @ApiModelProperty("管理员的回复")
    private String replyByAdmin;
    @ApiModelProperty("回复的管理员的管理员id")
    private Integer adminId;
    @ApiModelProperty("回复的管理员的姓名")
    private String adminName;
    @ApiModelProperty("申请状态")
    private Integer status;
    @ApiModelProperty("校园卡照片的路径")
    private String cardPicLocation;
    /**
     * 用户的个人信息部分
     */
    @ApiModelProperty("发起申请的用户的姓名")
    private String userName;
    @ApiModelProperty("发起申请的用户的手机号")
    private String phoneNumber;
    @ApiModelProperty("发起申请的用户的邮箱")
    private String email;
    @ApiModelProperty("发起申请的用户的院系")
    private String department;
    @ApiModelProperty("发起申请的用户的年级")
    private String grade;
    @ApiModelProperty("用户身份（其实应该都是学生）")
    private String identity;
    @ApiModelProperty("用户的学号，管理员应当与校园卡图片对比审核")
    private String studentId;
}
