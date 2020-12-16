package team.combinatorics.shuwashuwa.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import team.combinatorics.shuwashuwa.dao.AdminDao;
import team.combinatorics.shuwashuwa.dao.UserDao;
import team.combinatorics.shuwashuwa.dao.VolunteerApplicationDao;
import team.combinatorics.shuwashuwa.dao.co.SelectApplicationCO;
import team.combinatorics.shuwashuwa.exception.ErrorInfoEnum;
import team.combinatorics.shuwashuwa.exception.KnownException;
import team.combinatorics.shuwashuwa.model.dto.*;
import team.combinatorics.shuwashuwa.model.po.AdminPO;
import team.combinatorics.shuwashuwa.model.po.UserPO;
import team.combinatorics.shuwashuwa.model.po.VolunteerApplicationPO;
import team.combinatorics.shuwashuwa.service.ImageStorageService;
import team.combinatorics.shuwashuwa.service.UserService;
import team.combinatorics.shuwashuwa.utils.DTOUtil;
import team.combinatorics.shuwashuwa.utils.TokenUtil;
import team.combinatorics.shuwashuwa.utils.WechatUtil;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final VolunteerApplicationDao applicationDao;
    private final AdminDao adminDao;
    ImageStorageService imageStorageService;

    @Override
    public int deleteOneUser(int userid) {
        System.out.println("要删除用户的userid为：" + userid);
        return userDao.deleteUserByUserid(userid);
    }

    @Override
    public void deleteAllUsers() {
        userDao.deleteAllUsers();
    }

    @Override
    public LogInSuccessDTO wechatLogin(LogInInfoDTO logInInfoDto) throws Exception {
        String openid = WechatUtil.getOpenID(logInInfoDto.getCode());
        System.out.println("用户登录 @Service");
        System.out.println("openid: " + openid);
        LogInSuccessDTO logInSuccessDto = new LogInSuccessDTO();
        UserPO userPO = userDao.getUserByOpenid(openid);
        if (userPO == null) {
            logInSuccessDto.setFirstLogin(true);
            userDao.insertUserByOpenid(openid);
            userPO = userDao.getUserByOpenid(openid);
        } else
            logInSuccessDto.setFirstLogin(false);
        String token = TokenUtil.createToken(userPO.getId());
        logInSuccessDto.setToken(token);
        return logInSuccessDto;
    }

    @Override
    public void updateUserInfo(int userid, UserInfoUpdateDTO userInfoUpdateDto) {
        if (DTOUtil.fieldAllNull(userInfoUpdateDto))
            throw new KnownException(ErrorInfoEnum.PARAMETER_LACKING);
        userDao.updateUserInfo(userid, userInfoUpdateDto);
    }

    @Override
    public UserInfoResponseDTO getUserInfo(int userid) {
        UserPO userPO = userDao.getUserByUserid(userid);
        return (UserInfoResponseDTO) DTOUtil.convert(userPO, UserInfoResponseDTO.class);
    }

    /**
     * 处理志愿者申请
     * @param userid 申请成为志愿者的用户的id
     * @param additionDTO 包括申请理由和学生证照片位置
     */
    @Override
    public void addVolunteerApplication(int userid, VolunteerApplicationAdditionDTO additionDTO) {
        //参数检查
        if (DTOUtil.fieldExistNull(additionDTO))
            throw new KnownException(ErrorInfoEnum.PARAMETER_LACKING);

        //重复申请检查
        if (userDao.getUserByUserid(userid).getVolunteer())
            throw new KnownException(ErrorInfoEnum.DUPLICATED_PROMOTION);

        //插入新的申请
        VolunteerApplicationPO volunteerApplicationPO =
                (VolunteerApplicationPO) DTOUtil.convert(additionDTO, VolunteerApplicationPO.class);

        volunteerApplicationPO.setUserId(userid);

        applicationDao.insert(volunteerApplicationPO);

        //存档图片
        imageStorageService.setUseful(additionDTO.getCardPicLocation());
    }

    @Override
    public List<VolunteerApplicationResponseForAdminDTO> listUnauditedVolunteerApplication() {
        final List<VolunteerApplicationPO> raw = applicationDao.listApplicationsByCondition(
                SelectApplicationCO.builder().status(0).build());
        return raw.stream().map(
                (x) -> (VolunteerApplicationResponseForAdminDTO)
                        DTOUtil.convert(x, VolunteerApplicationResponseForAdminDTO.class)
        ).collect(Collectors.toList());
    }

    @Override
    public List<VolunteerApplicationResultDTO> listVolunteerApplicationOf(int userid) {
        final List<VolunteerApplicationPO> raw = applicationDao.listApplicationsByUserId(userid);
        return raw.stream().map(
                (x) -> (VolunteerApplicationResultDTO)
                        DTOUtil.convert(x, VolunteerApplicationResultDTO.class)
        ).collect(Collectors.toList());
    }

    /**
     * 处理志愿者申请
     *
     * @param adminUserid    管理员的userid 注意用这个换取管理员的admin id
     * @param updateDTO 用于更新的dto
     */
    @Override
    public void completeApplicationAudition(int adminUserid, VolunteerApplicationUpdateDTO updateDTO) {
        // 获取管理员id
        int adminID = adminDao.getAdminIDByUserID(adminUserid);
        // 判断更新数据是否完整
        if (updateDTO.getFormID() == null || updateDTO.getStatus() == null || updateDTO.getReplyByAdmin() == null)
            throw new KnownException(ErrorInfoEnum.PARAMETER_LACKING);
        // TODO 这里的细节问题之后再实现
        // 随便设置个尝试上限
        for (int i = 0; i < 3; i++) {
            // 首先取出当前的结构
            VolunteerApplicationPO volunteerApplicationPO = applicationDao.getApplicationByFormId(updateDTO.getFormID());
            // 如果状态已经被更新过，则不再更新
            if (volunteerApplicationPO.getStatus() != 0)
                throw new KnownException(ErrorInfoEnum.STATUS_UNMATCHED);
            // 更新状态，附带上次更新时间信息
            int returnValue = applicationDao.updateApplicationByAdmin(adminID,
                    updateDTO,
                    volunteerApplicationPO.getUpdatedTime());
            // 返回值为1说明更新成功，然后进行之后的处理
            if (returnValue == 1) {
                imageStorageService.delete(volunteerApplicationPO.getCardPicLocation());
                if (updateDTO.getStatus() == 1) {
                    Integer promotedUserId = applicationDao.getApplicationByFormId(updateDTO.getFormID()).getUserId();
                    userDao.updateUserVolunteerAuthority(promotedUserId, true);
                }
                return;
            }
        }
        // TODO 如果运行到这里（有一说一不太应该），或许应该定义个什么错误
        System.out.println("尝试超时");
    }
}