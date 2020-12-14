package team.combinatorics.shuwashuwa.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import team.combinatorics.shuwashuwa.dao.UserDao;
import team.combinatorics.shuwashuwa.dao.VolunteerApplicationDao;
import team.combinatorics.shuwashuwa.dao.co.SelectApplicationCO;
import team.combinatorics.shuwashuwa.exception.ErrorInfoEnum;
import team.combinatorics.shuwashuwa.exception.KnownException;
import team.combinatorics.shuwashuwa.model.dto.*;
import team.combinatorics.shuwashuwa.model.po.UserPO;
import team.combinatorics.shuwashuwa.model.po.VolunteerApplicationPO;
import team.combinatorics.shuwashuwa.service.ImageStorageService;
import team.combinatorics.shuwashuwa.service.UserService;
import team.combinatorics.shuwashuwa.utils.DTOUtil;
import team.combinatorics.shuwashuwa.utils.TokenUtil;
import team.combinatorics.shuwashuwa.utils.WechatUtil;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    ImageStorageService imageStorageService;

    private final UserDao userDao;
    private final VolunteerApplicationDao applicationDao;

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
    public void updateUserInfo(int userid, UserInfoDTO userInfoDto) {
        if(DTOUtil.fieldAllNull(userInfoDto))
            throw new KnownException(ErrorInfoEnum.PARAMETER_LACKING);
        userDao.updateUserInfo(userid, userInfoDto);
    }

    @Override
    public UserInfoDTO getUserInfo(int userid) {
        UserPO userPO = userDao.getUserByUserid(userid);
        return (UserInfoDTO) DTOUtil.convert(userPO,UserInfoDTO.class);
    }

    @Override
    public void addVolunteerApplication(int userid, VolunteerApplicationAdditionDTO additionDTO) {
        //参数检查
        if(DTOUtil.fieldExistNull(additionDTO))
            throw new KnownException(ErrorInfoEnum.PARAMETER_LACKING);

        //重复申请检查
        if(userDao.getUserByUserid(userid).getVolunteer())
            throw new KnownException(ErrorInfoEnum.DUPLICATED_PROMOTION);

        //插入新的申请
        VolunteerApplicationPO volunteerApplicationPO =
                (VolunteerApplicationPO) DTOUtil.convert(additionDTO,VolunteerApplicationPO.class);
        volunteerApplicationPO.setId(userid);
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
                                DTOUtil.convert(x,VolunteerApplicationResponseForAdminDTO.class)
                ).collect(Collectors.toList());
    }

    @Override
    public List<VolunteerApplicationResultDTO> listVolunteerApplicationOf(int userid) {
        final List<VolunteerApplicationPO> raw = applicationDao.listApplicationsByUserId(userid);
        return raw.stream().map(
                (x) -> (VolunteerApplicationResultDTO)
                        DTOUtil.convert(x,VolunteerApplicationResultDTO.class)
        ).collect(Collectors.toList());
    }

    @Override
    public void completeApplicationAudition(int userid, VolunteerApplicationUpdateDTO updateDTO) {
        if(updateDTO.getFormID()==null || updateDTO.getStatus()==null || updateDTO.getReplyByAdmin()==null)
            throw new KnownException(ErrorInfoEnum.PARAMETER_LACKING);
        applicationDao.updateApplicationByAdmin(userid, updateDTO);
        VolunteerApplicationPO po = applicationDao.getApplicationByFormId(updateDTO.getFormID());
        imageStorageService.delete(po.getCardPicLocation());
        if (updateDTO.getStatus() == 1) {
            Integer promotedUserId = applicationDao.getApplicationByFormId(updateDTO.getFormID()).getUserId();
            userDao.updateUserVolunteerAuthority(promotedUserId, true);
        }
    }

}