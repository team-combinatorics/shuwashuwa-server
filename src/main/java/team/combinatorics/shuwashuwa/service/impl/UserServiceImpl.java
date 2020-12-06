package team.combinatorics.shuwashuwa.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
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
import team.combinatorics.shuwashuwa.service.UserService;
import team.combinatorics.shuwashuwa.utils.TokenUtil;
import team.combinatorics.shuwashuwa.utils.WechatUtil;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

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
        JsonNode root = WechatUtil.getWechatInfo(logInInfoDto.getCode());
        String openid = root.path("openid").asText();
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
    public void updateUserInfo(int userid, UpdateUserInfoDTO updateUserInfoDto) {
        if(updateUserInfoDto.getComment()==null &&
                updateUserInfoDto.getDepartment()==null &&
                updateUserInfoDto.getEmail()==null &&
                updateUserInfoDto.getGrade()==null &&
                updateUserInfoDto.getIdentity()==null &&
                updateUserInfoDto.getPhoneNumber()==null &&
                updateUserInfoDto.getStudentId()==null &&
                updateUserInfoDto.getUserName()==null)
            throw new KnownException(ErrorInfoEnum.PARAMETER_LACKING);
        userDao.updateUserInfo(userid, updateUserInfoDto);
    }

    @Override
    public UserPO getUserInfo(int userid) {
        UserPO userPO = userDao.getUserByUserid(userid);
        userPO.setOpenid("你无权获取openid");
        return userPO;
    }

    @Override
    public void addVolunteerApplication(int userid, VolunteerApplicationDTO volunteerApplicationDTO) {
        VolunteerApplicationPO.VolunteerApplicationPOBuilder volunteerApplicationPOBuilder
                = VolunteerApplicationPO.builder();
        volunteerApplicationPOBuilder.userId(userid);
        if(volunteerApplicationDTO.getCardPicLocation()==null ||
                volunteerApplicationDTO.getReasonForApplication()==null)
            throw new KnownException(ErrorInfoEnum.PARAMETER_LACKING);
        volunteerApplicationPOBuilder.cardPicLocation(volunteerApplicationDTO.getCardPicLocation());
        volunteerApplicationPOBuilder.reasonForApplication(volunteerApplicationDTO.getReasonForApplication());
        VolunteerApplicationPO volunteerApplicationPO = volunteerApplicationPOBuilder.build();
        applicationDao.insert(volunteerApplicationPO);
    }

    @Override
    public List<VolunteerApplicationPO> listUnauditedVolunteerApplication() {
        return applicationDao.listApplicationsByCondition(SelectApplicationCO.builder().status(0).build());
    }

    @Override
    public void completeApplicationAudition(int userid, VolunteerApplicationUpdateDTO updateDTO) {
        if(updateDTO.getFormID()==null || updateDTO.getStatus()==null || updateDTO.getReplyByAdmin()==null)
            throw new KnownException(ErrorInfoEnum.PARAMETER_LACKING);
        applicationDao.updateApplicationByAdmin(userid, updateDTO);
        if (updateDTO.getStatus() == 1)
            userDao.updateUserVolunteerAuthority(applicationDao.getApplicationByFormId(updateDTO.getFormID()).getUserId(), true);
    }

}